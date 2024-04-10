rem 这是我的第一个bat脚本
:: 这是我的第一个bat脚本
@echo off
title TITLE指令修改控制台窗口的标题
:: 查看color的颜色表
color /?
color 0A
:start
set /a var+=1
echo 打印变量的值：%var%
if %var% leq 4 goto start

echo.
:: Word.Document.8文件扩展名
echo .doc文件扩展名，对应的文件类型：& ftype Word.Document.8

::在当前工作目录下创建一个临时文件
echo "这是一个临时文件" > temp.txt
call :sub1 temp.txt
call :sub2 1 2 3 4 5 6 7 8
pause
:: %1指的是temp.txt这个参数，%0指的是这个脚本本身，%2、%3以此类推
:sub1
echo "脚本本身参数扩展"
echo 脚本工作路径：%~dp0
echo.
echo "temp.txt参数扩展"
echo 删除引号：%~1
echo 扩展到路径：%~f1
echo 扩展到一个驱动器号：%~d1
echo 扩展到一个路径：%~p1
echo 扩展到一个文件名：%~n1
echo 扩展到一个文件扩展名：%~x1
echo 扩展的路径只包含短名：%~s1
echo 扩展到文件属性：%~a1
echo 扩展到文件的日期/时间：%~t1
echo 扩展到文件的大小：%~z1
echo 扩展到驱动器号和路径：%~dp1
echo 扩展到文件名和文件扩展名：%~nx1
echo 扩展到类似与DIR的输出行：%~ftza1
goto :eof


:sub2
echo 第二位参数的值：%~2
rem 向左移动一位
shift /1
echo 第二位参数的值：%~2
goto :eof