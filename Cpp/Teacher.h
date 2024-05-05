#pragma once
#include "People.h"

//派生类Teacher
class Teacher : public People{
public:
    Teacher(char *name, int age, int salary);
    virtual void display();  // 声明为虚函数，C++中虚函数的唯一用处就是构成多态
private:
    int m_salary;
};