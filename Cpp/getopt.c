#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>    /* getopt */

int main(int argc, char* argv[]) {
    int verbose = 0;
    int n = 0;
    int opt;
    while ((opt = getopt(argc, argv, ":vn:c::d:")) != -1) {
        switch (opt) {
            case '?':
                printf("Unknown option %c\n", optopt);
                break;
            case ':': // optstring以':'开头，那么缺少参数的情况下会返回':'，而不是'?'
                printf("-%c needs value\n", optopt);
                break;
            case 'v':
                printf("Option %c\n", opt);
                verbose = 1;
                break;
            case 'n':
                printf("Option %c with value %s\n", opt, optarg);
                n = atoi(optarg);
                printf("n = %d\n", n);
                break;
            case 'c':
                printf("Option %c with optional argument %s\n", opt, optarg);
                break;
            case 'd':
                printf("Option %c with value %s\n", opt, optarg);
                break;
            default:
                fprintf(stderr, "Unknown option\n");
                printf("usage: ...\n");
                exit(EXIT_FAILURE);
        }
    }
    // print the rest of the command line
    for (int i = optind; i < argc; i++) {
        printf("Argument: %s\n", argv[i]);
    }
    return EXIT_SUCCESS;
}

/*
$ ./build_linux/getopt file -v -n 100 -c200 -o q -d
Option v
Option n with value 100
n = 100
Option c with optional argument 200
Unknown option o
-d needs value
Argument: file
Argument: q
*/