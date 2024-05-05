#include "People.h"
#include <iostream>
using namespace std;

People::People(char *name, int age): m_name(name), m_age(age){}

void People::display() {
    cout << m_name << "今年" << m_age << "岁了，是个无业游民。" <<endl;
}