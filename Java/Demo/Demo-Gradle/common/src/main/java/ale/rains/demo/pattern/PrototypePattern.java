package ale.rains.demo.pattern;

//import java.util.HashMap;
//import java.util.Map;

/**
 * 原型模式
 * 原型设计模式允许通过复制现有对象来创建新对象，而不是通过实例化类来创建新对象。
 * 在需要创建大量相似对象时非常有用，它可以避免重复创建对象，从而提高性能，并且可以根据需要实现浅拷贝或深拷贝。
 * 在Java中，原型模式的实现通常涉及到实现Cloneable接口和重写clone()方法。
 */
public class PrototypePattern {
    /**
     * 在下面的代码中，Shape是一个抽象类，它实现了Cloneable接口并重写了clone()方法。
     * Circle和Square是Shape的具体子类，它们实现了draw()方法。
     * ShapeCache类是一个缓存，它存储了Shape对象的副本。
     * main()演示它使用ShapeCache来获取Shape对象的副本。
     *
     * 在loadCache()方法中，我们创建了两个Shape对象的副本，并将它们存储在shapeMap中。
     * 在main()方法中，我们使用getShape()方法来获取Shape对象的副本，并输出它们的类型。
     * 由于我们使用了原型模式，所以我们可以通过复制现有对象来创建新对象，而无需实例化类。
     */
    /*
    public abstract class Shape implements Cloneable {
        private String id;
        protected String type;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public abstract void draw();
        @Override
        public Object clone() {
            Object clone = null;
            try {
                clone = super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return clone;
        }
    }

    public class Circle extends Shape {
        public Circle() {
            type = "Circle";
        }
        @Override
        public void draw() {
            System.out.println("Inside Circle::draw() method.");
        }
    }
    public class Square extends Shape {
        public Square() {
            type = "Square";
        }
        @Override
        public void draw() {
            System.out.println("Inside Square::draw() method.");
        }
    }
    public class ShapeCache {
        private static Map<String, Shape> shapeMap = new HashMap<>();
        public static Shape getShape(String shapeId) {
            Shape cachedShape = shapeMap.get(shapeId);
            return (Shape) cachedShape.clone();
        }
        // For each shape run database query and create shape
        // shapeMap.put(shapeKey, shape);
        // for example, we are adding three shapes
        public static void loadCache() {
            Circle circle = new Circle();
            circle.setId("1");
            shapeMap.put(circle.getId(), circle);
            Square square = new Square();
            square.setId("2");
            shapeMap.put(square.getId(), square);
        }
    }
    public static void main(String[] args) {
        ShapeCache.loadCache();
        Shape clonedShape = ShapeCache.getShape("1");
        System.out.println("Shape : " + clonedShape.getType());
        Shape clonedShape2 = ShapeCache.getShape("2");
        System.out.println("Shape : " + clonedShape2.getType());
    }
    */
}
