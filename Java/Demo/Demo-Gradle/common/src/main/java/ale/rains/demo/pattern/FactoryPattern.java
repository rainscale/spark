package ale.rains.demo.pattern;

/**
 * 工厂模式
 * 通过一个工厂类来实现对象的创建，而无需直接暴露对象的创建逻辑给客户端。
 * 简单工厂模式的优点在于客户端无需了解具体产品类的创建细节，只需通过工厂类来创建对象，
 * 并且工厂类可以根据客户端的需求来动态创建不同类型的对象。
 * 但是缺点也比较明显，如果需要创建的产品类数量较多，则工厂类的代码会变得很臃肿，不便于维护。
 */
public class FactoryPattern {
    /*
    abstract class Animal {
        public abstract void sound();
    }
    class Cat extends Animal {
        @Override
        public void sound() {
            System.out.println("喵喵喵");
        }
    }
    class Dog extends Animal {
        @Override
        public void sound() {
            System.out.println("汪汪汪");
        }
    }
    class AnimalFactory {
        public static Animal createAnimal(String type) {
            if (type.equalsIgnoreCase("dog")) {
                return new Dog();
            } else if (type.equalsIgnoreCase("cat")) {
                return new Cat();
            } else {
                throw new IllegalArgumentException("Invalid animal type: " + type);
            }
        }
    }
    public static void main(String[] args) {
        // 使用工厂类创建不同的 Animal 对象
        Animal dog = AnimalFactory.createAnimal("dog");
        dog.sound();
        Animal cat = AnimalFactory.createAnimal("cat");
        cat.sound();
    }
    */
}
