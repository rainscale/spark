#!/usr/bin/python3
# -*- coding: utf-8 -*-

import time
from func_timeout import func_set_timeout, FunctionTimedOut, func_timeout

timeout_value = 5

'''
无参数
'''


@func_set_timeout(timeout_value)
def my_function():
    while True:
        print('time')
        time.sleep(1)


'''
有参数
'''


def task(name: str):
    print(f'Welcom, {name}')
    time.sleep(timeout_value + 1)
    print('No timeout.')


'''
或者使用装饰器
'''


def time_out(fn):
    def wrapper(*args, **kwargs):
        try:
            result = fn(*args, **kwargs)
            return result
        except FunctionTimedOut:
            print('Time out')
            return None

    return wrapper


@time_out
@func_set_timeout(timeout_value)
def a(name):
    time.sleep(timeout_value + 1)
    print(f"Hello, {name}")
    return 'b'


class TestFunction(object):
    def __init__(self):
        pass

    @func_set_timeout(timeout_value)
    def my_test(self):
        time.sleep(timeout_value + 1)
        print('No timeout 2')


try:
    my_function()
except FunctionTimedOut:
    print('程序超时')
except Exception as e:
    print('other exceptions.')
    print(e)

try:
    func_timeout(timeout_value, task, args=('Tom',))
    print("It's a test.")
except FunctionTimedOut:
    print('程序超时')
except Exception as e:
    print('other exceptions.')
    print(e)

c = a('Tom')
print(c)

try:
    x = TestFunction()
    x.my_test()
    print('No timeout 1')
except FunctionTimedOut as e:
    print('Timeout:', e)

'''
time
time
time
time
time
程序超时
Welcom, Tom
程序超时
Time out
None
Timeout: Function my_test (args=(<__main__.TestFunction object at 0x74a51e544a60>,)) (kwargs={}) timed out after 5.000000 seconds.
'''