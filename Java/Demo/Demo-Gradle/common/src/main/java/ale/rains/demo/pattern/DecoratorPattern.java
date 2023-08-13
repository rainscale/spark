package ale.rains.demo.pattern;

/**
 * 装饰器模式
 * 装饰器模式是一种结构性设计模式，它允许您在不影响同一类的其他对象的行为的情况下，静态或动态地向单个对象添加行为。
 * 当您想要在运行时添加或删除对象的功能时，或者当您想要减少创建不同行为组合所需的子类数量时，此模式非常有用。
 * 在Java中，使用继承和组合的结合来实现装饰器模式。
 * 具体来说，您需要创建一个基类或接口来定义对象的核心行为，然后创建一个或多个装饰器类来向对象添加附加行为。
 * 每个装饰器类都具有对其装饰的对象的引用，并且它可以在委托给对象的原始行为之前或之后修改对象的行为。
 * <p>
 * 装饰器模式适用于以下场景：
 * 1.在不修改现有代码的情况下，向现有类添加新的功能。
 * 2.在运行时动态地向对象添加新的行为。
 * 3.以不同的方式组合对象，以实现不同的行为。
 * <p>
 * 使用装饰器模式时需要注意以下几点：
 * 1.装饰器类需要实现与被装饰对象相同的接口，以便可以对被装饰对象进行包装。
 * 2.装饰器类应该在调用被装饰对象的方法之前或之后添加新的行为。
 * 3.不要创建过多的装饰器对象，否则会导致代码变得复杂难以维护。
 */
public class DecoratorPattern {
    /*
    public interface Pizza {
        public String getDescription();
        public double getCost();
    }
    // 具体组件
    public class PlainPizza implements Pizza {
        public String getDescription() {
            return "薄饼";
        }
        public double getCost() {
            return 4.00;
        }
    }
    // 装饰器
    public abstract class ToppingDecorator implements Pizza {
        protected Pizza pizza;
        public ToppingDecorator(Pizza pizza) {
            this.pizza = pizza;
        }
        public String getDescription() {
            return pizza.getDescription();
        }
        public double getCost() {
            return pizza.getCost();
        }
    }
    // 我们创建了两个具体的装饰器类 Cheese 和 Pepperoni ，它们向 Pizza 对象添加附加行为。
    // 每个装饰器类修改 getDescription() 和 getCost() 方法以在委托给原始对象之前或之后添加自己的行为。
    // 具体装饰器
    public class Cheese extends ToppingDecorator {
        public Cheese(Pizza pizza) {
            super(pizza);
        }
        public String getDescription() {
            return pizza.getDescription() + "，马苏里拉奶酪";
        }
        public double getCost() {
            return pizza.getCost() + 0.50;
        }
    }
    // 具体装饰器
    public class Pepperoni extends ToppingDecorator {
        public Pepperoni(Pizza pizza) {
            super(pizza);
        }
        public String getDescription() {
            return pizza.getDescription() + "，意大利辣香肠";
        }
        public double getCost() {
            return pizza.getCost() + 1.00;
        }
    }
    // 演示代码
    // 我们创建一个 PlainPizza 对象，然后用 Cheese 对象和 Pepperoni 对象装饰它。
    // 然后打印出Pizza 的描述和成本以验证装饰器是否修改了原始对象的行为。
    public static void main(String[] args) {
        Pizza pizza = new PlainPizza();
        pizza = new Cheese(pizza);
        pizza = new Pepperoni(pizza);
        System.out.println(pizza.getDescription());
        System.out.println("成本：$" + pizza.getCost());
    }
    */
}