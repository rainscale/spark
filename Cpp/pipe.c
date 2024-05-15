#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>
#define BUFFER_SIZE 1024

/*
int main() {
    int pipe_fd[2];
    pid_t pid;
    char* buffer = malloc(sizeof(char) * BUFFER_SIZE);
    if (buffer == NULL) {
        perror("动态内存申请失败");
        return -1;
    }
    // 创建管道
    if (pipe(pipe_fd) < 0) {
        perror("无法创建管道");
        exit(1);
    }
    
    // 设置读取端为非阻塞
    //int flags = fcntl(pipe_fd[0], F_GETFL);
    //if (flags == -1) {
    //    perror("fcntl");
    //    exit(1);
    //}
    //flags |= O_NONBLOCK;
    //if (fcntl(pipe_fd[0], F_SETFL, flags) == -1) {
    //    perror("fcntl");
    //    exit(1);
    //}
    
    // 创建子进程，使得父进程和子进程都能共享管道。
    pid = fork();
    if (pid < 0) {
        perror("无法创建子进程");
        exit(1);
    } else if (pid == 0) { // 子进程
        printf("这里是子进程：%d\n", getpid());
        close(pipe_fd[0]);      // 关闭读取端，只保留写入端
        const char* str = "Hello, World!";
        if (write(pipe_fd[1], str, strlen(str)) == strlen(str)) {
            printf("子进程已经写完了!\n");
        } else {
            perror("子进程写管道时发生了错误");
        }        
    } else { // 父进程
        printf("这里是父进程：%d\n", getpid());
        close(pipe_fd[1]);      // 关闭写入端，只保留读取端
        memset(buffer, 0, BUFFER_SIZE);  // 先清空缓冲区内容
        if (read(pipe_fd[0], buffer, BUFFER_SIZE) > 0) { // 阻塞模式会等待读数据时阻塞
            printf("父进程读取到消息了： %s\n", buffer);
        } else {
            perror("无法从管道中读取数据"); // 非阻塞模式报错：Resource temporarily unavailable
        }
        wait(NULL); // 等待子进程结束
    }
    free(buffer);
    exit(0);
}
*/

/*
这里是父进程：6222
这里是子进程：6223
子进程已经写完了!
父进程读取到消息了： Hello, World!
*/


int handle_cmd(int cmd) { // 子进程的命令处理函数
    if (cmd < 0 || cmd > 256) {
        printf("child: invalid command\n");
        return -1;
    }
    printf("child: the cmd from parent is %d\n", cmd);
    return 0;
}
    
int main() {
    int pipe_fd[2];
    pid_t pid;
    char r_buf[4];
    char** w_buf[256];
    int child_exit = 0;
    int i;
    int cmd;
    
    memset(r_buf, 0, sizeof(r_buf));
    
    if (pipe(pipe_fd) < 0) {
        perror("pipe create error");
        exit(1);
    }
    
    pid = fork();
    
    if (pid == 0 ) { // 子进程：解析从管道中获取的命令，并作相应的处理
        printf("子进程：%d\n", getpid());
        close(pipe_fd[1]);
        sleep(2);
        while (!child_exit) {
            read(pipe_fd[0], r_buf, 4);
            cmd = atoi(r_buf);
            if (cmd == 0) {
                printf("receive cmd from parent over now child process exit\n");
                child_exit = 1;
            } else if (handle_cmd(cmd) != 0) {
                printf("handle_cmd failed\n");
            }
            sleep(1);
        }
        close(pipe_fd[0]);
        exit(0);
    } else if (pid > 0) { // 父进程：send commands to child
        printf("父进程：%d\n", getpid());
        close(pipe_fd[0]);
        w_buf[0] = "003";
        w_buf[1] = "005";
        w_buf[2] = "777";
        w_buf[3] = "000";
        for (i = 0; i < 4; i++) {
            write(pipe_fd[1], w_buf[i], 4);
        }
        close(pipe_fd[1]);
        wait(NULL); // 等待子进程结束
    }
    exit(0);
}
      
/*
父进程：7065
子进程：7066
child: the cmd from parent is 3
child: the cmd from parent is 5
child: invalid command
handle_cmd failed
receive cmd from parent over now child process exit
*/
