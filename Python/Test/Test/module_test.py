# !/usr/bin/python3
# coding=utf-8

import sys
import math

'''
['/home/spark/Works/spark/Python/Test/Test', '/home/spark/Works/spark/Python/Test/Test', '/usr/lib/python310.zip', '/usr/lib/python3.10', '/usr/lib/python3.10/lib-dynload', '/home/spark/Works/spark/Python/Test/Test/.venv/lib/python3.10/site-packages']
'''
print(sys.path)

'''
dir()函数一个排好序的字符串列表，内容是一个模块里定义过的名字。
返回的列表容纳了在一个模块里定义的所有模块，变量和函数
['__doc__', '__loader__', '__name__', '__package__', '__spec__', 'acos', 'acosh', 'asin', 'asinh', 'atan', 'atan2', 'atanh', 'ceil', 'comb', 'copysign', 'cos', 'cosh', 'degrees', 'dist', 'e', 'erf', 'erfc', 'exp', 'expm1', 'fabs', 'factorial', 'floor', 'fmod', 'frexp', 'fsum', 'gamma', 'gcd', 'hypot', 'inf', 'isclose', 'isfinite', 'isinf', 'isnan', 'isqrt', 'lcm', 'ldexp', 'lgamma', 'log', 'log10', 'log1p', 'log2', 'modf', 'nan', 'nextafter', 'perm', 'pi', 'pow', 'prod', 'radians', 'remainder', 'sin', 'sinh', 'sqrt', 'tan', 'tanh', 'tau', 'trunc', 'ulp']
在这里，特殊字符串变量__name__指向模块的名字，__file__指向该模块的导入文件名
'''
content = dir(math)
print(content)

Money = 200
def AddMoney():
    a = 6
    print(locals()) # {'a': 6}
    '''
    根据调用地方的不同，globals()和locals()函数可被用来返回全局和局部命名空间里的名字。
    如果在函数内部调用locals()，返回的是所有能在该函数里访问的命名。
    如果在函数内部调用globals()，返回的是所有在该函数里能访问的全局名字。
    两个函数的返回类型都是字典。所以名字们能用 keys()函数摘取。
    {'__name__': '__main__', '__doc__': None, '__package__': None, '__loader__': <_frozen_importlib_external.SourceFileLoader object at 0x76b1ea31d300>, '__spec__': None, '__annotations__': {}, '__builtins__': <module 'builtins' (built-in)>, '__file__': '/home/spark/Works/spark/Python/Test/Test/module_test.py', '__cached__': None, 'sys': <module 'sys' (built-in)>, 'math': <module 'math' (built-in)>, 'content': ['__doc__', '__loader__', '__name__', '__package__', '__spec__', 'acos', 'acosh', 'asin', 'asinh', 'atan', 'atan2', 'atanh', 'ceil', 'comb', 'copysign', 'cos', 'cosh', 'degrees', 'dist', 'e', 'erf', 'erfc', 'exp', 'expm1', 'fabs', 'factorial', 'floor', 'fmod', 'frexp', 'fsum', 'gamma', 'gcd', 'hypot', 'inf', 'isclose', 'isfinite', 'isinf', 'isnan', 'isqrt', 'lcm', 'ldexp', 'lgamma', 'log', 'log10', 'log1p', 'log2', 'modf', 'nan', 'nextafter', 'perm', 'pi', 'pow', 'prod', 'radians', 'remainder', 'sin', 'sinh', 'sqrt', 'tan', 'tanh', 'tau', 'trunc', 'ulp'], 'Money': 200, 'AddMoney': <function AddMoney at 0x76b1ea38a8c0>}
    '''
    print(globals())
    global Money
    Money = Money + a # 没有上面一句，会报错：UnboundLocalError: local variable 'Money' referenced before assignment

print(Money)
AddMoney()
print(Money)