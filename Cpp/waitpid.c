#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>
#include <signal.h>

int main() {
    pid_t pid;
    pid_t cpid;
    int status;
    printf("pid %d\n", getpid());
    pid = fork();

    if (pid < 0) {
        perror("fork()");
        exit(EXIT_FAILURE);
    } else if (pid == 0) {
        puts("in child process");
        printf("\tchild pid %d\n", getpid());
        printf("\tchild ppid %d\n", getppid());
        sleep(3); // 1、让子进程睡眠3s，父进程会等待子进程结束才返回；2、换成30s，然后kill -9 子进程pid
        exit(EXIT_SUCCESS);
    } else {
        // 用法一
        cpid = waitpid(pid, &status, 0);
        printf("waitpid return %d\n", cpid);
        puts("in parent");
        printf("\tparent pid %d\n", getpid());
        printf("\tparent ppid %d\n", getppid());
        printf("\tchild process exited with status %d\n", status);

        /*
        // 用法二
        do {
            cpid = waitpid(pid, &status, WNOHANG);
            if (cpid == 0) {
                printf("no child exited\n");
                sleep(1);
            }
        } while (cpid == 0);
        if (WIFEXITED(status)) {
            printf("正常退出：%d\n", cpid);
        }
        if (WIFSIGNALED(status) && WTERMSIG(status) == SIGKILL) {
            // 使用kill -9 子进程id
            printf("%d被SIGKILL信号结束\n", cpid);
        }
        */
    }
    exit(EXIT_SUCCESS);
}

/* 用法一
pid 13113
in child process
	child pid 13114
	child ppid 13113
waitpid return 13114
in parent
	parent pid 13113
	parent ppid 4141
	child process exited with status 0
*/

/* 用法二
pid 13941
no child exited
in child process
	child pid 13942
	child ppid 13941
no child exited
no child exited
no child exited
no child exited
no child exited
no child exited
no child exited
13942被SIGKILL信号结束
*/