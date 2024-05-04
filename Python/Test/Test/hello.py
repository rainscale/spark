import os

'''
id()函数返回对象的唯一标识符，标识符是一个整数。
CPython 中 id() 函数用于获取对象的内存地址。
语法
id([object])
参数说明：
object -- 对象。
返回值
返回对象的内存地址。
'''
print('在hello.py文件中%s' % id(os)) # 在hello.py文件中131779366391136
print('Hello World!') # Hello World!