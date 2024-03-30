#define _GNU_SOURCE
#include <errno.h>    /* ESRCH */
#include <execinfo.h>    /* backtrace backtrace_symbols */
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>    /* printf */
#include <string.h>    /* strerror */
#include <unistd.h>    /* sleep */
#include <stdbool.h>
#include <signal.h>    /* signal */

#define MAX_STACK_FRAMES    128
sigset_t set;
volatile bool flag_exit = false;

void handle_signal(int signum) {
    const char* name;
    void* addrlist[MAX_STACK_FRAMES];
    char** symbollist;
    int addrlen;
    switch(signum) {
        case SIGUSR1: name = "SIGUSR1"; break;
        case SIGUSR2: name = "SIGUSR2"; break;
        case SIGSEGV: name = "SIGSEGV"; break;
        case SIGABRT: name = "SIGABRT"; break;
        case SIGFPE: name = "SIGFPE"; break;
        case SIGILL: name = "SIGILL"; break;
        case SIGINT: name = "SIGINT"; break;
        case SIGTSTP: name = "SIGTSTP"; break;
        case SIGTERM: name = "SIGTERM"; break;
        default: name = "UNKNOWN";
    }
    printf("Caught %s signal, printing stack:\n", name);

    /* Retrieve current stack addresses. */
    addrlen = backtrace(addrlist, MAX_STACK_FRAMES);
    if (addrlen == 0) {
        printf("\n");
        return;
    }
    /* Create readable strings to each frame. */
    symbollist = (char**)backtrace_symbols(addrlist, addrlen);
    /* Print the stack trace, excluding calls handling the signal. */
    for (int i = 0; i < addrlen; i++) {
        printf("%d %s\n", i, symbollist[i]);
    }
    free(symbollist);
    /* Reset and raise the signal so default handler runs. */
    signal(signum, SIG_DFL);
    raise(signum);
}

void signal_handler() {
    signal(SIGUSR1, handle_signal); // 该信号被进程的信号掩码阻塞
    signal(SIGUSR2, handle_signal); // 该信号被进程的信号掩码阻塞
    signal(SIGSEGV, handle_signal); // 试图访问未分配给自己的内存，或试图往没有写权限的内存地址写数据
    signal(SIGABRT, handle_signal); // 信号中止，异常终止，调用abort函数生成的信号
    signal(SIGFPE, handle_signal); // 浮点异常，错误的算术运算，包括溢出及除数为0等其他所有的算术错误
    signal(SIGILL, handle_signal); // 信号非法指令，通常是可执行文件本身出现错误，或试图执行数据段，堆栈溢出也可能产生该信号
    signal(SIGINT, handle_signal); // 信号中断，如Ctrl+C
    signal(SIGTSTP, handle_signal); // 停止进程的运行，如Ctrl+Z
    signal(SIGTERM, handle_signal); // 程序结束(terminate)信号，与SIGKILL不同的是该信号可被阻塞和处理。通常用来要求程序自己正常退出，shell命令kill缺省产生此信号。如果程序终止不了，我们才会尝试SIGKILL
}

void *timer_fn(void* arg) {
    long i = 0;
    printf("timer tid %d running\n", gettid());
    do {
        sleep(1);
        i++;
        printf("sleep %ld s\n", i);
        if (flag_exit) {
            // pthread_exit("timer_fn exit\n");
            printf("timer_fn exit\n");
            break;
        }
    } while (true);
    return NULL;
}

void *signal_fn(void* arg) {
    int signum;
    printf("signal tid %d running\n", gettid());
    while (true) {
        sigwait(&set, &signum);
        if (SIGUSR1 == signum) {
            printf("catch SIGUSR1\n");
            flag_exit = true;
        }
        if (SIGUSR2 == signum) {
            printf("catch SIGUSR2, sleep 2s and exit\n");
            sleep(2);
            break;
        }
    }
    printf("signal_fn exit\n");
    return NULL;
}

int main(int argc, char** argv) {
    char c;
    int rc;
    pthread_t ttid;
    pthread_t stid;
    void* status;
    printf("pid %d\n", getpid());
    sigemptyset(&set);
    sigaddset(&set, SIGUSR1);
    sigaddset(&set, SIGUSR2);
    sigprocmask(SIG_SETMASK, &set, NULL); // 设置进程信号掩码，使得信号SIGUSR1,SIGUSR2阻塞
    signal_handler();

    rc = pthread_create(&ttid, NULL, timer_fn, NULL);
    if (rc != 0) {
        printf("pthread_create failed: %s\n", strerror(rc));
        return EXIT_FAILURE;
    }
    pthread_detach(ttid);
    rc = pthread_join(ttid, NULL);
    if (rc != 0) {
        printf("pthread_join ttid failed: %s\n", strerror(rc)); // 不能对一个已经处于detach状态的线程调用pthread_join，这样的调用将返回EINVAL错误。
    }
    rc = pthread_create(&stid, NULL, signal_fn, NULL);
    if (rc != 0) {
        printf("pthread_create failed: %s\n", strerror(rc));
        return EXIT_FAILURE;
    }

    while (true) {
        scanf("%c", &c);
        if ('a' == c) {
            pthread_kill(stid, SIGUSR1);
        } else if ('q' == c) {
            pthread_kill(stid, SIGUSR2);
            pthread_join(stid, &status); // 等待线程stid执行完毕，这里阻塞。
            printf("finish\n");
            break;
        }
    }
    rc = pthread_join(stid, NULL);
    if (rc != 0) {
        printf("pthread_join stid failed: %s\n", strerror(rc)); // stid已退出，报错ESRCH
    }
    return EXIT_SUCCESS;
}

/*
pid 12048
pthread_join ttid failed: Invalid argument
timer tid 12049 running
signal tid 12050 running
sleep 1 s
sleep 2 s
sleep 3 s
a
catch SIGUSR1
sleep 4 s
timer_fn exit
q
catch SIGUSR2, sleep 2s and exit
signal_fn exit
finish
pthread_join stid failed: No such process
*/