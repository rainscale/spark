#!/usr/bin/python
# -*- coding: UTF-8 -*-
import os

# 给出当前的目录
print(os.getcwd())
# 打开一个文件
fo = open("foo.txt", "w")
print("文件名: ", fo.name)
print("是否已关闭 : ", fo.closed)
print("访问模式 : ", fo.mode)

fo.write("www.google.com!\nVery good site!\n")

# 关闭打开的文件
fo.close()

fo = open("foo.txt", "r+")
str = fo.read(10)
print("读取的字符串是 : ", str) # 读取的字符串是 :  www.google
# 关闭打开的文件
fo.close()