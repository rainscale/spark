#include<stdio.h>
#include<unistd.h>
#include<stdlib.h>
#include<sys/wait.h>
 
int main() {
	pid_t pid;
	pid = fork();
	if (pid < 0) {
		printf("%s fork error\n", __FUNCTION__);
		return EXIT_FAILURE;
	} else if (pid == 0) {
		printf("child run, pid is: %d\n", getpid());
		sleep(5);
		exit(257);
	} else {
		int status = 0;
        pid_t ret = 0;

        // 方式一：阻塞等待
		// ret = waitpid(-1, &status, 0); //阻塞式等待，和wait等价
        ret = wait(&status); //阻塞式等待
		printf("this is test for wait\n");

        /*
        // 方式二：非阻塞等待
        do {
            ret = waitpid(-1, &status, WNOHANG); // 非阻塞等待
            if (ret == 0) {
                printf("child is running\n");
            }
            sleep(1);
        } while (ret == 0);
        */

		if (WIFEXITED(status) && ret == pid) { // WIFEXITED(status): 若为正常终止子进程返回的状态,则为真.(查看进程是否是正常退出)
			printf("wait child success, child return code is: %d-%d\n", status, WEXITSTATUS(status)); // WEXITSTATUS(status): 若WIFEXITED非0，提取子进程退出码.（查看进程的退出码)
		} else {
			printf("wait child failed, return.\n");
			return EXIT_FAILURE;
		}
	}
	return EXIT_SUCCESS;
}

/*
方式一：
child run, pid is: 7621
this is test for wait
wait child success, child return code is: 256-1

方式二：
child is running
child run, pid is: 7733
child is running
child is running
child is running
child is running
wait child success, child return code is: 256-1
*/