#!/usr/bin/env python
# -*- coding: utf-8 -*-

a = 20
b = 5.5
c = True
d = 4 + 3j
print(type(a), type(b), type(c), type(d))  # <class 'int'> <class 'float'> <class 'bool'> <class 'complex'>
print(isinstance(a, int))  # True


class A:
    pass


class B(A):
    pass


"""
isinstance 和type的区别在于：
type()不会认为子类是一种父类类型。
isinstance()会认为子类是一种父类类型。
"""
print(isinstance(A(), A)) # True
print(type(A()) == A) # True
print(isinstance(B(), A)) # True
print(type(B()) == A) # False

# bool是int的子类
print(issubclass(bool, int)) # True
print(True==1) # True
print(False==0) # True
print(True+1) # 2
print(False+1) # 1


