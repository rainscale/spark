/*
$ clang++ -c const_cast.cpp -v
Ubuntu clang version 14.0.0-1ubuntu1.1
Target: x86_64-pc-linux-gnu
Thread model: posix
InstalledDir: /usr/bin
Found candidate GCC installation: /usr/bin/../lib/gcc/x86_64-linux-gnu/11
Found candidate GCC installation: /usr/bin/../lib/gcc/x86_64-linux-gnu/12
Found candidate GCC installation: /usr/bin/../lib/gcc/x86_64-linux-gnu/9
Selected GCC installation: /usr/bin/../lib/gcc/x86_64-linux-gnu/12
Candidate multilib: .;@m64
Selected multilib: .;@m64
 (in-process)
 "/usr/lib/llvm-14/bin/clang" -cc1 -triple x86_64-pc-linux-gnu -emit-obj -mrelax-all --mrelax-relocations -disable-free -clear-ast-before-backend -disable-llvm-verifier -discard-value-names -main-file-name const_cast.cpp -mrelocation-model pic -pic-level 2 -pic-is-pie -mframe-pointer=all -fmath-errno -ffp-contract=on -fno-rounding-math -mconstructor-aliases -funwind-tables=2 -target-cpu x86-64 -tune-cpu generic -mllvm -treat-scalable-fixed-error-as-warning -debugger-tuning=gdb -v -fcoverage-compilation-dir=/home/spark/Works/spark/Cpp -resource-dir /usr/lib/llvm-14/lib/clang/14.0.0 -internal-isystem /usr/bin/../lib/gcc/x86_64-linux-gnu/12/../../../../include/c++/12 -internal-isystem /usr/bin/../lib/gcc/x86_64-linux-gnu/12/../../../../include/x86_64-linux-gnu/c++/12 -internal-isystem /usr/bin/../lib/gcc/x86_64-linux-gnu/12/../../../../include/c++/12/backward -internal-isystem /usr/lib/llvm-14/lib/clang/14.0.0/include -internal-isystem /usr/local/include -internal-isystem /usr/bin/../lib/gcc/x86_64-linux-gnu/12/../../../../x86_64-linux-gnu/include -internal-externc-isystem /usr/include/x86_64-linux-gnu -internal-externc-isystem /include -internal-externc-isystem /usr/include -fdeprecated-macro -fdebug-compilation-dir=/home/spark/Works/spark/Cpp -ferror-limit 19 -fgnuc-version=4.2.1 -fcxx-exceptions -fexceptions -fcolor-diagnostics -faddrsig -D__GCC_HAVE_DWARF2_CFI_ASM=1 -o const_cast.o -x c++ const_cast.cpp
clang -cc1 version 14.0.0 based upon LLVM 14.0.0 default target x86_64-pc-linux-gnu
ignoring nonexistent directory "/usr/bin/../lib/gcc/x86_64-linux-gnu/12/../../../../x86_64-linux-gnu/include"
ignoring nonexistent directory "/include"
#include "..." search starts here:
#include <...> search starts here:
 /usr/bin/../lib/gcc/x86_64-linux-gnu/12/../../../../include/c++/12
 /usr/bin/../lib/gcc/x86_64-linux-gnu/12/../../../../include/x86_64-linux-gnu/c++/12
 /usr/bin/../lib/gcc/x86_64-linux-gnu/12/../../../../include/c++/12/backward
 /usr/lib/llvm-14/lib/clang/14.0.0/include
 /usr/local/include
 /usr/include/x86_64-linux-gnu
 /usr/include
End of search list.

$ clang++ const_cast.cpp -o const_cast
$ ./const_cast
const_cast进行强制类型转换可以突破 C/C++的常数限制，修改常数的值。
*/
#include <iostream>

using namespace std;

int main()
{
    const int n = 100;
    int *p = const_cast<int*>(&n);
    *p = 123;
    cout << "&n=" << &n << ", n=" << n << endl; // &n=0x7ffde80874a8, n=100 编译期生成100
    cout << "p=" << p << ", *p=" << *p << endl; // p=0x7ffde80874a8, *p=123
    int& ref = const_cast<int&>(n);
    ref = 456;
    cout << "&n=" << &n << ", n=" << n << endl; // &n=0x7ffde80874a8, n=100
    cout << "p=" << p << ", *p=" << *p << endl; // p=0x7ffde80874a8, *p=456
    cout << "&ref=" << &ref << ", ref=" << ref <<endl; // &ref=0x7ffde80874a8, ref=456
    return 0;
}
