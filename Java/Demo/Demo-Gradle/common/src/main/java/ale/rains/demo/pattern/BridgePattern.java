package ale.rains.demo.pattern;

/**
 * 桥接模式
 * 桥接模式是一种将抽象化和实现化解耦的设计模式。
 * 它使用了封装、聚合以及继承等基本技术，将两个独立变化的维度通过抽象化的方式进行桥接，
 * 从而降低了它们之间的耦合度，使得系统更加灵活。
 */
public class BridgePattern {
    /*
    // 首先，我们定义一个 Color 接口，它表示颜色：
    public interface Color {
        void applyColor();
    }
    // 然后，我们定义一个 Shape 抽象类，它包含了一个 Color 对象：
    public abstract class Shape {
        protected Color color;
        public Shape(Color color) {
            this.color = color;
        }
        public abstract void applyColor();
    }
    // 接下来，我们定义两个实现了 Color 接口的具体类：
    public class Red implements Color {
        @Override
        public void applyColor() {
            System.out.println("Applying red color");
        }
    }
    public class Blue implements Color {
        @Override
        public void applyColor() {
            System.out.println("Applying blue color");
        }
    }
    // 最后，我们定义两个实现了 Shape 抽象类的具体类：
    public class Circle extends Shape {
        public Circle(Color color) {
            super(color);
        }
        @Override
        public void applyColor() {
            System.out.print("Circle applying color: ");
            color.applyColor();
        }
    }
    public class Square extends Shape {
        public Square(Color color) {
            super(color);
        }
        @Override
        public void applyColor() {
            System.out.print("Square applying color: ");
            color.applyColor();
        }
    }
    // 现在，我们可以使用这些类来创建出对应的对象并调用它们的方法：
    public class Test {
        public static void main(String[] args) {
            Color blue = new Blue();
            Shape square = new Square(new Red());
            Shape circle = new Circle(blue);
            square.applyColor();
            circle.applyColor();
        }
    }
    // 输出结果如下：
    // Square applying color: Applying red color
    // Circle applying color: Applying blue color
    // 这是一个简单的桥接模式实现，它允许我们在运行时动态地改变
    // Shape类的颜色而不用影响到 Shape 子类，同时也允许我们增加新的颜色和形状类而无需改变其它现有的类。
    */
}
