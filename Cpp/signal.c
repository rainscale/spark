#include <stdio.h>
#include <stdlib.h>    /* printf */
#include <unistd.h>    /* sleep */
#include <stdbool.h>
#include <signal.h>    /* signal */
#include <execinfo.h>

#define MAX_STACK_FRAMES    128
void handle_signal(int signum) {
    const char* name;
    void* addrlist[MAX_STACK_FRAMES];
    char** symbollist;
    int addrlen;
    switch(signum) {
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
    signal(SIGSEGV, handle_signal); // 试图访问未分配给自己的内存，或试图往没有写权限的内存地址写数据
    signal(SIGABRT, handle_signal); // 信号中止，异常终止，调用abort函数生成的信号
    signal(SIGFPE, handle_signal); // 浮点异常，错误的算术运算，包括溢出及除数为0等其他所有的算术错误
    signal(SIGILL, handle_signal); // 信号非法指令，通常是可执行文件本身出现错误，或试图执行数据段，堆栈溢出也可能产生该信号
    signal(SIGINT, handle_signal); // 信号中断，如Ctrl+C
    signal(SIGTSTP, handle_signal); // 停止进程的运行，如Ctrl+Z
    signal(SIGTERM, handle_signal); // 程序结束(terminate)信号，与SIGKILL不同的是该信号可被阻塞和处理。通常用来要求程序自己正常退出，shell命令kill缺省产生此信号。如果程序终止不了，我们才会尝试SIGKILL
}

int main(int argc, char** argv) {
    long i = 0;
    printf("pid %d\n", getpid());
    signal_handler();

    do {
        sleep(1);
        i++;
        printf("sleep %ld s\n", i);
    } while (true);

    return 0;
}