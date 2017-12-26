
/*
 * Copyright (C) Igor Sysoev
 * Copyright (C) Nginx, Inc.
 */


#include <ngx_config.h>
#include <ngx_core.h>


/*
 * The time may be updated by signal handler or by several threads.
 * The time update operations are rare and require to hold the ngx_time_lock.
 * The time read operations are frequent, so they are lock-free and get time
 * values and strings from the current slot.  Thus thread may get the corrupted
 * values only if it is preempted while copying and then it is not scheduled
 * to run more than NGX_TIME_SLOTS seconds.
 */

#define NGX_TIME_SLOTS   64 // 时间值和各种格式字符串的数组长度，滚动存储ngx_time_update，0->63

static ngx_uint_t        slot; // 当前下标
static ngx_atomic_t      ngx_time_lock; // 更新时间需要的写锁
// https://www.cnblogs.com/yc_sunniwell/archive/2010/06/24/1764231.html volatile 每次都需要真真切切地读内存地址，不能通过读寄存器来偷懒
volatile ngx_msec_t      ngx_current_msec; // 当前毫秒数
volatile ngx_time_t     *ngx_cached_time; // 当前时间
volatile ngx_str_t       ngx_cached_err_log_time; //"yyyy/MM/dd HH:mm:ss"
volatile ngx_str_t       ngx_cached_http_time; //"EEE, dd MMM yyyy HH:mm:ss zzz"
volatile ngx_str_t       ngx_cached_http_log_time;//"dd/MMM/yyyy:HH:mm:ss Z"
volatile ngx_str_t       ngx_cached_http_log_iso8601;//"yyyy-MM-ddTHH:mm:ssZ"需要添加冒号
volatile ngx_str_t       ngx_cached_syslog_time;//"MMM dd HH:mm:ss"

#if !(NGX_WIN32)

/*
 * localtime() and localtime_r() are not Async-Signal-Safe functions, therefore,
 * they must not be called by a signal handler, so we use the cached
 * GMT offset value. Fortunately the value is changed only two times a year.
 */

static ngx_int_t         cached_gmtoff;
#endif

static ngx_time_t        cached_time[NGX_TIME_SLOTS];
static u_char            cached_err_log_time[NGX_TIME_SLOTS]
                                    [sizeof("1970/09/28 12:00:00")];
static u_char            cached_http_time[NGX_TIME_SLOTS]
                                    [sizeof("Mon, 28 Sep 1970 06:00:00 GMT")];
static u_char            cached_http_log_time[NGX_TIME_SLOTS]
                                    [sizeof("28/Sep/1970:12:00:00 +0600")];
static u_char            cached_http_log_iso8601[NGX_TIME_SLOTS]
                                    [sizeof("1970-09-28T12:00:00+06:00")];
static u_char            cached_syslog_time[NGX_TIME_SLOTS]
                                    [sizeof("Sep 28 12:00:00")];


static char  *week[] = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" }; // 从周日开始算
static char  *months[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                           "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

void
ngx_time_init(void)
{// birthday of Igor Sysoev ? https://www.csdn.net/article/2013-09-09/2816874-this-russian-software-is-taking-over-the-internet
    ngx_cached_err_log_time.len = sizeof("1970/09/28 12:00:00") - 1; // 结果等于strlen，但是有区别 http://blog.csdn.net/21aspnet/article/details/1539951
    ngx_cached_http_time.len = sizeof("Mon, 28 Sep 1970 06:00:00 GMT") - 1;
    ngx_cached_http_log_time.len = sizeof("28/Sep/1970:12:00:00 +0600") - 1;
    ngx_cached_http_log_iso8601.len = sizeof("1970-09-28T12:00:00+06:00") - 1;
    ngx_cached_syslog_time.len = sizeof("Sep 28 12:00:00") - 1;
// 在2002年，42岁的他开始启动这一项目，同年十月发布了第一段公共简码。年份能对上……
    ngx_cached_time = &cached_time[0]; // 当前时间指向数组第一位

    ngx_time_update(); // 开始更新
}


void
ngx_time_update(void)
{
    u_char          *p0, *p1, *p2, *p3, *p4; // 指向各个格式化时间字符串的指针
    ngx_tm_t         tm, gmt;
    time_t           sec; // 当前秒数
    ngx_uint_t       msec; // 当前毫秒数
    ngx_time_t      *tp; // 指向当前slot时间数组的指针
    struct timeval   tv; // 存储gettimeofday结果

    if (!ngx_trylock(&ngx_time_lock)) { // 原子操作，当成锁来使用
        return;
    }

    ngx_gettimeofday(&tv); // gettimeofday(tp, NULL), 忽略时区

    sec = tv.tv_sec; // 距离Epoch时间的秒数1970-01-01 00:00:00 UTC
    msec = tv.tv_usec / 1000; // 微秒数/1000=毫秒数 tv_usec is microseconds, so msec is milliseconds

    ngx_current_msec = (ngx_msec_t) sec * 1000 + msec;

    tp = &cached_time[slot]; // slot静态变量，默认从0开始

    if (tp->sec == sec) {
        tp->msec = msec;
        ngx_unlock(&ngx_time_lock); // 如果还是当前秒数没变化，只更新毫秒数，其他的字符串不用变化，直接解锁返回
        return;
    }

    if (slot == NGX_TIME_SLOTS - 1) {
        slot = 0; // 由63滚回0
    } else {
        slot++; // ++直到63
    }

    tp = &cached_time[slot]; // 指向下一个slot时间数组值，开始更新

    tp->sec = sec;
    tp->msec = msec;

    ngx_gmtime(sec, &gmt); // 把秒数转换成公历年月日时分秒星期X，GMT时间


    p0 = &cached_http_time[slot][0];

    (void) ngx_sprintf(p0, "%s, %02d %s %4d %02d:%02d:%02d GMT",
                       week[gmt.ngx_tm_wday], gmt.ngx_tm_mday,
                       months[gmt.ngx_tm_mon - 1], gmt.ngx_tm_year,
                       gmt.ngx_tm_hour, gmt.ngx_tm_min, gmt.ngx_tm_sec);

#if (NGX_HAVE_GETTIMEZONE)

    tp->gmtoff = ngx_gettimezone();
    ngx_gmtime(sec + tp->gmtoff * 60, &tm);

#elif (NGX_HAVE_GMTOFF)

    ngx_localtime(sec, &tm); // 本地时间
    cached_gmtoff = (ngx_int_t) (tm.ngx_tm_gmtoff / 60); // 分钟偏移 long	tm_gmtoff;	/* offset from UTC in seconds */
    tp->gmtoff = cached_gmtoff; // 分钟偏移用于计算时区

#else

    ngx_localtime(sec, &tm);
    cached_gmtoff = ngx_timezone(tm.ngx_tm_isdst);
    tp->gmtoff = cached_gmtoff;

#endif


    p1 = &cached_err_log_time[slot][0];

    (void) ngx_sprintf(p1, "%4d/%02d/%02d %02d:%02d:%02d",
                       tm.ngx_tm_year, tm.ngx_tm_mon,
                       tm.ngx_tm_mday, tm.ngx_tm_hour,
                       tm.ngx_tm_min, tm.ngx_tm_sec);


    p2 = &cached_http_log_time[slot][0];

    (void) ngx_sprintf(p2, "%02d/%s/%d:%02d:%02d:%02d %c%02i%02i",
                       tm.ngx_tm_mday, months[tm.ngx_tm_mon - 1],
                       tm.ngx_tm_year, tm.ngx_tm_hour,
                       tm.ngx_tm_min, tm.ngx_tm_sec,
                       tp->gmtoff < 0 ? '-' : '+',
                       ngx_abs(tp->gmtoff / 60), ngx_abs(tp->gmtoff % 60));

    p3 = &cached_http_log_iso8601[slot][0];

    (void) ngx_sprintf(p3, "%4d-%02d-%02dT%02d:%02d:%02d%c%02i:%02i",
                       tm.ngx_tm_year, tm.ngx_tm_mon,
                       tm.ngx_tm_mday, tm.ngx_tm_hour,
                       tm.ngx_tm_min, tm.ngx_tm_sec,
                       tp->gmtoff < 0 ? '-' : '+',
                       ngx_abs(tp->gmtoff / 60), ngx_abs(tp->gmtoff % 60));

    p4 = &cached_syslog_time[slot][0];

    (void) ngx_sprintf(p4, "%s %2d %02d:%02d:%02d",
                       months[tm.ngx_tm_mon - 1], tm.ngx_tm_mday,
                       tm.ngx_tm_hour, tm.ngx_tm_min, tm.ngx_tm_sec);

    ngx_memory_barrier();

    ngx_cached_time = tp;
    ngx_cached_http_time.data = p0;
    ngx_cached_err_log_time.data = p1;
    ngx_cached_http_log_time.data = p2;
    ngx_cached_http_log_iso8601.data = p3;
    ngx_cached_syslog_time.data = p4;

    ngx_unlock(&ngx_time_lock);//解锁
}


#if !(NGX_WIN32)

void
ngx_time_sigsafe_update(void)
{ // ngx_signal_handler()
    u_char          *p, *p2;
    ngx_tm_t         tm;
    time_t           sec;
    ngx_time_t      *tp;
    struct timeval   tv;

    if (!ngx_trylock(&ngx_time_lock)) {
        return;
    }

    ngx_gettimeofday(&tv);

    sec = tv.tv_sec;

    tp = &cached_time[slot];

    if (tp->sec == sec) {
        ngx_unlock(&ngx_time_lock);
        return;
    }

    if (slot == NGX_TIME_SLOTS - 1) {
        slot = 0;
    } else {
        slot++;
    }

    tp = &cached_time[slot];

    tp->sec = 0;

    ngx_gmtime(sec + cached_gmtoff * 60, &tm); // GMT转localtime
// 只需要cached_err_log_time & cached_syslog_time？
    p = &cached_err_log_time[slot][0];

    (void) ngx_sprintf(p, "%4d/%02d/%02d %02d:%02d:%02d",
                       tm.ngx_tm_year, tm.ngx_tm_mon,
                       tm.ngx_tm_mday, tm.ngx_tm_hour,
                       tm.ngx_tm_min, tm.ngx_tm_sec);

    p2 = &cached_syslog_time[slot][0];

    (void) ngx_sprintf(p2, "%s %2d %02d:%02d:%02d",
                       months[tm.ngx_tm_mon - 1], tm.ngx_tm_mday,
                       tm.ngx_tm_hour, tm.ngx_tm_min, tm.ngx_tm_sec);

    ngx_memory_barrier();

    ngx_cached_err_log_time.data = p; // ngx_log_error_core()
    ngx_cached_syslog_time.data = p2; // ngx_syslog_add_header()

    ngx_unlock(&ngx_time_lock);
}

#endif


u_char *
ngx_http_time(u_char *buf, time_t t)
{
    ngx_tm_t  tm;

    ngx_gmtime(t, &tm);

    return ngx_sprintf(buf, "%s, %02d %s %4d %02d:%02d:%02d GMT",
                       week[tm.ngx_tm_wday],
                       tm.ngx_tm_mday,
                       months[tm.ngx_tm_mon - 1],
                       tm.ngx_tm_year,
                       tm.ngx_tm_hour,
                       tm.ngx_tm_min,
                       tm.ngx_tm_sec);
}


u_char *
ngx_http_cookie_time(u_char *buf, time_t t)
{
    ngx_tm_t  tm;

    ngx_gmtime(t, &tm);

    /*
     * Netscape 3.x does not understand 4-digit years at all and
     * 2-digit years more than "37"
     */

    return ngx_sprintf(buf,
                       (tm.ngx_tm_year > 2037) ?
                                         "%s, %02d-%s-%d %02d:%02d:%02d GMT":
                                         "%s, %02d-%s-%02d %02d:%02d:%02d GMT",
                       week[tm.ngx_tm_wday],
                       tm.ngx_tm_mday,
                       months[tm.ngx_tm_mon - 1],
                       (tm.ngx_tm_year > 2037) ? tm.ngx_tm_year:
                                                 tm.ngx_tm_year % 100,
                       tm.ngx_tm_hour,
                       tm.ngx_tm_min,
                       tm.ngx_tm_sec);
}


void
ngx_gmtime(time_t t, ngx_tm_t *tp)
{
    ngx_int_t   yday;
    ngx_uint_t  n, sec, min, hour, mday, mon, year, wday, days, leap;

    /* the calculation is valid for positive time_t only */

    n = (ngx_uint_t) t;

    days = n / 86400;

    /* January 1, 1970 was Thursday */

    wday = (4 + days) % 7; // Wed,Thu,Fri,Sat

    n %= 86400; // 距离当天00:00:00秒数
    hour = n / 3600;
    n %= 3600;
    min = n / 60;
    sec = n % 60;

    /*
     * the algorithm based on Gauss' formula,
     * see src/http/ngx_http_parse_time.c
     */
    // 719527 = 365*1970+1970/4-1970/100+1970/400=719050+492.5-19+4=719527.5, From March 1, 1 BC To March 1, 1971 BC, 再减掉1971 Jan + Feb (31+28)
    /* days since March 1, 1 BC */
    days = days - (31 + 28) + 719527;
// 闰年需要把公元元年剔除，所以该算法就是将公元元年的Jan 和 Feb 剔除，从March 1, 1 BC开始算
    /*
     * The "days" should be adjusted to 1 only, however, some March 1st's go
     * to previous year, so we adjust them to 2.  This causes also shift of the
     * last February days to next year, but we catch the case when "yday"
     * becomes negative.
     */
// 400年里，有100-4+1个闰年，总天数为(365 * 400 + 100 - 4 + 1)=146097，平均每年146097/400=365.2425天
    year = (days + 2) * 400 / (365 * 400 + 100 - 4 + 1);

    yday = days - (365 * year + year / 4 - year / 100 + year / 400);

    if (yday < 0) {
        leap = (year % 4 == 0) && (year % 100 || (year % 400 == 0));
        yday = 365 + leap + yday;
        year--;
    }

    /*
     * The empirical formula that maps "yday" to month.
     * There are at least 10 variants, some of them are:
     *     mon = (yday + 31) * 15 / 459
     *     mon = (yday + 31) * 17 / 520
     *     mon = (yday + 31) * 20 / 612
     */

    mon = (yday + 31) * 10 / 306;

    /* the Gauss' formula that evaluates days before the month */

    mday = yday - (367 * mon / 12 - 30) + 1;

    if (yday >= 306) {

        year++;
        mon -= 10;

        /*
         * there is no "yday" in Win32 SYSTEMTIME
         *
         * yday -= 306;
         */

    } else {

        mon += 2;

        /*
         * there is no "yday" in Win32 SYSTEMTIME
         *
         * yday += 31 + 28 + leap;
         */
    }

    tp->ngx_tm_sec = (ngx_tm_sec_t) sec;
    tp->ngx_tm_min = (ngx_tm_min_t) min;
    tp->ngx_tm_hour = (ngx_tm_hour_t) hour;
    tp->ngx_tm_mday = (ngx_tm_mday_t) mday;
    tp->ngx_tm_mon = (ngx_tm_mon_t) mon;
    tp->ngx_tm_year = (ngx_tm_year_t) year;
    tp->ngx_tm_wday = (ngx_tm_wday_t) wday;
}


time_t
ngx_next_time(time_t when)
{
    time_t     now, next;
    struct tm  tm;

    now = ngx_time();

    ngx_libc_localtime(now, &tm);

    tm.tm_hour = (int) (when / 3600);
    when %= 3600;
    tm.tm_min = (int) (when / 60);
    tm.tm_sec = (int) (when % 60);

    next = mktime(&tm);

    if (next == -1) {
        return -1;
    }

    if (next - now > 0) {
        return next;
    }

    tm.tm_mday++;

    /* mktime() should normalize a date (Jan 32, etc) */

    next = mktime(&tm);

    if (next != -1) {
        return next;
    }

    return -1;
}
