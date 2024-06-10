#include <getopt.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAX_STR_SIZE 256
static const char* program_name;

void usage(int status)
{
    if (status != EXIT_SUCCESS) {
        fprintf(stderr, "Try '%s --help' for more information.\n", program_name);
    } else {
        printf("Usage: %s [-d|--decrypt] [-i|--input infile] [-o|--output outfile] [-h|--help]\n", program_name);
    }
    exit(status);
}

int main(int argc, char** argv)
{
    char* infile;
    char* outfile;
    char filepath[256];
    FILE* in;
    FILE* out;
    int padding = 0;
    long i;
    int ch = 0;
    bool is_decrypt = false;
    int opt;
    const char* const short_options = ":hdp:i:o::";
    const struct option long_options[] = {
        {"help", no_argument, NULL, 'h'},
        {"decrypt", no_argument, NULL, 'd'},
        {"padding", required_argument, NULL, 'p'},
        {"input", required_argument, NULL, 'i'},
        {"output", required_argument, NULL, 'o'},
        {0, 0, 0, 0}
    };
    program_name = argv[0];
    while ((opt = getopt_long(argc, argv, short_options, long_options, NULL)) != -1) {
        switch (opt) {
            case '?':
                printf("Unknown option %c\n", optopt);
                break;
            case ':': // optstring以':'开头，那么缺少参数的情况下会返回':'，而不是'?'
                printf("-%c needs value\n", optopt);
                break;
            case 'h':
                usage(EXIT_SUCCESS);
                break;
            case 'd':
                is_decrypt = true;
                break;
            case 'p':
                padding = atoi(optarg);
                printf("padding: %d\n", padding);
                break;
            case 'i':
                infile = optarg;
                printf("input: %s\n", infile);
                break;
            case 'o':
                outfile = optarg;
                break;
            default:
                usage(EXIT_FAILURE);
                break;
        }
    }
    if (!infile) {
        printf("miss input parameter.\n");
        return EXIT_FAILURE;
    }
    if ((in = fopen(infile, "rb")) == NULL) {
        printf("can't open %s\n", infile);
        return EXIT_FAILURE;
    }
    if (!outfile) {
        strcpy(filepath, infile);
        if (is_decrypt) {
            strcat(filepath, ".dec");
        } else {
            strcat(filepath, ".enc");
        }
        outfile = filepath;
    }
    printf("output: %s\n", outfile);
    if ((out = fopen(outfile, "wb")) == NULL) {
        printf("can't create %s\n", outfile);
        return EXIT_FAILURE;
    }
    
    if (is_decrypt) {
        printf("decrypt......\n");
        if (padding == 0) {
            padding = fgetc(in);
        }
        fseek(in, padding, SEEK_CUR);
    } else {
        printf("encrypt......\n");
        fputc(padding, out);
        srand((unsigned int)time(NULL));
        while (i++ < padding) {
            ch = rand();
            fputc(ch, out); 
        }
    }  
    while ((ch = fgetc(in)) != EOF) {
         ch = ~ch;
         fputc(ch, out);
    }     
    fclose(in);
    fclose(out);
    return EXIT_SUCCESS;
}