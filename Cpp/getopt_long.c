#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>

#define print_opt_help(opt_index, help_str)             \
                    do {                                \
                        printf("\t--%s\t-%c\t%s", long_options[opt_index].name, (char)long_options[opt_index].val, help_str); \
                    } while (0)
#define DEBUG_INFO(format, ...) printf("%s:%d@%s "format"\n", __FILE__, __LINE__, __func__, ##__VA_ARGS__)

char* const short_options = ":vn::l:";

int lopt;
static struct option long_options[] = {
    {"verbose", no_argument, NULL, 'v'},
    {"name", optional_argument, NULL, 'n'},
    {"love", required_argument, NULL, 'l'},
    {"http-proxy", required_argument, &lopt, 1},
    {0, 0, 0, 0}
};

void usage() {
    printf("\nUsage:\n");
    print_opt_help(0, "verbose\n");
    print_opt_help(1, "your name\n");
    print_opt_help(2, "love action\n");
    printf("\nExamples:\n");
    printf("\t./getopt_long -nJack\t---\t方式一\n");
    printf("\t./getopt_long -v --name=Jack -l love\t---\t方式二\n");
}

int main(int argc, char* argv[]) {
    int opt;
    int option_index = 0;

    DEBUG_INFO("no_argument = %d", no_argument);
    DEBUG_INFO("required_argument = %d", required_argument);
    DEBUG_INFO("optional_argument = %d", optional_argument);

    while((opt = getopt_long(argc, argv, short_options, long_options, &option_index)) != -1) {
        switch (opt) {
            case 0:
                printf("Option %s", long_options[option_index].name);
                if (optarg)
                    printf(" with arg %s", optarg);
                printf("\n");
                break;
            case 'v':
                printf("Option %c\n", opt);
                break;
            case 'n':
                printf("Option %c with optional argument %s\n", opt, optarg);
                break;
            case 'l':
                printf("Option %c with value %s\n", opt, optarg);
                break;
            case ':':
                DEBUG_INFO("Unkown option: %c needs a parameter", optopt);
                usage();
                break;
            default:
                usage();
                printf("Unknown option: %d\n", opt);
                printf("optopt = %c\n", (char)optopt);
                printf("opterr = %d\n", opterr);
                exit(EXIT_FAILURE);
        }
    }

    if (optind < argc) {
        printf("non-option argv elements: ");
        while (optind < argc) {
            printf("%s ", argv[optind++]);
        }
        printf("\n");
    }
}

/*
$ ./build_linux/getopt_long -v --http-proxy=127.0.0.1:80 --name=100 -n101 --love love -n101 -lyou -l you --lo ve -l
/home/spark/Works/spark/Cpp/getopt_long.c:36@main no_argument = 0
/home/spark/Works/spark/Cpp/getopt_long.c:37@main required_argument = 1
/home/spark/Works/spark/Cpp/getopt_long.c:38@main optional_argument = 2
Option v
Option http-proxy with arg 127.0.0.1:80
Option n with optional argument 100
Option n with optional argument 101
Option l with value love
Option n with optional argument 101
Option l with value you
Option l with value you
Option l with value ve
/home/spark/Works/spark/Cpp/getopt_long.c:58@main Unkown option: l needs a parameter

Usage:
	--verbose	-v	verbose
	--name	-n	your name
	--love	-l	love action

Examples:
	./getopt_long -nJack	---	方式一
	./getopt_long -v --name=Jack -l love	---	方式二
*/