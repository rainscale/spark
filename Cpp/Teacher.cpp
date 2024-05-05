#include "Teacher.h"
#include <iostream>
using namespace std;

Teacher::Teacher(char *name, int age, int salary): People(name, age), m_salary(salary) {}

void Teacher::display() {
    cout << m_name << "今年" << m_age << "岁了，是一名教师，每月有" << m_salary << "元的收入。" <<endl;
}
