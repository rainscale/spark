# !/usr/bin/python3
# coding=utf-8

def change(a):
    "#可写函数说明：python传不可变对象实例,通过id()函数来查看内存地址变化"
    print(id(a))
    a = 10
    print(id(a))


def changeme(mylist):
    "传可变对象实例,可变对象在函数里修改了参数，那么在调用这个函数的函数里，原始的参数也被改变了"
    mylist.append([1, 2, 3, 4])
    print("函数内取值：", mylist)
    return # 不带表达式的return相当于返回None


def printinfo2(name, age = 35):
    "调用函数时，如果没有传递参数，则会使用默认参数"
    print("名字：", name)
    print("年龄：", age)


def printinfo3(arg1, *vartuple):
    "加了星号*的参数会以元组(tuple)的形式导入，存放所有未命名的变量参数"
    print("输出：")
    print(arg1)
    for var in vartuple:
        print(var)
    return


def printinfo4(arg1, **vardict):
    "加了两个星号**的参数会以字典的形式导入"
    print("输出：")
    print(arg1)
    print(vardict)

'''
139184640622832
139184640622832
139184640623120
'''
a = 1
print(id(a))
change(a)

'''
函数内取值： [10, 20, 30, [1, 2, 3, 4]]
函数外取值： [10, 20, 30, [1, 2, 3, 4]]
'''
mylist = [10, 20 ,30]
changeme(mylist)
print("函数外取值：", mylist)

'''
名字： John
年龄： 35
'''
printinfo2("John")

'''
输出：
10
'''
printinfo3(10)

'''
输出：
70
60
50
'''
printinfo3(70, 60, 50)

'''
输出：
1
{'a': 2, 'b': 3}
'''
printinfo4(1, a=2, b=3)

'''
lambda 函数的语法只包含一个语句，如下：
lambda [arg1 [,arg2,.....argn]]:expression
'''
x = lambda a: a + 10
print(x(5)) # 15

'''
我们可以将匿名函数封装在一个函数内，这样可以使用同样的代码来创建多个匿名函数
以下实例将匿名函数封装在myfunc函数中，通过传入不同的参数来创建不同的匿名函数
'''
def myfunc(n):
    return lambda a: a * n

mydoubler = myfunc(2)
mytripler = myfunc(3)

print(mydoubler(11)) # 22
print(mytripler(11)) # 33