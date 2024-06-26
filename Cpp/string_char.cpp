#include <cstring>
#include <iostream>
#include <vector>
using namespace std;

int main(int argc, char **argv)
{
    string str = "Hello World!";
    printf("%s\n", str.c_str());
    cout << str << endl;
    // 使用string的data方法
    char* p1 = str.data();
    // const char* p1 = str.data();
    cout << p1 << endl;
    // 使用string的c_str方法
    // char* p2 = str.c_str(); // error: invalid conversion from ‘const char*’ to ‘char*’ [-fpermissive]
    // char* p2 = (char*)str.c_str(); // 编译正常
    char* p2 = const_cast<char*>(str.c_str());
    cout << p2 << endl;
    // 使用string的copy方法
    char* p3;
    int len = str.length();
    p3 = (char*)malloc((len+1) * sizeof(char));
    str.copy(p3, len, 0);
    *(p3 + len) = '\0';
    cout << p3 << endl;
    // 使用vector方法
    vector<char> v1(str.begin(), str.end());
    v1.push_back('\0');
    char* p4 = &v1[0];
    cout << p4 << endl;
    // 使用string的连续存储功能
    char* p5 = &*str.begin();
    cout << p5 << endl;
    
    // char*转string
    const char* s = "Hello World!";
    string str1 = s;
    cout << str1 << endl;
    
    // char[]转string
    char a[] = "Hello World!";
    string str2 = a;
    cout << str2 << endl;
    
    // string转char[]
    char a1[10];
    const char* cstr = str.c_str();
    strcpy(a1, cstr);
    cout << a1 << endl;
    
    // string转char[]
    char a2[10];
    int i;
    for (i = 0; i < str.length(); i++) {
        a2[i] = str[i];
    }
    a2[i] = '\0';
    cout << a2 << endl;
        
    return 0;
}
