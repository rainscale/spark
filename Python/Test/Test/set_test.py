#!/usr/bin/python3
# -*- coding: utf-8 -*-

sites = {'Google', 'Taobao', 'Runoob', 'Facebook', 'Zhihu', 'Baidu'}

print(sites)   # 输出集合，重复的元素被自动去掉

# 成员测试
if 'Runoob' in sites :
    print('Runoob 在集合中')
else :
    print('Runoob 不在集合中')


# set可以进行集合运算
a = set('abracadabra')
b = set('alacazam')

print(a) # {'d', 'c', 'a', 'b', 'r'}
print(b) # {'m', 'a', 'l', 'c', 'z'}
# a 和 b 的差集
print(a - b)     # {'b', 'd', 'r'}
# a 和 b 的并集
print(a | b)     # {'d', 'm', 'a', 'l', 'c', 'z', 'b', 'r'}
# a 和 b 的交集
print(a & b)     # {'a', 'c'}
# a 和 b 中不同时存在的元素
print(a ^ b)     # {'m', 'd', 'z', 'l', 'b', 'r'}