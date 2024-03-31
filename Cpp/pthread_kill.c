#define _GNU_SOURCE
#include <errno.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>
#include <unistd.h>
 
#define NUM_THREADS    3

static void checkResults(char* string, int rc) {
    if (rc) {
        printf("Error on: %s, rc = %d", string, rc);
        exit(EXIT_FAILURE);
    }
    return;
}
 
void* thread_fn(void*parm) {
    pthread_t self = pthread_self();
    pid_t tid = gettid();
    int rc;
    printf("Thread 0x%lx %d entered\n", self, tid);
    errno = 0;
    rc = sleep(30);
    if (rc != 0 && errno == EINTR) {
        printf("Thread 0x%lx %d got a signal delivered to it\n", self, tid);
        return NULL;
    }
    printf("Thread 0x%lx %d did not get expected results! rc=%d, errno=%d\n", self, tid, rc, errno);
    return NULL;
}

void signal_handler(int signum)
{
    pthread_t self = pthread_self();
    pid_t tid = gettid();
    printf("Thread 0x%lx %d catch %d in signal handler\n", self, tid, signum);
    return;
}

int main(int argc, char** argv) {
    int rc;
    int i;
    struct sigaction act;
    pthread_t threads[NUM_THREADS];
 
    printf("Enter Testcase - %s\n", argv[0]);
    printf("Set up the alarm handler for the process\n");

    memset(&act, 0, sizeof(act));
    sigemptyset(&act.sa_mask);
    act.sa_flags = 0;
    act.sa_handler = signal_handler;
 
    rc = sigaction(SIGALRM, &act, NULL);
    checkResults("sigaction\n", rc);
 
    for (i = 0; i < NUM_THREADS; ++i) {
        rc = pthread_create(&threads[i], NULL, thread_fn, NULL);
        checkResults("pthread_create()", rc);
    }
 
    sleep(3);

    for (i = 0; i < NUM_THREADS; ++i) {
        rc = pthread_kill(threads[i], SIGALRM);
        checkResults("pthread_kill()", rc);
    }
 
    for (i = 0; i < NUM_THREADS; ++i) {
        rc = pthread_join(threads[i], NULL);
        checkResults("pthread_join()", rc);
    }

    printf("Main completed\n");
    return EXIT_SUCCESS;
}