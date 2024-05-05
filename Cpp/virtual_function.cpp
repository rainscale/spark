#include "People.h"
#include "Teacher.h"

int main() {
    // C++提供多态的目的是：可以通过基类指针对所有派生类（包括直接派生和间接派生）
    // 的成员变量和成员函数进行“全方位”的访问，尤其是成员函数。如果没有多态，我们只能访问成员变量。
    People *p = new People((char*)"王志刚", 23);
    p->display();
    p = new Teacher((char*)"赵宏佳", 45, 8200);
    p->display();

    // 借助引用也可以实现多态
    People p1((char*)"王志刚", 23);
    Teacher t1((char*)"赵宏佳", 45, 8200);
   
    People &rp = p1;
    People &rt = t1;
   
    rp.display();
    rt.display();
    return 0;
}