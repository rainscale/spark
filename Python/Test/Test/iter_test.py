#!/usr/bin/python3
# -*- coding: utf-8 -*-
import sys

# 把一个类作为一个迭代器使用需要在类中实现两个方法 __iter__() 与 __next__()
class MyNumbers:
    def __iter__(self):
        self.a = 1
        return self

    def __next__(self):
        if self.a <= 20:
            x = self.a
            self.a += 1
            return x
        else:
            raise StopIteration  # 通过 StopIteration 异常标识迭代的完成


"""
在Python中，使用了yield的函数被称为生成器（generator）。
yield是一个关键字，用于定义生成器函数，生成器函数是一种特殊的函数，可以在迭代过程中逐步产生值，而不是一次性返回所有结果。
跟普通函数不同的是，生成器是一个返回迭代器的函数，只能用于迭代操作，更简单点理解生成器就是一个迭代器。
当在生成器函数中使用 yield 语句时，函数的执行将会暂停，并将 yield 后面的表达式作为当前迭代的值返回。
然后，每次调用生成器的next()方法或使用for循环进行迭代时，函数会从上次暂停的地方继续执行，
直到再次遇到yield语句。这样，生成器函数可以逐步产生值，而不需要一次性计算并返回所有结果。
调用一个生成器函数，返回的是一个迭代器对象。
"""


def countdown(n):
    while n > 0:
        yield n
        n -= 1


def fibonacci(n):  # 生成器函数 - 斐波那契
    a, b, counter = 0, 1, 0
    while True:
        if counter > n:
            return
        yield a
        a, b = b, a + b
        counter += 1


myclass = MyNumbers()
myiter = iter(myclass)

for x in myiter:
    print(x)

# 创建生成器对象
generator = countdown(5)

# 通过迭代生成器获取值
print(next(generator))  # 5
print(next(generator))  # 4
print(next(generator))  # 3

# 使用 for 循环迭代生成器
for value in generator:
    print(value)  # 输出: 2 1

f = fibonacci(10)  # f 是一个迭代器，由生成器返回生成

while True:
    try:
        print(next(f), end=" ") # 0 1 1 2 3 5 8 13 21 34 55 
    except StopIteration:
        sys.exit()
