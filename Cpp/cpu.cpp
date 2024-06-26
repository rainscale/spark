#include <errno.h>
#include <execinfo.h>    /* backtrace backtrace_symbols */
#include <sched.h>    /* sched_setaffinity */
#include <signal.h>    /* signal */
#include <stdio.h>
#include <stdlib.h>    /* free */
#include <string.h>    /* strstr */
#include <sys/wait.h>    /* wait */

char* get_current_task_name() {
    static char task_name[1024] = {0};
    char* pstr = nullptr;
    readlink("/proc/self/exe", task_name, 1024);
    pstr = task_name;
    return pstr;
}

char* addr2line(const char* ptr, const char* elf_name) {
    char *pstr = nullptr;
    pid_t pid = 0;
    static char addr2line_cmd[1024] = {0};
    sprintf(addr2line_cmd, "addr2line %s -e %s -f -p", ptr, elf_name);
    pid = fork();
    if (pid == 0) {
        system(addr2line_cmd);
        exit(0);
    } else {
        wait(nullptr);
    }
    return pstr;
}

#define MAX_STACK_FRAMES    128
void handle_signal(int signum) {
    const char* name;
    void* frames[MAX_STACK_FRAMES];
    char** symbols;
    int num_frames;
    char* ptr = nullptr;
    char* ptr_end = nullptr;
    char* pstr = nullptr;
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
    num_frames = backtrace(frames, MAX_STACK_FRAMES);
    if (num_frames == 0) {
        printf("\n");
        return;
    }
    /* Create readable strings to each frame. */
    symbols = (char**)backtrace_symbols(frames, num_frames);
    /* Print the stack trace, excluding calls handling the signal. */
    //for (int i = 0; i < num_frames; i++) {
    //    printf("%d %s\n", i, symbols[i]);
    //}
    for (int i = 0; i < num_frames; i++) {
        ptr = strstr(symbols[i], "[");
        if (ptr != nullptr) {
            ptr++;
            ptr_end = strstr(ptr, "]");
            if (ptr_end != nullptr) {
                *ptr_end = '\0';
            }
            pstr = addr2line(ptr, get_current_task_name());
        }
    }
    free(symbols);
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

bool LockToCPU(int cpu_to_lock) {
    cpu_set_t cpuset;
    CPU_ZERO(&cpuset);
    CPU_SET(cpu_to_lock, &cpuset);
    if (sched_setaffinity(0, sizeof(cpuset), &cpuset) != 0) {
        if (errno == EINVAL) {
            printf("Invalid cpu %d\n", cpu_to_lock);
        } else {
            perror("sched_setaffinity failed\n");
        }
        return false;
    }
    return true;
}

int main(int argc, char** argv) {
    long i = 0;
    printf("pid %d\n", getpid());
    signal_handler();
    while (true) {
        sleep(1);
        i++;
        printf("sleep %ld s\n", i);
    }
    return 0;
}