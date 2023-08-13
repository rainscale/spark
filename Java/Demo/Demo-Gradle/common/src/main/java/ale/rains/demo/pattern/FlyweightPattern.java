package ale.rains.demo.pattern;

/**
 * 享元模式/轻量级模式
 * 享元模式（Flyweight Pattern）是一种结构型设计模式，它通过共享对象来减少内存使用和对象创建的开销。
 * 享元模式属于对象型模式，它通过创建一个享元工厂来管理共享对象，并在需要时返回已经存在的对象，从而减少对象的创建和销毁次数。
 */
public class FlyweightPattern {
    /*
    interface Shape {
        void draw();
    }
    // 具体享元类
    class Circle implements Shape {
        private String color;
        private int x;
        private int y;
        private int radius;
        public Circle(String color) {
            this.color = color;
        }
        public void setX(int x) {
            this.x = x;
        }
        public void setY(int y) {
            this.y = y;
        }
        public void setRadius(int radius) {
            this.radius = radius;
        }
        @Override
        public void draw() {
            System.out.println("画了一个" + color + "的圆，半径为" + radius + "，位置为(" + x + "," + y + ")");
        }
    }
    // 享元工厂类
    // 创建了一个享元工厂类 ShapeFactory ，它维护了一个 Map ，用于存储已经创建的圆对象。
    // 在客户端需要绘制圆时，我们可以通过 ShapeFactory 获取已经存在的圆对象，如果不存在，则创建一个新的圆对象，并将其存储在 Map 中。
    class ShapeFactory {
        private static final Map<String, Shape> circleMap = new HashMap<>();
        public static Shape getCircle(String color) {
            Circle circle = (Circle) circleMap.get(color);
            if (circle == null) {
                circle = new Circle(color);
                circleMap.put(color, circle);
                System.out.println("创建了一个" + color + "的圆");
            }
            return circle;
        }
    }
    // 客户端代码
    public class FlyweightPatternDemo {
        private static final String[] colors = { "红色", "绿色", "蓝色", "黄色", "黑色" };
        public static void main(String[] args) {
            for (int i = 0; i < 20; i++) {
                Circle circle = (Circle) ShapeFactory.getCircle(getRandomColor());
                circle.setX(getRandomX());
                circle.setY(getRandomY());
                circle.setRadius(100);
                circle.draw();
            }
        }
        private static String getRandomColor() {
            return colors[(int) (Math.random() * colors.length)];
        }
        private static int getRandomX() {
            return (int) (Math.random() * 100);
        }
        private static int getRandomY() {
            return (int) (Math.random() * 100);
        }
    }
    */
}
