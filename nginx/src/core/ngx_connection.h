
/*
 * Copyright (C) Igor Sysoev
 * Copyright (C) Nginx, Inc.
 */


#ifndef _NGX_CONNECTION_H_INCLUDED_
#define _NGX_CONNECTION_H_INCLUDED_


#include <ngx_config.h>
#include <ngx_core.h>


typedef struct ngx_listening_s  ngx_listening_t;

struct ngx_listening_s {
    ngx_socket_t        fd; // file descriptor

    struct sockaddr    *sockaddr; // ngx_create_listening ngx_set_inherited_sockets
    socklen_t           socklen;    /* size of sockaddr */
    size_t              addr_text_max_len; // NGX_*_ADDRSTRLEN,INET6,UNIX,INET,SOCKADDR
    ngx_str_t           addr_text;

    int                 type; // SOCK_STREAM, SOCK_DGRAM

    int                 backlog; // NGX_LISTEN_BACKLOG,-1,511
    int                 rcvbuf; // ngx_http_add_listening
    int                 sndbuf;
#if (NGX_HAVE_KEEPALIVE_TUNABLE)
    int                 keepidle;
    int                 keepintvl;
    int                 keepcnt;
#endif

    /* handler of accepted connection */
    ngx_connection_handler_pt   handler; // ngx_http_init_connection

    void               *servers;  /* array of ngx_http_in_addr_t, for example ngx_http_init_listening */

    ngx_log_t           log; // ngx_configure_listening_sockets ngx_http_add_listening
    ngx_log_t          *logp;

    size_t              pool_size; // set in ngx_http_add_listening, used in ngx_create_pool
    /* should be here because of the AcceptEx() preread */
    size_t              post_accept_buffer_size; // NGX_WIN32
    /* should be here because of the deferred accept */
    ngx_msec_t          post_accept_timeout; // set in ngx_http_add_listening, used in ngx_add_timer

    ngx_listening_t    *previous; // set in ngx_init_cycle, used in ngx_event_process_init
    ngx_connection_t   *connection; // set in ngx_event_process_init, used in ngx_close_listening_sockets, ngx_enable_accept_events, ngx_disable_accept_events, ngx_event_process_init
    ngx_uint_t          worker; // set in ngx_clone_listening, used in ngx_event_process_init

    unsigned            open:1; // all = 1
    unsigned            remain:1; // ?
    unsigned            ignore:1; // all = 1

    unsigned            bound:1;       /* already bound, not used */
    unsigned            inherited:1;   /* inherited from previous process, not changed */
    unsigned            nonblocking_accept:1; // not used
    unsigned            listen:1; // all = 1
    unsigned            nonblocking:1; /* TODO: nonblocking is not used yet */
    unsigned            shared:1;    /* shared between threads or processes, not used */
    unsigned            addr_ntop:1; // all = 1, set in ngx_http_add_listening, used in ngx_event_accept, ngx_event_recvmsg
    unsigned            wildcard:1; // all = 1, not changed, used in ngx_configure_listening_sockets, ngx_event_recvmsg

#if (NGX_HAVE_INET6)
    unsigned            ipv6only:1; // IPV6_V6ONLY, all = 1, not changed, used in ngx_open_listening_sockets
#endif
    unsigned            reuseport:1; // SO_REUSEPORT, set in ngx_set_inherited_sockets, ngx_http_add_listening
    unsigned            add_reuseport:1; // SO_REUSEPORT, set in ngx_init_cycle, used in ngx_open_listening_sockets
    unsigned            keepalive:2; // SO_KEEPALIVE, set in ngx_http_add_listening, used in ngx_configure_listening_sockets

    unsigned            deferred_accept:1; // not used, NGX_HAVE_DEFERRED_ACCEPT
    unsigned            delete_deferred:1; // not used, NGX_HAVE_DEFERRED_ACCEPT
    unsigned            add_deferred:1; // not used, NGX_HAVE_DEFERRED_ACCEPT
#if (NGX_HAVE_DEFERRED_ACCEPT && defined SO_ACCEPTFILTER)
    char               *accept_filter;
#endif
#if (NGX_HAVE_SETFIB)
    int                 setfib;
#endif

#if (NGX_HAVE_TCP_FASTOPEN)
    int                 fastopen; // ngx_create_listening set -1, TCP_FASTOPEN
#endif

};


typedef enum {
    NGX_ERROR_ALERT = 0,
    NGX_ERROR_ERR,
    NGX_ERROR_INFO,
    NGX_ERROR_IGNORE_ECONNRESET,
    NGX_ERROR_IGNORE_EINVAL
} ngx_connection_log_error_e;


typedef enum {
    NGX_TCP_NODELAY_UNSET = 0,
    NGX_TCP_NODELAY_SET,
    NGX_TCP_NODELAY_DISABLED
} ngx_connection_tcp_nodelay_e;


typedef enum {
    NGX_TCP_NOPUSH_UNSET = 0,
    NGX_TCP_NOPUSH_SET,
    NGX_TCP_NOPUSH_DISABLED
} ngx_connection_tcp_nopush_e;


#define NGX_LOWLEVEL_BUFFERED  0x0f
#define NGX_SSL_BUFFERED       0x01
#define NGX_HTTP_V2_BUFFERED   0x02


struct ngx_connection_s {
    void               *data;
    ngx_event_t        *read;
    ngx_event_t        *write;

    ngx_socket_t        fd;

    ngx_recv_pt         recv; // ngx_recv=ngx_os_io.ngx_unix_recv ngx_udp_recv=ngx_os_io.ngx_udp_unix_recv
    ngx_send_pt         send; // ngx_send=ngx_os_io.ngx_unix_send ngx_udp_recv=ngx_os_io.ngx_udp_unix_send
    ngx_recv_chain_pt   recv_chain; // ngx_os_io.ngx_readv_chain()
    ngx_send_chain_pt   send_chain; // ngx_os_io.ngx_writev_chain()

    ngx_listening_t    *listening; // ngx_event_process_init

    off_t               sent;

    ngx_log_t          *log;

    ngx_pool_t         *pool; // ngx_create_pool

    int                 type; // SOCK_STREAM SOCK_DGRAM

    struct sockaddr    *sockaddr; // ngx_event_accept ngx_event_recvmsg
    socklen_t           socklen; // unsigned int
    ngx_str_t           addr_text;

    ngx_str_t           proxy_protocol_addr; // ngx_proxy_protocol_read
    in_port_t           proxy_protocol_port;

#if (NGX_SSL || NGX_COMPAT)
    ngx_ssl_connection_t  *ssl;
#endif
    // ngx_connection_local_sock_addr ngx_proxy_protocol_write ngx_event_accept ngx_event_recvmsg
    struct sockaddr    *local_sockaddr;
    socklen_t           local_socklen;

    ngx_buf_t          *buffer; // ngx_create_temp_buf

    ngx_queue_t         queue; // prev and next pointers, 双向链表(Doubly Linked List)

    ngx_atomic_uint_t   number; // ngx_atomic_fetch_add(), c->log->connection = c->number;

    ngx_uint_t          requests; // ngx_http_create_request c->requests++; 计数器
// NGX_HTTP_*_BUFFERED : LOWLEVEL=0xf0,WRITE=0x10,GZIP=0x20,SSI=0x01,SUB=0x02,COPY=0x04
    unsigned            buffered:8;

    unsigned            log_error:3;     /* ngx_connection_log_error_e NGX_ERROR_IGNORE_ECONNRESET*/

    unsigned            timedout:1; // all = 1
    unsigned            error:1; // all = 1
    unsigned            destroyed:1; // changed in ngx_http_set_keepalive ngx_http_keepalive_handler

    unsigned            idle:1; // changed in ngx_http_upstream_get_keepalive_peer ngx_http_keepalive_handler
    unsigned            reusable:1; // set in ngx_reusable_connection
    unsigned            close:1; // all = 1
    unsigned            shared:1; // all = 1

    unsigned            sendfile:1; // ngx_http_update_location_config ngx_http_upstream_send_response
    unsigned            sndlowat:1; // all = 1
    unsigned            tcp_nodelay:2;   /* ngx_connection_tcp_nodelay_e NGX_TCP_NODELAY_DISABLED */
    unsigned            tcp_nopush:2;    /* ngx_connection_tcp_nopush_e NGX_TCP_NOPUSH_DISABLED */

    unsigned            need_last_buf:1; // not set, all = 1

#if (NGX_HAVE_AIO_SENDFILE || NGX_COMPAT)
    unsigned            busy_count:2;
#endif

#if (NGX_THREADS || NGX_COMPAT)
    ngx_thread_task_t  *sendfile_task;
#endif
};

// NGX_LOG_DEBUG_CONNECTION=0x80000000
#define ngx_set_connection_log(c, l)                                         \
                                                                             \
    c->log->file = l->file;                                                  \
    c->log->next = l->next;                                                  \
    c->log->writer = l->writer;                                              \
    c->log->wdata = l->wdata;                                                \
    if (!(c->log->log_level & NGX_LOG_DEBUG_CONNECTION)) {                   \
        c->log->log_level = l->log_level;                                    \
    }


ngx_listening_t *ngx_create_listening(ngx_conf_t *cf, struct sockaddr *sockaddr,
    socklen_t socklen);
ngx_int_t ngx_clone_listening(ngx_conf_t *cf, ngx_listening_t *ls);
ngx_int_t ngx_set_inherited_sockets(ngx_cycle_t *cycle);
ngx_int_t ngx_open_listening_sockets(ngx_cycle_t *cycle);
void ngx_configure_listening_sockets(ngx_cycle_t *cycle);
void ngx_close_listening_sockets(ngx_cycle_t *cycle);
void ngx_close_connection(ngx_connection_t *c);
void ngx_close_idle_connections(ngx_cycle_t *cycle);
ngx_int_t ngx_connection_local_sockaddr(ngx_connection_t *c, ngx_str_t *s,
    ngx_uint_t port);
ngx_int_t ngx_connection_error(ngx_connection_t *c, ngx_err_t err, char *text);

ngx_connection_t *ngx_get_connection(ngx_socket_t s, ngx_log_t *log);
void ngx_free_connection(ngx_connection_t *c);

void ngx_reusable_connection(ngx_connection_t *c, ngx_uint_t reusable);

#endif /* _NGX_CONNECTION_H_INCLUDED_ */
