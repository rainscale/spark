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