#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>

void signal_action(int signum, siginfo_t* siginfo, void* myact) {
    printf("signal_action catch signal %d\n", signum);
    printf("siginfo signo:%d\n", siginfo->si_signo); /* siginfo结构里保存的信号值 */
    printf("siginfo errno:%d\n", siginfo->si_errno); /* 打印出错误代码 */
    printf("siginfo code:%d\n", siginfo->si_code);   /* 打印出出错原因 */
    int i=0;
    while (i < 5) {
        printf("tid %ld\n", pthread_self());
        sleep(1);
        i++;
    }
}

void signal_handler(int signum) {
    printf("signal_handler catch signal %d\n", signum);
}

int main() {
    // 方式一
    struct sigaction sa;
    sigemptyset(&sa.sa_mask); // 清空阻塞信号
    sa.sa_flags = SA_SIGINFO; // 设置SA_SIGINFO表示传递附加信息到触发函数
    sa.sa_sigaction = signal_action;
 
    /*
    方式二
    struct sigaction sa;
    sa.sa_handler = signal_handler;
    sigemptyset(&sa.sa_mask); // 清空阻塞信号
    sa.sa_flags = 0;
    */
    if (sigaction(SIGINT, &sa, NULL) < 0) {
        perror("sigaction");
        return EXIT_FAILURE;
    }

    // printf("raise SIGINT\n");
    // raise(SIGINT);
    // kill(getpid(), SIGINT);
    while (true) {
        printf("waiting for signal...\n");
        sleep(5); // sleep期间按Ctrl+C触发SIGINT信号
    }
    return EXIT_SUCCESS;
}