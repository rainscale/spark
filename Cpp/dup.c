#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
/*
dup2()用来复制参数oldfd所指的文件描述符，并将它拷贝至参数newfd后一块返回。
若参数newfd为一已打开的文件描述符，则newfd所指的文件会先被关闭。
dup2()所复制的文件描述符，与原来的文件描述符共享各种文件状态，详情可参考dup()。
返回值:
当复制成功时，则返回最小及尚未使用的文件描述符。若有错误则返回-1，errno会存放错误代码。
附加说明:
dup2()相当于调用fcntl(oldfd，F_DUPFD，newfd)；请参考fcntl()。
错误代码:
EBADF 参数fd非有效的文件描述词，或该文件已关闭
函数定义：int dup2(int oldfd, int newfd);
头文件：unistd.h
运行结果:
fd1 = 3, fd2 = 4
write to fd1: 5
read from fd2: 5
read content: hello
*/
int main() {
    int fd1, fd2;
    char buf[1024];
    int len;

    fd1 = open("test.txt", O_RDWR);
    if (fd1 == -1) {
        printf("Failed to open test.txt!\n");
        exit(1);
    }

    fd2 = dup(fd1);
    if (fd2 == -1) {
        printf("Failed to duplicate file descriptor!\n");
        exit(1);
    }

    printf("fd1 = %d, fd2 = %d\n", fd1, fd2);

    // 向fd1写入数据
    len = write(fd1, "hello", 5);
    printf("write to fd1: %d\n", len);
    fsync(fd1); // 将缓冲区数据写回磁盘
    lseek(fd1, 0, SEEK_SET); // 移动文件的读写位置

    // 从fd2读取数据
    len = read(fd2, buf, 1024);
    printf("read from fd2: %d\n", len);
    printf("read content: %s\n", buf);

    close(fd1);
    close(fd2);
    exit(0);
}
