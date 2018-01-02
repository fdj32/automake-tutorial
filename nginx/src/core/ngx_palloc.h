
/*
 * Copyright (C) Igor Sysoev
 * Copyright (C) Nginx, Inc.
 */


#ifndef _NGX_PALLOC_H_INCLUDED_
#define _NGX_PALLOC_H_INCLUDED_


#include <ngx_config.h>
#include <ngx_core.h>


/*
 * NGX_MAX_ALLOC_FROM_POOL should be (ngx_pagesize - 1), i.e. 4095 on x86.
 * On Windows NT it decreases a number of locked pages in a kernel.
 */
#define NGX_MAX_ALLOC_FROM_POOL  (ngx_pagesize - 1) // ngx_pagesize = getpagesize(); 在Intel x86上其返回值应为4096Bytes = 4KB。

#define NGX_DEFAULT_POOL_SIZE    (16 * 1024) // 16K

#define NGX_POOL_ALIGNMENT       16
#define NGX_MIN_POOL_SIZE                                                     \
    ngx_align((sizeof(ngx_pool_t) + 2 * sizeof(ngx_pool_large_t)),            \
              NGX_POOL_ALIGNMENT)


typedef void (*ngx_pool_cleanup_pt)(void *data);

typedef struct ngx_pool_cleanup_s  ngx_pool_cleanup_t;

struct ngx_pool_cleanup_s {
    ngx_pool_cleanup_pt   handler; // c->handler(c->data); 清除 data 的 方法
    void                 *data;
    ngx_pool_cleanup_t   *next;
};


typedef struct ngx_pool_large_s  ngx_pool_large_t;

struct ngx_pool_large_s {
    ngx_pool_large_t     *next;
    void                 *alloc;
};


typedef struct {
    u_char               *last; // pool 的结尾 p->d.last = (u_char *) p + sizeof(ngx_pool_t);
    u_char               *end; // 该数据块结尾处的内存地址 p->d.end = (u_char *) p + size;
    ngx_pool_t           *next; // 下一个pool 指针
    ngx_uint_t            failed; // 在此数据块上申请内存失败的次数
} ngx_pool_data_t;


struct ngx_pool_s {
    ngx_pool_data_t       d; // pool 放数据的data块
    size_t                max; // pool一次分配的最大内存，超出则分配到large区
    ngx_pool_t           *current; // pool当前的数据块指针
    ngx_chain_t          *chain; // created by ngx_alloc_chain_link
    ngx_pool_large_t     *large; // created by ngx_palloc_large
    ngx_pool_cleanup_t   *cleanup; // created by ngx_pool_cleanup_add
    ngx_log_t            *log; // come from "log = ngx_cycle->log;"
};


typedef struct {
    ngx_fd_t              fd;
    u_char               *name;
    ngx_log_t            *log;
} ngx_pool_cleanup_file_t;


void *ngx_alloc(size_t size, ngx_log_t *log);
void *ngx_calloc(size_t size, ngx_log_t *log);

ngx_pool_t *ngx_create_pool(size_t size, ngx_log_t *log);
void ngx_destroy_pool(ngx_pool_t *pool);
void ngx_reset_pool(ngx_pool_t *pool);

void *ngx_palloc(ngx_pool_t *pool, size_t size);
void *ngx_pnalloc(ngx_pool_t *pool, size_t size);
void *ngx_pcalloc(ngx_pool_t *pool, size_t size);
void *ngx_pmemalign(ngx_pool_t *pool, size_t size, size_t alignment);
ngx_int_t ngx_pfree(ngx_pool_t *pool, void *p);


ngx_pool_cleanup_t *ngx_pool_cleanup_add(ngx_pool_t *p, size_t size);
void ngx_pool_run_cleanup_file(ngx_pool_t *p, ngx_fd_t fd);
void ngx_pool_cleanup_file(void *data);
void ngx_pool_delete_file(void *data);


#endif /* _NGX_PALLOC_H_INCLUDED_ */
