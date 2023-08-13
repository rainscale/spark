package ale.rains.demo.pattern;

/**
 * 抽象工厂模式
 * 通过定义一个创建对象的接口来创建对象，但将具体实现的决定留给子类来决定。
 * 在抽象工厂模式中，接口是负责创建一个相关对象的工厂，不需要显式指定它们的类。
 * 每个生成的工厂都能按照工厂模式提供对象。
 */
public class AbstractFactoryPattern {
    /*
    // 创建一个抽象产品类
    abstract class Animal {
        public abstract void sound();
    }
    // 创建具体产品类，继承自 Animal 类
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
    abstract class AnimalFactory {
        // 定义一个抽象方法，用于创建 Animal 对象
        public abstract Animal createAnimal();
    }
    // 创建具体工厂类，实现创建 Animal 对象的接口
    class CatFactory extends AnimalFactory {
        @Override
        public Animal createAnimal() {
            return new Cat();
        }
    }
    class DogFactory extends AnimalFactory {
        @Override
        public Animal createAnimal() {
            return new Dog();
        }
    }
    // 演示代码
    public static void main(String[] args) {
        // 创建一个 Dog 对象
        AnimalFactory dogFactory = new DogFactory();
        Animal dog = dogFactory.createAnimal();
        dog.sound();

        // 创建一个 Cat 对象
        AnimalFactory catFactory = new CatFactory();
        Animal cat = catFactory.createAnimal();
        cat.sound();
    }
    */
}
