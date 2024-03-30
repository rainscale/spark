#define _GNU_SOURCE
#include <pthread.h>
#include <stdio.h>    /* printf */
#include <stdlib.h>    /* EXIT_SUCCESS EXIT_FAILURE */
#include <string.h>    /* strerror */
#include <unistd.h>    /* getpid gettid(-D_GNU_SOURCE) */

#define NUM_THREADS    4
// 全局变量(Global Variable)，在所有函数外部定义的变量，作用域默认是整个程序，也就是所有的源文件，包括.c和.h文件。
pthread_t threads[NUM_THREADS];
int ids[NUM_THREADS] = {1, 2, 3, 4};

void printids(const char *s) {
    pid_t pid;
    pthread_t tid;
    pid = getpid();
    tid = pthread_self();
    printf ("%s pid %d tid %lu (0x%lx)\n", s, pid, tid, tid);
}

void* thr_fn(void* args) {
    int id = (int)(*((int*)args));
    printf("new thread %d-%d running\n", gettid(), id);
    if (id == 1) {
        pthread_exit("pthread 1 invoke pthread_exit");
    } else {
        return NULL;
    }
}

int main(int argc, char** argv) {
    int err;
    void* retval;
    printf("pid %d\n", getpid());
    for (int i = 0; i < NUM_THREADS; i++) {
        err = pthread_create(&threads[i], NULL, thr_fn, &ids[i]); // 传递参数时需要使用全局变量或malloc出来的变量，不能用局部变量
        if (err != 0) {
            printf("pthread_create failed: %s\n", strerror(err));
            return EXIT_FAILURE;
        } else {
            printf("pthread_create %lu success\n", threads[i]);
        }
    }
    printids("main thread:");
    for (int i = 0; i < NUM_THREADS; i++) {
        int rc = pthread_join(threads[i], &retval); // 阻塞等待ntid线程退出，ntid线程返回码在retval
        printf("thread %lu return value(retval): %s\n", threads[i], (char*)retval);
        printf("pthread_join %d return %d\n", ids[i], rc);
    }
    return EXIT_SUCCESS;
}

/*
pid 10057
pthread_create 139853301806656 success
new thread 10058-1 running
pthread_create 139853293413952 success
new thread 10059-2 running
pthread_create 139853285021248 success
new thread 10060-3 running
pthread_create 139853276628544 success
main thread: pid 10057 tid 139853305112384 (0x7f3222926740)
thread 139853301806656 return value(retval): pthread 1 invoke pthread_exit
pthread_join 1 return 0
thread 139853293413952 return value(retval): (null)
pthread_join 2 return 0
thread 139853285021248 return value(retval): (null)
pthread_join 3 return 0
new thread 10061-4 running
thread 139853276628544 return value(retval): (null)
pthread_join 4 return 0
*/