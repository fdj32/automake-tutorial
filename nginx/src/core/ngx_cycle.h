
/*
 * Copyright (C) Igor Sysoev
 * Copyright (C) Nginx, Inc.
 */


#ifndef _NGX_CYCLE_H_INCLUDED_
#define _NGX_CYCLE_H_INCLUDED_


#include <ngx_config.h>
#include <ngx_core.h>


#ifndef NGX_CYCLE_POOL_SIZE
#define NGX_CYCLE_POOL_SIZE     NGX_DEFAULT_POOL_SIZE // 16K
#endif


#define NGX_DEBUG_POINTS_STOP   1 // raise(SIGSTOP);
#define NGX_DEBUG_POINTS_ABORT  2 // ngx_abort(); abort();


typedef struct ngx_shm_zone_s  ngx_shm_zone_t;

typedef ngx_int_t (*ngx_shm_zone_init_pt) (ngx_shm_zone_t *zone, void *data);

struct ngx_shm_zone_s {
    void                     *data; // see init methods
    ngx_shm_t                 shm;
    ngx_shm_zone_init_pt      init; // ngx_http_*_init_zone, conn, req, upstream, file
    void                     *tag; // &ngx_http_fastcgi_module, &ngx_http_limit_conn_module, &ngx_http_limit_req_module, &ngx_http_proxy_module, &ngx_http_scgi_module, &ngx_http_upstream_module, &ngx_http_uwsgi_module, cmd->post
    ngx_uint_t                noreuse;  /* unsigned  noreuse:1; */
};


struct ngx_cycle_s {
    void                  ****conf_ctx; // cycle->conf_ctx = ngx_pcalloc(pool, ngx_max_module * sizeof(void *)); ngx_is_init_cycle判断条件
    ngx_pool_t               *pool;

    ngx_log_t                *log;
    ngx_log_t                 new_log; // ngx_error_log() ngx_log_set_log()

    ngx_uint_t                log_use_stderr;  /* unsigned  log_use_stderr:1; ngx_log_set_log(), cf->cycle->log_use_stderr = 1; not changed*/

    ngx_connection_t        **files; // ngx_get_connection(), ngx_event_process_init()
    ngx_connection_t         *free_connections;
    ngx_uint_t                free_connection_n;

    ngx_module_t            **modules;
    ngx_uint_t                modules_n;
    ngx_uint_t                modules_used;    /* unsigned  modules_used:1; ngx_count_modules() cycle->modules_used = 1; not changed*/

    ngx_queue_t               reusable_connections_queue; // ngx_queue_init()
    ngx_uint_t                reusable_connections_n;

    ngx_array_t               listening; // ngx_array_init()
    ngx_array_t               paths;

    ngx_array_t               config_dump;
    ngx_rbtree_t              config_dump_rbtree;
    ngx_rbtree_node_t         config_dump_sentinel; // ngx_rbtree_init() 哨兵

    ngx_list_t                open_files;
    ngx_list_t                shared_memory;

    ngx_uint_t                connection_n;
    ngx_uint_t                files_n; // cycle->files_n = (ngx_uint_t) rlmt.rlim_cur;

    ngx_connection_t         *connections; // ngx_event_process_init()
    ngx_event_t              *read_events; // ngx_event_process_init()
    ngx_event_t              *write_events; // ngx_event_process_init()

    ngx_cycle_t              *old_cycle; // ngx_init_cycle()

    ngx_str_t                 conf_file; // ngx_get_options() 'c'
    ngx_str_t                 conf_param; // ngx_get_options() 'g'
    ngx_str_t                 conf_prefix; // ngx_get_options() 'p'
    ngx_str_t                 prefix; // ngx_get_options() 'p'
    ngx_str_t                 lock_file; // ngx_core_module_init_conf(), ngx_test_lockfile(), ngx_event_module_init(), ngx_shmtx_create()
    ngx_str_t                 hostname; // gethostname()
};


typedef struct {
    ngx_flag_t                daemon; // long, ngx_core_module_create_conf(),NGX_CONF_UNSET=-1, ngx_core_module_init_conf(), ngx_conf_init_value(), -1->1, ngx_daemon()
    ngx_flag_t                master; // long, ngx_core_module_create_conf(),NGX_CONF_UNSET=-1, ngx_core_module_init_conf(), ngx_conf_init_value(), -1->1, ngx_process = NGX_PROCESS_MASTER;

    ngx_msec_t                timer_resolution; // unsigned long, ngx_core_module_create_conf(),NGX_CONF_UNSET=-1, ngx_core_module_init_conf(), ngx_conf_init_msec_value(ccf->timer_resolution, 0); ngx_event_module_init(); ngx_timer_resolution
    ngx_msec_t                shutdown_timeout; // ngx_add_timer(&ngx_shutdown_event, ccf->shutdown_timeout);

    ngx_int_t                 worker_processes; // ngx_set_worker_processes(), auto = sysctl hw.ncpu
    ngx_int_t                 debug_points; // 0, 1=raise(SIGSTOP); 2=ngx_abort();

    ngx_int_t                 rlimit_nofile; // #define	RLIMIT_NOFILE	8		/* number of open files */
    off_t                     rlimit_core; // #define	RLIMIT_CORE	4		/* core file size */

    int                       priority; // ngx_set_priority(), ngx_core_commands
// http://blog.csdn.net/lgq421033770/article/details/51787273
    ngx_uint_t                cpu_affinity_auto; // NGX_HAVE_CPU_AFFINITY, worker_cpu_affinity, ngx_set_cpu_affinity()
    ngx_uint_t                cpu_affinity_n;
    ngx_cpuset_t             *cpu_affinity;

    char                     *username; // NGX_USER->ngx_set_user()
    ngx_uid_t                 user; // passwd.pw_uid
    ngx_gid_t                 group; // passwd.pw_gid

    ngx_str_t                 working_directory; // chdir
    ngx_str_t                 lock_file; // ngx_test_lockfile, NGX_LOCK_PATH, logs/nginx.lock

    ngx_str_t                 pid; // NGX_PID_PATH logs/nginx.pid
    ngx_str_t                 oldpid; // logs/nginx.pid.oldbin

    ngx_array_t               env; // ngx_set_env()
    char                    **environment; // ngx_set_environment()
} ngx_core_conf_t; // ngx_get_conf() ngx_core_module_create_conf()


#define ngx_is_init_cycle(cycle)  (cycle->conf_ctx == NULL)


ngx_cycle_t *ngx_init_cycle(ngx_cycle_t *old_cycle);
ngx_int_t ngx_create_pidfile(ngx_str_t *name, ngx_log_t *log);
void ngx_delete_pidfile(ngx_cycle_t *cycle);
ngx_int_t ngx_signal_process(ngx_cycle_t *cycle, char *sig);
void ngx_reopen_files(ngx_cycle_t *cycle, ngx_uid_t user);
char **ngx_set_environment(ngx_cycle_t *cycle, ngx_uint_t *last);
ngx_pid_t ngx_exec_new_binary(ngx_cycle_t *cycle, char *const *argv);
ngx_cpuset_t *ngx_get_cpu_affinity(ngx_uint_t n);
ngx_shm_zone_t *ngx_shared_memory_add(ngx_conf_t *cf, ngx_str_t *name,
    size_t size, void *tag);
void ngx_set_shutdown_timer(ngx_cycle_t *cycle);


extern volatile ngx_cycle_t  *ngx_cycle;
extern ngx_array_t            ngx_old_cycles; // count=10, created in ngx_init_cycle(), destroyed in ngx_clean_old_cycles()
extern ngx_module_t           ngx_core_module; // ngx_modules[]
extern ngx_uint_t             ngx_test_config; // ngx_get_options() 't' 'T'
extern ngx_uint_t             ngx_dump_config; // ngx_get_options() 'T'
extern ngx_uint_t             ngx_quiet_mode; // ngx_get_options() 'q', whether output configuration test result to stderr, ngx_log_stderr()


#endif /* _NGX_CYCLE_H_INCLUDED_ */
