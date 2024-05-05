#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

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