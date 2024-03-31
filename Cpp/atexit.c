#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

void func_1(void) {
	printf("my is %s\n",__FUNCTION__);
}

void func_2(void) {
	printf("my is %s\n",__FUNCTION__);
}

void func_3(void) {
	printf("my is %s\n",__FUNCTION__);
}

int main(int argc,char **argv) {
    // exit以登记这些函数的相反顺序调用它们
	atexit(func_1);
	atexit(func_2);
	atexit(func_3);
	atexit(func_3);
	atexit(func_1);
	
	printf("main func end\n");
	
	return EXIT_SUCCESS;
}

/*
main func end
my is func_1
my is func_3
my is func_3
my is func_2
my is func_1
*/