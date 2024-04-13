#!/usr/bin/python3
# -*- coding: utf-8 -*-

sites = ["Baidu", "Google", "Runoob", "Taobao"]
for site in sites:
    print(site)
'''
Baidu
Google
Runoob
Taobao
'''

word = 'runoob'
for letter in word:
    print(letter)
'''
r
u
n
o
o
b
'''

#  1 到 5 的所有数字：
for number in range(1, 6):
    print(number)
'''
1
2
3
4
5
'''

for x in range(6):
  print(x)
else:
  print("Finally finished!")
'''
0
1
2
3
4
5
Finally finished!
'''

sites = ["Baidu", "Google","Runoob","Taobao"]
for site in sites:
    if site == "Runoob":
        print("菜鸟教程!")
        break
    print("循环数据 " + site)
else:
    print("没有循环数据!")
print("完成循环!")
'''
循环数据 Baidu
循环数据 Google
菜鸟教程!
完成循环!
'''

a = ['Google', 'Baidu', 'Runoob', 'Taobao', 'QQ']
for i in range(len(a)):
    print(i, a[i])
'''
0 Google
1 Baidu
2 Runoob
3 Taobao
4 QQ
'''