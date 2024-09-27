#!/usr/bin/python3

# 从左到右索引默认0开始的，最大范围是字符串长度少1
# 从右到左索引默认-1开始的，最大范围是字符串开头
# a b c d e f
# 0 1 2 3 4 5
#-6-5-4-3-2-1
s = 'abcdef'

print(s) # abcedf
print(s[0]) # a

# [头下标:尾下标]获取的子字符串包含头下标的字符，但不包含尾下标的字符
print(s[1:5]) # bcde

# 输出从第三个字符开始的字符串
print(s[2:]) # cdef

# 步长2
print(s[1:4:2]) # bd

# 加号（+）是字符串连接运算符，星号（*）是重复操作
print(s * 2) # abcdefabcdef
print(s + "ghijklmn") # abcdefghijklmn
print('a' in s) # True
print('x' not in s) #True
'''
原始字符串：所有的字符串都是直接按照字面的意思来使用，没有转义特殊或不能打印的字符。
原始字符串除在字符串的第一个引号前加上字母 r（可以大小写）以外，与普通字符串有着几乎完全相同的语法。
'''
print(r"\n") # \n
print(R"\n") # \n

para_str = """这是一个多行字符串的实例
多行字符串可以使用制表符
TAB ( \t )。
也可以使用换行符 [ \n ]。
"""
print(para_str)

name='Python'
print('hello %s' % name) # hello Python
print(f'hello {name}') # hello Python
x = 1
print(f'{x+1=}') # x+1=2

# 列表
list = ['a', 'b', 'c', 'd', 'e']
tinylist = [123, 'john']

print(list) # ['a', 'b', 'c', 'd', 'e']
print(list[0]) # a
print(list[1:3]) # ['b', 'c']
print(list[2:]) # ['c', 'd', 'e']
print(list * 2) # ['a', 'b', 'c', 'd', 'e', 'a', 'b', 'c', 'd', 'e']
print(list + tinylist) # ['a', 'b', 'c', 'd', 'e', 123, 'john']
list[2] = 1000
print(list) # ['a', 'b', 1000, 'd', 'e']
del list[2]
print(list) # ['a', 'b', 'd', 'e']

# 嵌套列表
a = ['a', 'b', 'c']
n = [1, 2, 3]
m = [a, n]
print(m) # [['a', 'b', 'c'], [1, 2, 3]]
print(m[0]) # ['a', 'b', 'c']
print(m[0][1]) # b

# 元组
tuple = ('a', 7, 2.3, 'Jack', 70.2)
tinytuple = (123, 'john')
print(tuple) # ('a', 7, 2.3, 'Jack', 70.2)
print(tuple[0]) # a
print(tuple[1:3]) # (7, 2.3)
print(tuple[2:]) # (2.3, 'Jack', 70.2)
print(tuple * 2) # ('a', 7, 2.3, 'Jack', 70.2, 'a', 7, 2.3, 'Jack', 70.2)
print(tuple + tinytuple) # ('a', 7, 2.3, 'Jack', 70.2, 123, 'john')
# 元组不允许更新
# tuple[2] = 1000 # TypeError: 'tuple' object does not support item assignment

# 字典
dict = {}
dict['one'] = "This is one"
dict[2] = "This is two"
tinydict = {'name': 'Frank', 'code': 9527, 'dept': 'sales'}

# 输出键为'one' 的值
print(dict['one']) # This is one
# 输出键为 2 的值
print(dict[2]) # This is two
# 输出完整的字典
print(tinydict) # {'name': 'Frank', 'code': 9527, 'dept': 'sales'}
# 输出所有键
print(tinydict.keys()) # dict_keys(['name', 'code', 'dept'])
# 输出所有值
print(tinydict.values()) # dict_values(['Frank', 9527, 'sales'])