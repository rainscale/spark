package ale.rains.demo.pattern;

/**
 * 访问者模式
 * Java访问者模式是一种行为型设计模式，它允许你在不修改对象结构的前提下定义新的操作。
 * 访问者模式将对象结构和操作分离开来，使得操作可以独立地应用于对象结构中的元素。
 * 在访问者模式中，有两个主要角色：访问者和元素。
 * 访问者定义了对元素进行操作的方法，而元素则提供了接受访问者的方法。
 *
 * 访问者模式适用于以下场景：
 * 1.对象结构比较稳定，但经常需要在此对象结构上定义新的操作。
 * 2.需要对一个对象结构中的对象进行很多不同的且不相关的操作，而不希望这些操作“污染”这些对象的类。
 * 3.对象结构中对象的类很少改变，但经常需要在这些对象上定义新的操作。
 * 4.需要在运行时根据对象的类型执行不同的操作。
 * 5.需要对对象结构中的所有对象进行某种类型的处理，但是对象所采取的处理方式根据对象类型不同而异。
 * 常见的使用访问者模式的场景包括：编译器的语法分析、XML文档解析、静态分析器、模型验证器、模型转换器等。
 */
public class VisitorPattern {
    /*
    // 下面是一个访问者模式的示例代码，其中我们将实现一个简单的计算器，它可以对表达式进行计算。
    // 我们将使用访问者模式来遍历表达式树，并对每个节点执行相应的操作。
    interface Expression {
        void accept(Visitor visitor);
    }
    class NumberExpression implements Expression {
        private int value;
        public NumberExpression(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }
    // 定义具体的元素类：加法表达式
    class AdditionExpression implements Expression {
        private Expression left;
        private Expression right;
        public AdditionExpression(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }
        public Expression getLeft() {
            return left;
        }
        public Expression getRight() {
            return right;
        }
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }
    interface Visitor {
        void visit(NumberExpression expression);
        void visit(AdditionExpression expression);
    }
    // 定义具体的访问者类：打印访问者
    class PrinterVisitor implements Visitor {
        public void visit(NumberExpression expression) {
            System.out.print(expression.getValue());
        }
        public void visit(AdditionExpression expression) {
            System.out.print("(");
            expression.getLeft().accept(this);
            System.out.print("+");
            expression.getRight().accept(this);
            System.out.print(")");
        }
    }
    class CalculatorVisitor implements Visitor {
        private int result;
        public void visit(NumberExpression expression) {
            result = expression.getValue();
        }
        public void visit(AdditionExpression expression) {
            expression.getLeft().accept(this);
            int leftValue = result;
            expression.getRight().accept(this);
            int rightValue = result;
            result = leftValue + rightValue;
        }
        public int getResult() {
            return result;
        }
    }
    public class Client {
        public static void main(String[] args) {
            // 构建表达式树：1 + (2 + 3)
            Expression expression = new AdditionExpression(
                    new NumberExpression(1),
                    new AdditionExpression(
                            new NumberExpression(2),
                            new NumberExpression(3)
                    )
            );
            // 计算表达式的值
            CalculatorVisitor calculator = new CalculatorVisitor();
            expression.accept(calculator);
            System.out.println("Result: " + calculator.getResult());
            // 打印表达式的字符串表示
            PrinterVisitor printer = new PrinterVisitor();
            expression.accept(printer);
        }
    }
    // 输出结果：
    // Result: 6
    // (1+(2+3))
    // 上面的例子中，我们定了一个表达式接口Expression，并提供了两个表达式实现类数字表达式NumberExpression
    // 和加法表达式AdditionExpression，定义了一个访问者接口Visitor，
    // 以及两个具体访问者CalculatorVisitor、PrinterVisitor，两个访问者接收表达式对象，
    // 并在访问者中对表达式实现具体操作，分别是表达式运算以及表达式打印。
    // 上面例子没有改变具体的表达式类，且定义了新的操作。
    */
}
