#pragma once
//基类People
class People {
public:
    People(char *name, int age);
    virtual void display();  // 声明为虚函数，C++中虚函数的唯一用处就是构成多态
protected:
    char *m_name;
    int m_age;
};