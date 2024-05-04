# !/usr/bin/python3
# coding=utf-8
import time

'''
exec执行储存在字符串或文件中的Python语句，相比于eval，exec可以执行更复杂的Python代码。
需要说明的是在Python2中exec不是函数，而是一个内置语句(statement)，但是Python2中有一个execfile()函数。
可以理解为Python3把exec这个statement和execfile()函数的功能够整合到一个新的exec()函数中去了。
语法
exec obj
参数
obj -- 要执行的表达式。
返回值
exec 返回值永远为 None。
'''
exec('print("Hello World")') # Hello World

'''
next()返回迭代器的下一个项目。
next()函数要和生成迭代器的iter()函数一起使用。
语法
next(iterable[, default])
参数说明：
iterable -- 可迭代对象
default -- 可选，用于设置在没有下一个元素时返回该默认值，如果不设置，又没有下一个元素则会触发StopIteration异常。
返回值
返回下一个项目。
'''
# 首先获得Iterator对象:
it = iter([1, 2, 3, 4, 5])
# 循环:
while True:
    try:
        # 获得下一个值:
        x = next(it)
        print(x)
    except StopIteration:
        # 遇到StopIteration就退出循环
        break

'''
help() 函数用于查看函数或模块用途的详细说明。
语法
help([object])
参数说明：
object -- 对象；
返回值
返回对象帮助信息。
'''
help('time') # 查看time模块的帮助
help('str') # 查看str数据类型的帮助
a = [1,2,3]
help(a) # 查看列表list帮助信息
help(a.append) # 显示list的append方法的帮助

'''
__import__()函数用于动态加载类和函数
如果一个模块经常变化就可以使用__import__()来动态载入。
语法
__import__(name[, globals[, locals[, fromlist[, level]]]])
参数说明：
name -- 模块名
返回值
返回元组列表。
'''
__import__('hello')

print(complex(1, 2)) # (1+2j)

'''
hash() 用于获取取一个对象（字符串或者数值等）的哈希值。
语法
hash(object)
参数说明：
object -- 对象；
返回值
返回对象的哈希值。
'''
print(hash('test')) # 5088208224642682444
print(hash(1)) # 1
print(hash(str([1,2,3]))) # 集合 7666464346782421378
print(hash(str(sorted({'1':1})))) # 字典 7809971829909532792

'''
set()函数创建一个无序不重复元素集，可进行关系测试，删除重复数据，还可以计算交集、差集、并集等。
语法
class set([iterable])
参数说明：
iterable -- 可迭代对象对象；
返回值
返回新的集合对象。
'''
print(set('google')) # {'l', 'o', 'e', 'g'}

'''
ound() 方法返回浮点数x的四舍五入值
'''
print("round(80.23456, 2) = ", round(80.23456, 2)) # round(80.23456, 2) =  80.23
print("round(100.000056, 3) = ", round(100.000056, 3)) # round(100.000056, 3) =  100.0
print("round(-100.000056, 3) = ", round(-100.000056, 3)) # round(-100.000056, 3) :  -100.0

'''
memoryview()函数返回给定参数的内存查看对象(memory view)。
所谓内存查看对象，是指对支持缓冲区协议的数据进行包装，在不需要复制对象基础上允许Python代码访问
'''
v = memoryview(bytearray("abcefg", 'utf-8'))
print(v[1]) # 98
print(v[-1]) # 103
print(v[1:4]) # <memory at 0x7062ff29e5c0>
print(v[1:4].tobytes()) # b'bce'

'''
hex()函数用于将10进制整数转换成16进制，以字符串形式表示。
语法
hex(x)
参数说明：
x -- 10进制整数
返回值
返回16进制数，以字符串形式表示。
'''
print(hex(255)) # 0xff
print(type(hex(12))) # <class 'str'>

'''
object()函数返回一个空对象，我们不能向该对象添加新的属性或方法。
object()函数返回的对象是所有类的基类，它没有任何属性和方法，只有Python内置对象所共有的一些特殊属性和方法，
例如 __doc__ 、__class__、__delattr__、__getattribute__等。
object()是Python中最基本的对象，其他所有对象都是由它派生出来的。
因此，object()对象是所有Python类的最顶层的超类（或者称为基类或父类），
所有的内置类型、用户定义的类以及任何其他类型都直接或间接地继承自它。
语法
object()
参数说明：
无。
返回值
返回一个空对象。
'''
o = object()
'''
dir()函数不带参数时，返回当前范围内的变量、方法和定义的类型列表；
带参数时，返回参数的属性、方法列表。如果参数包含方法__dir__()，该方法将被调用。
如果参数不包含__dir__()，该方法将最大限度地收集参数信息。
语法
dir([object])
参数说明：
object -- 对象、变量、类型。
返回值
返回模块的属性列表。
'''
print(dir(o))

print(oct(10)) # 0o12

'''
sorted() 函数对所有可迭代的对象进行排序操作。
sort与sorted区别：
sort是应用在list上的方法，sorted可以对所有可迭代的对象进行排序操作。
list的sort方法返回的是对已经存在的列表进行操作，无返回值，而内建函数sorted方法返回的是一个新的list，
而不是在原来的基础上进行的操作。
语法
sorted(iterable, cmp=None, key=None, reverse=False)
参数说明：
iterable -- 可迭代对象。
cmp -- 比较的函数，这个具有两个参数，参数的值都是从可迭代对象中取出，此函数必须遵守的规则为，大于则返回1，小于则返回-1，等于则返回0。
key -- 主要是用来进行比较的元素，只有一个参数，具体的函数的参数就是取自于可迭代对象中，指定可迭代对象中的一个元素来进行排序。
reverse -- 排序规则，reverse = True 降序 ， reverse = False 升序（默认）。
返回值
返回重新排序的列表。
'''
m = [5, 7, 6, 3, 4, 1, 2]
n = sorted(m)
print(m) # [5, 7, 6, 3, 4, 1, 2]
print(n) # [1, 2, 3, 4, 5, 6, 7]
L = [('b', 2), ('a', 1), ('c', 3), ('d', 4)]
help('sorted')
print(sorted(L, key = lambda x : x[1])) # [('a', 1), ('b', 2), ('c', 3), ('d', 4)] 利用key
students = [('john', 'A', 15), ('jane', 'B', 12), ('dave', 'B', 10)]
print(sorted(students, key = lambda s: s[2])) # [('dave', 'B', 10), ('jane', 'B', 12), ('john', 'A', 15)] 按年龄排序
print(sorted(students, key = lambda s: s[2], reverse = True)) # [('john', 'A', 15), ('jane', 'B', 12), ('dave', 'B', 10)] # 按降序

myslice = slice(5) # 设置截取5个元素的切片
print(myslice) # slice(None, 5, None)
arr = range(10)
print(list(arr)) # [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
print(list(arr[myslice])) # [0, 1, 2, 3, 4] 截取5个元素

'''
dict() 函数用于创建一个字典。
语法
class dict(**kwarg)
class dict(mapping, **kwarg)
class dict(iterable, **kwarg)
参数说明：
**kwargs -- 关键字。
mapping -- 元素的容器，映射类型（Mapping Types）是一种关联式的容器类型，它存储了对象与对象之间的映射关系。
iterable -- 可迭代对象。
返回值
返回一个字典。
'''
print(dict()) # {} 创建空字典
empty = dict() # empty = {}
print('empty =', empty)
print(type(empty)) # <class 'dict'>
print(dict(a='a', b='b', t='t'))  # {'a': 'a', 'b': 'b', 't': 't'} 传入关键字
print(dict(zip(['one', 'two', 'three'], [1, 2, 3])))  # {'one': 1, 'two': 2, 'three': 3} 映射函数方式来构造字典
print(dict([('one', 1), ('two', 2), ('three', 3)]))    # {'one': 1, 'two': 2, 'three': 3} 可迭代对象方式来构造字典

'''
hasattr()函数用于判断对象是否包含对应的属性。
'''
class Coordinate:
    x = 10
    y = -5
    z = 0


point1 = Coordinate()
print(hasattr(point1, 'x')) # True
print(hasattr(point1, 'y')) # True
print(hasattr(point1, 'z')) # True
print(hasattr(point1, 'no'))  # 没有该属性 False
print(getattr(point1, 'x')) # 10
setattr(point1, 'w', 100)
print(point1.w) # 100 如果属性不存在会创建一个新的对象属性，并对属性赋值
delattr(Coordinate, 'z')
print('--删除 z 属性后--')
print('x =', point1.x) # x = 10
print('y =', point1.y) # y = -5
# 触发错误
print('z = ', point1.z) # AttributeError: 'Coordinate' object has no attribute 'z'

'''
compile() 函数将一个字符串编译为字节代码。
语法
compile(source, filename, mode[, flags[, dont_inherit]])
参数
source -- 字符串或者AST（Abstract Syntax Trees）对象。。
filename -- 代码文件名称，如果不是从文件读取代码则传递一些可辨认的值。
mode -- 指定编译代码的种类。可以指定为 exec, eval, single。
flags -- 变量作用域，局部命名空间，如果被提供，可以是任何映射对象。。
flags和dont_inherit是用来控制编译源码时的标志
返回值
返回表达式执行结果。
'''
str2 = "for i in range(0,10): print(i)"
cd = compile(str2,'','exec')   # 编译为字节代码对象
print(cd) # <code object <module> at 0x7c5a8b82a130, file "", line 1>
exec(cd)

str3 = "3 * 4 + 5"
a3 = compile(str3,'','eval')
print(a3) # <code object <module> at 0x7bf16772e1e0, file "", line 1>
print(eval(a3)) # 17

aa = [1,2,3]
bb = [4,5,6]
cc = [4,5,6,7,8]
zipped = zip(aa, bb) # 打包为元组的列表
print(list(zipped)) # [(1, 4), (2, 5), (3, 6)]
print(list(zip(aa, cc))) # [(1, 4), (2, 5), (3, 6)]
aa1, bb1 = zip(*zip(aa, bb)) # 与zip相反，zip(*) 可理解为解压，返回二维矩阵式
print(aa1) # (1, 2, 3)
print(bb1) # (4, 5, 6)

aList = [123, 'xyz', 'zara', 'abc', 'xyz']
aList.reverse()
print(aList) # ['xyz', 'abc', 'zara', 'xyz', 123]

print(globals()) # globals函数返回一个全局变量的字典，包括所有导入的变量。

'''
vars()函数返回对象object的属性和属性值的字典对象
'''
print(vars())

'''
classmethod修饰符对应的函数不需要实例化，不需要self参数，但第一个参数需要是表示自身类的cls参数，
可以来调用类的属性，类的方法，实例化对象等。
'''
class A1(object):
    bar = 1

    def func1(self):
        print('foo')

    @classmethod
    def func2(cls):
        print('func2')
        print(cls.bar)
        cls().func1()  # 调用 foo 方法


A1.func2()  # 不需要实例化

'''
getattr()函数用于返回一个对象属性值。
语法
getattr(object, name[, default])
参数
object -- 对象。
name -- 字符串，对象属性。
default -- 默认返回值，如果不提供该参数，在没有对应属性时，将触发AttributeError。
返回值
返回对象属性值。
'''
a1= A1()
print(getattr(a1, 'bar')) # 1

def square(x):
    return x ** 2
print(list(map(square, [1, 2, 3, 4, 5]))) # [1, 4, 9, 16, 25]
print(list(map(lambda x: x ** 2, [1, 2, 3, 4, 5])))  # 使用lambda匿名函数 [1, 4, 9, 16, 25]

'''
repr()方法可以将读取到的格式字符，比如换行符、制表符，转化为其相应的转义字符。
'''
s="物品\t单价\t数量\n包子\t1\t2"
print(s)
print(repr(s))

'''
frozenset() 返回一个冻结的集合，冻结后集合不能再添加或删除任何元素
'''
a1 = frozenset(range(10))     # 生成一个新的不可变集合
print(a1) # frozenset({0, 1, 2, 3, 4, 5, 6, 7, 8, 9})
b1 = frozenset('runoob') # frozenset({'n', 'b', 'u', 'r', 'o'})
print(b1)

'''
chr()用一个范围在range（256）内的（就是0～255）整数作参数，返回一个对应的字符
'''
print(chr(0x30), chr(0x31), chr(0x61)) # 0 1 a
print(chr(48), chr(49), chr(97) ) # 0 1 a

'''
callable() 函数用于检查一个对象是否是可调用的。如果返回True，object仍然可能调用失败；但如果返回False，调用对象object绝对不会成功。
对于函数、方法、lambda 函式、 类以及实现了__call__方法的类实例, 它都返回True。
语法
callable(object)
参数
object -- 对象
返回值
可调用返回 True，否则返回 False。
'''
print(callable(0)) # False
print(callable('baidu')) # False
def add(a, b):
    return a + b
print(callable(add)) # True

print("{1} {0} {1}".format("hello", "world")) # world hello world
print("网站名：{name}, 地址 {url}".format(name="菜鸟教程", url="www.runoob.com")) # 网站名：菜鸟教程, 地址 www.runoob.com
# 通过字典设置参数
site = {"name": "菜鸟教程", "url": "www.runoob.com"}
print("网站名：{name}, 地址 {url}".format(**site)) # 网站名：菜鸟教程, 地址 www.runoob.com
# 通过列表索引设置参数
my_list = ['菜鸟教程', 'www.runoob.com']
print("网站名：{0[0]}, 地址 {0[1]}".format(my_list))  # "0" 是必须的 网站名：菜鸟教程, 地址 www.runoob.com
print("{} 对应的位置是 {{0}}".format("runoob")) # runoob 对应的位置是 {0}

aTuple = (123, 'baidu', 'google', 'abc');
aList = list(aTuple)
print(aList) # [123, 'baidu', 'google', 'abc']

print(float('123')) # 123.0

print(bytearray()) # bytearray(b'')
print(bytearray([1, 2, 3])) # bytearray(b'\x01\x02\x03')
print(bytearray('google', 'utf-8')) # bytearray(b'google')

print(bool()) # False
print(bool(0)) # False
print(bool(1)) # True
print(bool(2)) # True
print(issubclass(bool, int)) # True

str1 = "gooooogle"
print(len(str1)) # 9
l = [1, 2, 3, 4, 5]
print(len(l)) # 5

'''
python2.x range()函数可创建一个整数列表，一般用在for循环中。
注意：Python3 range()返回的是一个可迭代对象（类型是对象），而不是列表类型， 所以打印的时候不会打印列表，具体可查阅 Python3 range()用法说明。
函数语法
range(start, stop[, step])
参数说明：
start: 计数从start开始。默认是从0开始。例如range(5)等价于range(0， 5);
stop: 计数到stop结束，但不包括stop。例如：range(0， 5)是[0, 1, 2, 3, 4]没有5
step：步长，默认为1。例如：range(0， 5)等价于range(0, 5, 1)
'''
'''
filter()函数用于过滤序列，过滤掉不符合条件的元素，返回由符合条件元素组成的新列表。
该接收两个参数，第一个为函数，第二个为序列，序列的每个元素作为参数传递给函数进行判断，
然后返回True或False，最后将返回True的元素放到新列表中。
注意: Python2.7 返回列表，Python3.x 返回迭代器对象，具体内容可以查看：Python3 filter() 函数
语法
以下是 filter() 方法的语法:
filter(function, iterable)
参数
function -- 判断函数。
iterable -- 可迭代对象。
'''
def is_odd(n):
    print(locals()) # locals()函数会以字典类型返回当前位置的全部局部变量
    return n % 2 == 1


newlst = filter(is_odd, range(1, 10))
print(list(newlst)) # [1, 3, 5, 7, 9]

print(tuple([1, 2, 3, 4]))  # (1, 2, 3, 4)
print(tuple({1: 2, 3: 4}))  # (1, 3)
print(tuple((1, 2, 3, 4)))  # (1, 2, 3, 4)

'''
iter()函数用来生成迭代器。
语法
以下是iter()方法的语法:
iter(object[, sentinel])
参数
object -- 支持迭代的集合对象。
sentinel -- 如果传递了第二个参数，则参数object必须是一个可调用的对象（如，函数），此时，iter创建了一个迭代器对象，每次调用这个迭代器对象的__next__()方法时，都会调用object。
返回值
迭代器对象。
'''
lst = [1, 2, 3]
for i in iter(lst):
    print(i)


class X(object):
    def __init__(self):
        self._x = None

    def getx(self):
        print('getx ', self._x)
        return self._x

    def setx(self, value):
        print('setx ', value)
        self._x = value

    def delx(self):
        print('delx')
        del self._x

    x = property(getx, setx, delx, "I'm the 'x' property.")


x = X()
x.x = 8
print(x.x)
del x.x


class Y(object):
    def __init__(self):
        self._x = None

    @property
    def x(self):
        """I'm the 'x' property."""
        print('getter ', self._x)
        return self._x

    @x.setter
    def x(self, value):
        print('setter ', value)
        self._x = value

    @x.deleter
    def x(self):
        print('deleter')
        del self._x


y = Y()
y.x = 8
print(y.x)
del y.x


class P(object):
    @staticmethod
    def f():
        print("Hello Python!")


P.f()  # Hello Python!
pobj = P()
pobj.f()  # Hello Python!

'''
all() 函数用于判断给定的可迭代参数 iterable 中的所有元素是否都为 TRUE，如果是返回 True，否则返回 False。
元素除了是 0、空、None、False 外都算 True。
函数等价于：
def all(iterable):
    for element in iterable:
        if not element:
            return False
    return True
'''
print(all(['a', 'b', 'c', 'd']))  # True
print(all(['a', 'b', '', 'd']))  # False 列表中存在一个空的元素
print(all([0, 1, 2, 3]))  # False 列表中存在一个为0的元素
print(all(('a', 'b', 'c', 'd')))  # True
print(all(('a', 'b', '', 'd')))  # False 元组中存在一个空的元素
print(all((0, 1, 2, 3)))  # False 元组中存在一个为0的元素
print(all([]))  # True 空列表
print(all(()))  # True 空元组

'''
any()函数用于判断给定的可迭代参数iterable是否全部为False，则返回False，如果有一个为True，则返回True。
元素除了是 0、空、FALSE外都算 TRUE。
函数等价于：
def any(iterable):
    for element in iterable:
        if element:
            return True
    return False
'''
print(any(['a', 'b', 'c', 'd']))  # True
print(any(['a', 'b', '', 'd']))  # True
print(any([0, '', False]))  # False
print(any(('a', 'b', 'c', 'd')))  # True
print(any(('a', 'b', '', 'd')))  # True
print(any((0, '', False)))  # False
print(any([]))  # False
print(any(()))  # False

'''
enumerate() 函数用于将一个可遍历的数据对象(如列表、元组或字符串)组合为一个索引序列，
同时列出数据和数据下标，一般用在for循环当中。
'''
seasons = ['Spring', 'Summer', 'Fail', 'Winter']
print(list(enumerate(seasons)))  # [(0, 'Spring'), (1, 'Summer'), (2, 'Fail'), (3, 'Winter')]
print(list(enumerate(seasons, start=1)))  # [(1, 'Spring'), (2, 'Summer'), (3, 'Fail'), (4, 'Winter')]
i = 0
seq = ['one', 'two', 'three']
for element in seq:
    print(i, seq[i])
    i += 1
for i, element in enumerate(seq):
    print(i, element)

print(int())  # 0
print(int(3))  # 3
print(int(3.6))  # 3
print(int('12', 16))  # 18
print(int('0xa', 16))  # 10
print(int('10', 8))  # 8

'''
eval()函数用来执行一个字符串表达式，并返回表达式的值。
字符串表达式可以包含变量、函数调用、运算符和其他Python语法元素。
语法
以下是 eval() 方法的语法:
eval(expression[, globals[, locals]])
参数
expression -- 表达式。
globals -- 变量作用域，全局命名空间，如果被提供，则必须是一个字典对象。
locals -- 变量作用域，局部命名空间，如果被提供，可以是任何映射对象。
eval()函数将字符串expression解析为Python表达式，并在指定的命名空间中执行它。
返回值
eval()函数将字符串转换为相应的对象，并返回表达式的结果。
'''
# 执行简单的数学表达式
result = eval("2 + 3 * 4")
print(result)  # 14

# 执行变量引用
x = 10
result = eval("x + 5")
print(result)  # 15

# 在指定命名空间中执行表达式
namespace = {'a': 2, 'b': 3}
result = eval("a + b", namespace)
print(result)  # 5

'''
isinstance()函数来判断一个对象是否是一个已知的类型，类似type()。
isinstance()与type()区别：
type()不会认为子类是一种父类类型，不考虑继承关系。
isinstance()会认为子类是一种父类类型，考虑继承关系。
如果要判断两个类型是否相同推荐使用 isinstance()。
语法
以下是 isinstance()方法的语法:
isinstance(object, classinfo)
参数
object -- 实例对象。
classinfo -- 可以是直接或间接类名、基本类型或者由它们组成的元组。
返回值
如果对象的类型与参数二的类型（classinfo）相同则返回True，否则返回False。
'''
a = 2
print(isinstance(a, int))  # True
print(isinstance(a, str))  # False
print(isinstance(a, (str, int, list)))  # True 是元组中的一个返回True


class A:
    pass


class B(A):
    pass


print(isinstance(A(), A))  # True
print(type(A()) == A)  # True
print(isinstance(B(), A))  # True
print(type(B()) == A)  # False
print(issubclass(B, A))  # True

'''
print()方法用于打印输出，最常见的一个函数。
在 Python3.3 版增加了flush关键字参数。
print 在Python3.x是一个函数，但在Python2.x版本不是一个函数，只是一个关键字。
语法
以下是 print()方法的语法:
print(*objects, sep=' ', end='\n', file=sys.stdout, flush=False)
参数
objects -- 复数，表示可以一次输出多个对象。输出多个对象时，需要用,分隔。
sep -- 用来间隔多个对象，默认值是一个空格。
end -- 用来设定以什么结尾。默认值是换行符\n，我们可以换成其他字符串。
file -- 要写入的文件对象。
flush -- 输出是否被缓存通常决定于file，但如果flush关键字参数为True，流会被强制刷新。
返回值
无。
'''
print("www", "baidu", "com", sep=".")  # 设置间隔符 www.baidu.com
print("Loading", end="")
for i in range(20):
    print(".", end='', flush=True)
    time.sleep(0.5)
print('\n')

'''
super()函数是用于调用父类(超类)的一个方法。
super()是用来解决多重继承问题的，直接用类名调用父类方法在使用单继承的时候没问题，
但是如果使用多继承，会涉及到查找顺序（MRO）、重复调用（钻石继承）等种种问题。
MRO 就是类的方法解析顺序表, 其实也就是继承父类方法时的顺序表。
语法
以下是super()方法的语法:
super(type[, object-or-type])
参数
type -- 类。
object-or-type -- 类，一般是self
Python3.x和Python2.x的一个区别是: Python3可以使用直接使用super().xxx代替super(Class, self).xxx
'''


class C:
    def add(self, x):
        y = x + 1
        print(y)


class D(C):
    def add(self, x):
        super().add(x)


d = D()
d.add(2)  # 3

# bin()返回一个整数int或者长整数long int的二进制表示
print(bin(10))  # 0b1010

'''
ord()函数是chr()函数（对于8位的ASCII字符串）或unichr()函数（对于Unicode对象）的配对函数，
它以一个字符（长度为1的字符串）作为参数，返回对应的ASCII数值，或者Unicode数值，
如果所给的Unicode字符超出了你的Python定义范围，则会引发一个TypeError的异常。
'''
print(ord('a'))  # 97
print(ord('b'))  # 98

'''
str()函数将对象转化为适于人阅读的形式
'''
dict1 = {'google': 'google.com', 'baidu': 'baidu.com'}
print(str(dict1))  # {'google': 'google.com', 'baidu': 'baidu.com'}

'''
绝对值
'''
print("abs(-45) = ", abs(-45))  # abs(-45) =  45
print("abs(119) = ", abs(119))  # abs(119) =  119

'''
divmod() 函数把除数和余数运算结果结合起来，返回一个包含商和余数的元组(a//b, a%b)
'''
print("divmod(7, 2) = ", divmod(7, 2))  # divmod(7, 2) =  (3, 1)
print("divmod(8, 2) = ", divmod(8, 2))  # divmod(8, 2) =  (4, 0)

a = input("input:")
print(type(a))  # <class 'str'>

f = open('test.txt')
for line in f:
    print(line)
f.close()
