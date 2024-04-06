#!/usr/bin/python3
"""
运算符优先级，从最高到最低优先级的所有运算符:
运算符	                    描述
**	                        指数 (最高优先级)
~ + -	                    按位翻转, 一元加号和减号 (最后两个的方法名为 +@ 和 -@)
* / % //	                乘，除，取模和取整除
+ -	                        加法减法
>> <<	                    右移，左移运算符
&	                        位 'AND'
^ |	                        位运算符
<= < > >=	                比较运算符
<> == !=	                等于运算符
= %= /= //= -= += *= **=	赋值运算符
is is not	                身份运算符
in not in	                成员运算符
not and or	                逻辑运算符
"""

# 算术运算符
a = 21
b = 10
c = 0
c = a + b
print("a + b = ", c) # 31
c = a - b
print("a - b = ", c) # 11
c = a / b
print("a / b = ", c) # 2.1
# 取模 - 返回除法的余数
c = a % b
print("a % b = ", c) # 1
a = 2
b = 3
# 幂 - 返回x的y次幂
c = a ** b
print("a ** b = ", c) # 8
a = 9
b = 2
# 取整除 - 返回商的整数部分（向下取整）
c = a // b
print("a // b = ", c) # 4
a = -9
c = a // b
print("a // b = ", c) # -5

# 比较运算符
a = 21
b = 10
c = 0
if a == b:
    print("a等于b")
else:
    print("a不等于b") #
# <>不等于python3已废弃
if a != b:
    print("a不等于b") #
else:
    print("a等于b")
if a < b:
    print("a小于b")
else:
    print("a大于等于b") #
if a > b:
    print("a大于b") #
else:
    print("a小于等于b")
a = 5
b = 20
if a <= b:
    print("a小于等于b") #
else:
    print("a大于b")
if b >= a:
    print("b大于等于a") #
else:
    print("b小于a")

# 赋值运算符
a = 21
b = 10
c = 0
c += a
print("c += a - c的值为：", c) # 21
c *= a
print("c *= a - c的值为：", c) # 441
c /= a
print("c /= a - c的值为：", c) # 21.0
c = 2
c %= a
print("c %= a - c的值为：", c) # 2
a = 3
c **= a
print("c **= a - c的值为：", c) # 8
c //= a
print("c //= a - c的值为：", c) # 2

# 位运算符
a = int('00111100', 2) # 60
b = int('00001101', 2) # 13
print(a & b) # 12 = 00001100
print(a | b) # 61 = 00111101
print(a ^ b) # 49 = 00110001
print(~a) # -61 = 11000011
print(a << 2) # 240 = 11110000
print(a >> 2) # 15 = 00001111

# 逻辑运算符
a = 10
b = 20
if a and b:
    print("变量a和b都是True") #
else:
    print("变量a和b有一个不为True")
if a or b:
    print("变量a和b都为True，或其中一个变量为True") #
else:
    print("变量a和b都不为True")
a = 0
if a and b:
    print("变量a和b都为True")
else:
    print("变量a和b有一个不为True") #
if a or b:
    print("变量a和b都为True，或其中一个变量为True") #
else:
    print("变量a和b都不为True")
if not (a and b):
   print("变量a和b都为False，或其中一个变量为False") #
else:
   print("变量a和b都为True")

# 成员运算符
a = 10
b = 20
list = [1, 2, 3, 4, 5]
if (a in list):
    print("变量a在给定的列表中list中")
else:
    print("变量a不在给定的列表中list中") #
if (b not in list):
    print("变量b不在给定的列表中list中") #
else:
    print("变量b在给定的列表中list中")
a = 2
if (a in list):
    print("变量a在给定的列表中list中") #
else:
    print("变量a不在给定的列表中list中")

# 身份运算符
# is与==区别：
# is用于判断两个变量引用对象是否为同一个(同一块内存空间)， ==用于判断引用变量的值是否相等
a = 20
b = 20
if (a is b):
    print("a和b有相同的标识") #
else:
    print("a和b没有相同的标识")
if (a is not b):
    print("a和b没有相同的标识")
else:
    print("a和b有相同的标识") #
b = 30
if (a is b):
    print("a和b有相同的标识")
else:
    print("a和b没有相同的标识") #
if (a is not b):
    print("a和b没有相同的标识") #
else:
    print("a和b有相同的标识")