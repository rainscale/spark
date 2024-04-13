#!/usr/bin/python3
import getopt
import sys


def usage():
    print('usage: getopt_test.py\n'
          '-h help \n'
          '-i inputfile\n'
          '-o outputfile\n'
          '')


def main(argv):
    inputfile = ''
    outputfile = ''
    try:
        opts, args = getopt.getopt(argv, "hi:o:", ["ifile=", "ofile="])
    except getopt.GetoptError:
        print('GetoptError')
        usage()
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            usage()
            sys.exit()
        elif opt in ("-i", "--ifile"):
            inputfile = arg
        elif opt in ("-o", "--ofile"):
            outputfile = arg
    print('Input file is', inputfile)
    print('Output file is', outputfile)


if __name__ == "__main__":
    print('Number of arguments:', len(sys.argv), 'arguments.')
    print('Argument List:', str(sys.argv))
    print("Path:", sys.path)
    main(sys.argv[1:])
