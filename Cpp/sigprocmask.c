#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
 
void handler(int sig) {
    printf("Deal with SIGINT\n");  // SIGINT信号处理函数
}

int main() {
    int i = 0;
    sigset_t newmask;
    sigset_t oldmask;
    sigset_t pendmask;
    struct sigaction act;
    act.sa_handler = handler;  // handler为信号处理函数首地址
    sigemptyset(&act.sa_mask);
    act.sa_flags = 0;
    sigaction(SIGINT, &act, 0);  // 信号捕捉函数，捕捉Ctrl+C
    sigemptyset(&newmask); // 初始化信号量集
    sigaddset(&newmask, SIGINT); // 将SIGINT添加到信号量集中
    sigprocmask(SIG_BLOCK, &newmask, &oldmask); //将newmask中的SIGINT阻塞掉，并保存当前信号屏蔽字到Oldmask
    //sleep(5); 
    // 休眠5秒钟，说明:在5s休眠期间，任何SIGINT信号都会被阻塞，如果在5s内收到任何键盘的Ctrl+C信号，则此时会把这些信息存在内核的队列中，等待5s结束后，可能要处理此信号。 
    while (i < 5) {
        sleep(1);
        i++;
        printf("sleep %d s\n", i);
    }
    sigpending(&pendmask); // 检查信号是悬而未决的,所谓悬而未决，是指SIGINT被阻塞还没有被处理
    if (sigismember(&pendmask, SIGINT)) {
        printf("SIGINT pending\n");
    }
 
    printf("SIGINT unblocked\n");
    sigprocmask(SIG_SETMASK, &oldmask, NULL); // 恢复被屏蔽的信号SIGINT，
    i = 0;
    // sleep(5);
    // 此处开始处理信号，此时Ctrl+C信号会调用信号处理函数  
    while (i < 5) {
        sleep(1);
        i++;
        printf("sleep %d s\n", i);
    }
    return EXIT_SUCCESS;
}