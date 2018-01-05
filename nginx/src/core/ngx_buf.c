
/*
 * Copyright (C) Igor Sysoev
 * Copyright (C) Nginx, Inc.
 */


#include <ngx_config.h>
#include <ngx_core.h>


ngx_buf_t *
ngx_create_temp_buf(ngx_pool_t *pool, size_t size)
{
    ngx_buf_t *b;

    b = ngx_calloc_buf(pool); // 从 pool 分配一个 buf 结构体
    if (b == NULL) {
        return NULL;
    }

    b->start = ngx_palloc(pool, size); // 从pool 分配一块 size 大小的内存
    if (b->start == NULL) {
        return NULL;
    }

    /* ngx_memzero(p, size);
     * set by ngx_calloc_buf():
     *
     *     b->file_pos = 0;
     *     b->file_last = 0;
     *     b->file = NULL;
     *     b->shadow = NULL;
     *     b->tag = 0;
     *     and flags
     */

    b->pos = b->start; // 内存块起始位置
    b->last = b->start; // 内存块起始位置
    b->end = b->last + size; // 内存块终止位置
    b->temporary = 1; // 临时性的

    return b;
}


ngx_chain_t *
ngx_alloc_chain_link(ngx_pool_t *pool)
{
    ngx_chain_t  *cl;

    cl = pool->chain;

    if (cl) { // pop pool的chain链表头
        pool->chain = cl->next;
        return cl;
    }
// 从 pool 中分配 一个 chain
    cl = ngx_palloc(pool, sizeof(ngx_chain_t));
    if (cl == NULL) {
        return NULL;
    }

    return cl;
}


ngx_chain_t *
ngx_create_chain_of_bufs(ngx_pool_t *pool, ngx_bufs_t *bufs)
{ // 功能未完成，没有被调用
    u_char       *p;
    ngx_int_t     i;
    ngx_buf_t    *b;
    ngx_chain_t  *chain, *cl, **ll;

    p = ngx_palloc(pool, bufs->num * bufs->size);
    if (p == NULL) {
        return NULL;
    }

    ll = &chain;

    for (i = 0; i < bufs->num; i++) {

        b = ngx_calloc_buf(pool);
        if (b == NULL) {
            return NULL;
        }

        /*
         * set by ngx_calloc_buf():
         *
         *     b->file_pos = 0;
         *     b->file_last = 0;
         *     b->file = NULL;
         *     b->shadow = NULL;
         *     b->tag = 0;
         *     and flags
         *
         */

        b->pos = p;
        b->last = p;
        b->temporary = 1;

        b->start = p;
        p += bufs->size;
        b->end = p;

        cl = ngx_alloc_chain_link(pool);
        if (cl == NULL) {
            return NULL;
        }

        cl->buf = b;
        *ll = cl; // 此处看不懂
        ll = &cl->next;
    }

    *ll = NULL;

    return chain;
}


ngx_int_t
ngx_chain_add_copy(ngx_pool_t *pool, ngx_chain_t **chain, ngx_chain_t *in)
{ // 把in 添加到*chain 尾部
    ngx_chain_t  *cl, **ll;

    ll = chain;
// 取 指向 chain 的结尾指针的next 指针 到 ll，到了结尾处此时 *ll = NULL，通过修改 ll 实现尾部添加元素
    for (cl = *chain; cl; cl = cl->next) {
        ll = &cl->next;
    }

    while (in) {
        cl = ngx_alloc_chain_link(pool);
        if (cl == NULL) {
            return NGX_ERROR;
        }

        cl->buf = in->buf;
        *ll = cl; // 添加cl到尾部
        ll = &cl->next; // ll指向cl->next 指针地址，此时值为 未知
        in = in->next; // in 后移直至 NULL
    }

    *ll = NULL; // 设置结尾处的next指针

    return NGX_OK;
}


ngx_chain_t *
ngx_chain_get_free_buf(ngx_pool_t *p, ngx_chain_t **free)
{
    ngx_chain_t  *cl;

    if (*free) { // *free 不为NULL
        cl = *free;
        *free = cl->next; // 取出free 头节点，修改free 指向 第二节点
        cl->next = NULL; // 头节点不再指向第二节点
        return cl; // 返回孤儿头节点
    }
// *free 为 NULL，则在pool 上创建一个新的
    cl = ngx_alloc_chain_link(p);
    if (cl == NULL) {
        return NULL;
    }

    cl->buf = ngx_calloc_buf(p);
    if (cl->buf == NULL) {
        return NULL;
    }

    cl->next = NULL;

    return cl;
}


void
ngx_chain_update_chains(ngx_pool_t *p, ngx_chain_t **free, ngx_chain_t **busy,
    ngx_chain_t **out, ngx_buf_tag_t tag)
{ // 把 out 和 busy 链表以 ==tag 为条件分别放到p->chain 或者 free 链表
    ngx_chain_t  *cl;

    if (*out) { // *out 不为NULL
        if (*busy == NULL) {
            *busy = *out;

        } else {
            for (cl = *busy; cl->next; cl = cl->next) { /* void */ }
// 把 out append 到 busy 后面
            cl->next = *out;
        }

        *out = NULL;
    }

    while (*busy) { // 把 busy 取出循环
        cl = *busy;

        if (ngx_buf_size(cl->buf) != 0) {
            break;
        }
// buf size = 0
        if (cl->buf->tag != tag) { // 比较 tag 干什么？
            *busy = cl->next;
            ngx_free_chain(p, cl); // tag 不相同，把cl 加入到 p->chain 链表头
            continue;
        }
// tag 相同 buf清空
        cl->buf->pos = cl->buf->start;
        cl->buf->last = cl->buf->start;

        *busy = cl->next;
        cl->next = *free; // 把cl 放到 free 链表头
        *free = cl;
    }
}


off_t
ngx_chain_coalesce_file(ngx_chain_t **in, off_t limit)
{ // ngx_darwin_sendfile_chain.c fix bug
    off_t         total, size, aligned, fprev;
    ngx_fd_t      fd;
    ngx_chain_t  *cl;

    total = 0;

    cl = *in;
    fd = cl->buf->file->fd;

    do {
        size = cl->buf->file_last - cl->buf->file_pos;

        if (size > limit - total) {
            size = limit - total;

            aligned = (cl->buf->file_pos + size + ngx_pagesize - 1)
                       & ~((off_t) ngx_pagesize - 1);
// aligned = ngx_align(cl->buf->file_pos + size, ngx_pagesize)
            if (aligned <= cl->buf->file_last) {
                size = aligned - cl->buf->file_pos;
            }

            total += size;
            break;
        }

        total += size;
        fprev = cl->buf->file_pos + size;
        cl = cl->next;

    } while (cl
             && cl->buf->in_file
             && total < limit
             && fd == cl->buf->file->fd
             && fprev == cl->buf->file_pos);

    *in = cl; // 为啥要修改这个指针？

    return total;
}


ngx_chain_t *
ngx_chain_update_sent(ngx_chain_t *in, off_t sent)
{
    off_t  size;

    for ( /* void */ ; in; in = in->next) {

        if (ngx_buf_special(in->buf)) {
            continue;
        }

        if (sent == 0) {
            break;
        }

        size = ngx_buf_size(in->buf);

        if (sent >= size) {
            sent -= size;

            if (ngx_buf_in_memory(in->buf)) {
                in->buf->pos = in->buf->last;
            }

            if (in->buf->in_file) {
                in->buf->file_pos = in->buf->file_last;
            }

            continue;
        }

        if (ngx_buf_in_memory(in->buf)) {
            in->buf->pos += (size_t) sent;
        }

        if (in->buf->in_file) {
            in->buf->file_pos += sent;
        }

        break;
    }

    return in;
}
