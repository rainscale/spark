#!/usr/bin/python3

# chr() 用一个范围在 range（256）内的（就是0～255）整数作参数，返回一个对应的字符
# chr(i)
# i -- 可以是10进制也可以是16进制的形式的数字
print(chr(0x30), chr(0x31), chr(0x61)) # 0 1 a
print(chr(48), chr(49), chr(97)) # 0 1 a

# ord() 函数是 chr() 函数（对于8位的ASCII字符串）或 unichr() 函数（对于Unicode对象）的配对函数，
# 它以一个字符（长度为1的字符串）作为参数，返回对应的 ASCII 数值，或者 Unicode 数值，
# 如果所给的 Unicode 字符超出了你的 Python 定义范围，则会引发一个 TypeError 的异常。
# ord(c)
# c -- 字符
# 返回值：返回值是对应的十进制整数
print(ord('a')) # 97
print(ord('b')) # 98
print(ord('c')) # 99

# hex() 函数用于将10进制整数转换成16进制，以字符串形式表示
# hex(x)
# x -- 10进制整数
# 返回16进制数，以字符串形式表示
print(hex(255)) # 0xff
print(hex(-42)) # -0x2a
print(hex(12)) # 0xc
print(type(hex(12))) # <class 'str'>

# oct() 函数将一个整数转换成 8 进制字符串
# Python2.x 版本的 8 进制以 0 作为前缀表示
# Python3.x 版本的 8 进制以 0o 作为前缀表示
# oct(x)
# x -- 整数
# 返回值：返回 8 进制字符串
print(oct(10)) # 0o12
print(oct(20)) # 0o24
print(type(oct(15))) # <class 'str'>

# 将一个字符串或数字转换为整型
# class int(x, base=10)
# x -- 字符串或数字
# base -- 进制数，默认十进制
print(int(1)) # 1
print(int(3.6)) # 3
print(int('10011', 2)) # 19
print(int('0xa', 16)) # 10
print(int('10', 8)) # 8
print(int('1000', 10)) # 1000

# 将整数和字符串转换成浮点数
# class float([x])
# x -- 整数或字符串
print(float(1)) # 1.0
print(float(112)) # 112.0
print(float(-123.6)) # -123.6
print(float('123')) # 123.0

# 创建一个值为 real + imag * j 的复数或者转化一个字符串或数为复数。如果第一个参数为字符串，则不需要指定第二个参数
# class complex([real[, imag]])
# real -- int, long, float或字符串
# imag -- int, long, float
print(complex(1, 2)) # (1+2j)
print(complex(1)) # (1+0j)
print(complex("1")) # (1+0j)
print(complex("1+2j")) # (1+2j)

# 将对象转化为适于人阅读的形式
# class str(object='')
# object -- 对象
s = "Hello World"
print(str(s)) # Hello World
dict0 = {'baidu': 'baidu.com', 'google': 'google.com'}
print(str(dict0)) # {'baidu': 'baidu.com', 'google': 'google.com'}

# 将对象转化为供解释器读取的形式
# repr(object)
# object -- 对象
print(repr(s)) # 'Hello World'
print(repr(dict0)) # {'baidu': 'baidu.com', 'google': 'google.com'}

# 执行一个字符串表达式，并返回表达式的值
# 字符串表达式可以包含变量、函数调用、运算符和其他 Python 语法元素
# eval(expression[, globals[, locals]])
# expression -- 表达式
# globals -- 变量作用域，全局命名空间，如果被提供，则必须是一个字典对象
# locals -- 变量作用域，局部命名空间，如果被提供，可以是任何映射对象
# eval() 函数将字符串 expression 解析为 Python 表达式，并在指定的命名空间中执行它
# 返回值：eval() 函数将字符串转换为相应的对象，并返回表达式的结果
x = 7
print(eval('3 * x')) # 21
print(eval('pow(2, 2)')) # 4
print(eval('2 + 2')) # 4
n = 81
print(eval('n + 4')) # 85

# 将列表转换为元组
# tuple(iterable)
# iterable -- 要转换为元组的可迭代序列
print(tuple([1, 2, 3, 4])) # (1, 2, 3, 4)
# 针对字典 会返回字典的key组成的tuple
print(tuple({1: 2, 3: 4})) # (1, 3)

# 将元组转换为列表
# 注：元组与列表是非常类似的，区别在于元组的元素值不能修改，元组是放在括号中，列表是放于方括号中
# list(tup)
# tup -- 要转换为列表的元组
print(list((123, 'baidu', 'google', 'abc'))) # [123, 'baidu', 'google', 'abc']

# 创建一个无序不重复元素集，可进行关系测试，删除重复数据，还可以计算交集、差集、并集等
# class set([iterable])
# iterable -- 可迭代对象对象
# 返回值：返回新的集合对象
s1 = set('runoob')
s2 = set('google')
print(set('runoob')) # {'r', 'n', 'b', 'o', 'u'}
print(set('google')) # {'e', 'l', 'g', 'o'}
print(s1, s2) # {'u', 'o', 'b', 'n', 'r'} {'l', 'g', 'o', 'e'}
# 交集
print(s1 & s2) # {'o'}
# 并集
print(s1 | s2) # {'o', 'n', 'l', 'e', 'r', 'u', 'b', 'g'}
# 差集
print(s1 - s2) # {'n', 'r', 'u', 'b'}

# 创建一个字典
# class dict(**kwarg)
# class dict(mapping, **kwarg)
# class dict(iterable, **kwarg)
# **kwargs -- 关键字
# mapping -- 元素的容器，映射类型（Mapping Types）是一种关联式的容器类型，它存储了对象与对象之间的映射关系
# iterable -- 可迭代对象
# 创建空字典
print(dict()) # {}
# 传入关键字
print(dict(a = 'a', b = 'b', t = 't')) # {'a': 'a', 'b': 'b', 't': 't'}
# 映射函数方式来构造字典
print(dict(zip(['one', 'two', 'three'], [1, 2, 3]))) # {'one': 1, 'two': 2, 'three': 3}
# 可迭代对象方式来构造字典
print(dict([('one', 1), ('two', 2), ('three', 3)])) # {'one': 1, 'two': 2, 'three': 3}
numbers = dict(x = 5, y = 0)
print('numbers = ', numbers) # numbers =  {'x': 5, 'y': 0}
print(type(numbers)) # <class 'dict'>
# 可迭代对象创建字典，设置关键字参数
numbers2 = dict([('x', 5), ('y', -5)], z = 8)
print('numbers2 = ',numbers2) # numbers2 =  {'x': 5, 'y': -5, 'z': 8}
# 映射创建字典，关键字参数会被传递
numbers3 = dict({'x': 4, 'y': 5}, z = 8)
print('numbers3 = ',numbers3) # numbers3 =  {'x': 4, 'y': 5, 'z': 8}

# 创建冻结的集合，冻结后集合不能再添加或删除任何元素
# class frozenset([iterable])
# iterable -- 可迭代的对象，比如列表、字典、元组等等
# 返回值：返回新的frozenset对象，如果不提供任何参数，默认会生成空集合
print(frozenset(range(10))) # frozenset({0, 1, 2, 3, 4, 5, 6, 7, 8, 9})
print(frozenset('google')) # frozenset({'o', 'g', 'e', 'l'})