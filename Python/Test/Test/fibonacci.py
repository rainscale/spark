#!/usr/bin/python3
# coding=utf-8

# Fibonacci series: 斐波纳契数列
# 两个元素的总和确定了下一个数
a, b = 0, 1
while b < 1000:
    print(b, end=',')
    a, b = b, a + b
# 1,1,2,3,5,8,13,21,34,55,89,144,233,377,610,987,