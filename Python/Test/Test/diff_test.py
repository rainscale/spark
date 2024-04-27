#!/usr/bin/python3
# -*- coding: utf-8 -*-


def compare_file(file1, file2):
    with open(file1, 'r') as f1:
        text1 = f1.read()
        lines1 = text1.splitlines()
    with open(file2, 'r') as f2:
        text2 = f2.read()
        lines2 = text2.splitlines()
    count = 0
    same_lines = []
    for line1 in lines1:
        count += 1
        same = [line for line in lines2 if line == line1]
        if len(same) > 0:
            print('%s same count：%d' % (line1, len(same)))
            same_lines.append(count)
    f1.close()
    f2.close()
    return same_lines


file1 = input('输入第一个文件：')
file2 = input('输入第二个文件：')
samer = compare_file(file1, file2)
if len(samer) == 0:
    print('两个文件完全不一样')
else:
    print('%s共有%d处相同，相同的行号有：%s' % (file1, len(samer), samer))