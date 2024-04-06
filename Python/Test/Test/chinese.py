#!/usr/bin/python3
# coding: utf-8
# coding=utf-8
# -*- coding: UTF-8 -*-

import sys

# python单行注释采用#开头，多行注释使用三个单引号 ''' 或三个双引号 """
'''
注意：Python3.X源码文件默认使用utf-8编码，所以可以正常解析中文，无需指定 UTF-8 编码。
'''

"""
注意：如果你使用编辑器，同时需要设置 py 文件存储的格式为 UTF-8，否则会出现类似以下错误信息：
SyntaxError: (unicode error) ‘utf-8’ codec can’t decode byte 0xc4 in position 0:
invalid continuation byte
Pycharm 设置步骤：
进入 file > Settings，在输入框搜索 encoding。
找到 Editor > File encodings，将 IDE Encoding 和 Project Encoding 设置为utf-8。
"""

# Python3默认可以正常解析中文
print("你好，世界")

item_one = 100 # 赋值整形变量
item_two = 100.0 # 浮点型
item_three = -0x64 # long类型只存在于Python2.X版本中，在2.2以后的版本中，int类型数据溢出后会自动转为long类型。在Python3.X版本中long类型被移除，使用int替代。
# 可以使用斜杠（\）将一行的语句分为多行显示
total = item_one + \
        item_two + \
        item_three
print(total) # 100.0

# 语句中包含[], {}或()括号就不需要使用多行连接符
days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday',
        'Friday', 'Saturday', 'Sunday']

print('Number of arguments:', len(sys.argv), 'arguments.')
print('Argument List:', str(sys.argv))

# 第一个注释
print('Hello, Python!') # 第二个注释

# 在Python3中， print函数的参数end默认值为"\n"，即end="\n"，表示换行，
# 给end赋值为空, 即end=""，就不会换行了
# Hello,World!
print('Hello,', end="")
print('World!')
# 可以设置一些特色符号或字符串，如end="@":admin@127.0.0.1
print('admin', end="@")
print('127.0.0.1')

input("按下enter键退出，其他任意键显示...\n")
