#!/bin/bash

:<<'COMMENT'
这是注释的部分。
可以有多行内容。
COMMENT

: '
这是注释的部分。
可以有多行内容。
'

echo "-- \$* 演示 ---"
for i in "$*"; do
    echo $i # 1 2 3 相当于一个参数
done

echo "-- \$@ 演示 ---"
for i in "$@"; do
    echo $i # 1  相当于三个独立参数
            # 2
            # 3
done

value="baidu.com"
echo $value
echo ${value}

for skill in Ada Coffe Action Java; do
    # echo "I am good at $skillScript" # 解释器就会把$skillScript当成一个变量（其值为空）
    echo "I am good at ${skill}Script" # 建议变量加上花括号
done

url="https://www.google.com"
readonly url
# url="https://www.baidu.com" 只读变量不能被修改
myUrl="http://www.runoob.com"
echo $myUrl
unset myUrl # 变量被删除后不能再次使用。unset命令不能删除只读变量
echo $myUrl # 没有任何输出

array=(value0 value1 value2 value3 value4 value5 value6)
echo ${array[5]} # value5
# 获取数组中的所有元素
echo ${array[@]} # value0 value1 value2 value3 value4 value5 value6
echo ${array[*]} # value0 value1 value2 value3 value4 value5 value6
# 获取数组的长度
echo ${#array[@]} # 7
echo ${#array[*]} # 7
# 取得数组单个元素的长度
echo ${#array[1]} # 6
for i in ${array[*]}; do
    echo $i
done

name="John"
# 获取字符串长度
echo ${#name} # 4
str='Hello, $name'
echo $str # Hello, $name
str="Hello, $name"
echo $str # Hello, John
str="Hello, I know you are \"$name\"!\n"
echo -e $str # Hello, I know you are "John"!
             #
# 使用双引号拼接
greeting="hello, "$name"!"
greeting_1="hello, ${name}!"
echo $greeting  $greeting_1 # hello, John! hello, John!


# 使用单引号拼接
greeting_2='hello, '$name'!'
greeting_3='hello, ${name}!'
echo $greeting_2  $greeting_3 # hello, John! hello, ${name}!

# 提取子字符串
# 以下实例从字符串第2个字符开始截取4个字符：
str="google is a great site"
echo ${str:1:4} # 输出 oogl

# 查找子字符串
# 查找字符i或o的位置(哪个字母先出现就计算哪个)：
str="google is a great site"
echo `expr index "$str" io` # 2
