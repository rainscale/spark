#include<stdio.h>
#include<stdlib.h>
#include<sys/wait.h>
#include<unistd.h>
#include<time.h>

#define PROC_NUM 10
#define DEFAULT_PID -1

int child_run() {
	srand(time(NULL));
	int t = rand() % 30;
	printf("this child pid is: %d, sleep time is: %d\n", getpid(), t);
	sleep(t);
	return 0;
}

int creat_proc(pid_t *pid, int num) {
	if(pid != NULL && num > 0) {
		for(int i = 0; i < num; i++) {
			pid_t id = fork();
			if (id < 0) {
				printf("%s: create %d proc failed\n", __FUNCTION__, i);
				return 1;
			} else if(id == 0) {
				int child_ret = child_run();
				exit(1);
			} else {
				pid[i] = id;
			}
		}
	}
	return 0;
}

int wait_proc(pid_t *pid, int num) { // 阻塞等待多个子进程
	int wait_ret = 0;
	if (pid != NULL && num > 0) {
		for (int i = 0; i < num; i++) {
			if (pid[i] == DEFAULT_PID) {
				continue;
			}
			int status = 0;
			int ret = waitpid(pid[i], &status, 0);
			if (WIFEXITED(status) && ret == pid[i]) {
				// wait子进程传入的status，它的低8位为零，次低8位为真正退出码.
				printf("wait child pid %d success, return code is: %d\n", pid[i], WEXITSTATUS(status));
			} else {
				printf("wait child pid %d failed\n", pid[i]);
				wait_ret = 1;
			}
		}
	}
	return wait_ret;
}

int main() {
    pid_t pid_list[PROC_NUM];
	for(int i = 0; i < PROC_NUM; i++) {
		pid_list[i] = DEFAULT_PID;
	}
	if (creat_proc(pid_list, sizeof(pid_list) / sizeof(pid_list[0])) == 0) {
		printf("%s: create all proc successs!\n", __FUNCTION__);
	} else {
		printf("%s: not all proc create success!\n", __FUNCTION__);
	}

	if (wait_proc(pid_list, sizeof(pid_list) / sizeof(pid_list[0])) == 0) { // 阻塞等待多个子进程结束
		printf("%s: wait all proc success!\n", __FUNCTION__);
	} else {
		printf("%s: not all proc wait success!\n", __FUNCTION__);
	}

	return EXIT_SUCCESS;
}

/*
运行结果:
this child pid is: 8595, sleep time is: 25
this child pid is: 8596, sleep time is: 25
this child pid is: 8597, sleep time is: 25
this child pid is: 8598, sleep time is: 25
this child pid is: 8599, sleep time is: 25
this child pid is: 8600, sleep time is: 25
this child pid is: 8601, sleep time is: 25
this child pid is: 8602, sleep time is: 25
main: create all proc successs!
this child pid is: 8603, sleep time is: 25
this child pid is: 8604, sleep time is: 25
wait child pid 8595 success, return code is: 1
wait child pid 8596 success, return code is: 1
wait child pid 8597 success, return code is: 1
wait child pid 8598 success, return code is: 1
wait child pid 8599 success, return code is: 1
wait child pid 8600 success, return code is: 1
wait child pid 8601 success, return code is: 1
wait child pid 8602 success, return code is: 1
wait child pid 8603 success, return code is: 1
wait child pid 8604 success, return code is: 1
main: wait all proc success!
*/
