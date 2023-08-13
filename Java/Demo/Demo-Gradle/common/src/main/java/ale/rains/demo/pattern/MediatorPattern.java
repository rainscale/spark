package ale.rains.demo.pattern;

/**
 * 中介者模式
 * Java中介者模式是一种行为设计模式，它允许对象之间通过中介者对象进行通信，从而避免对象之间的直接耦合。
 * 中介者模式将对象之间的复杂关系转化为中介者和对象之间的简单关系，从而提高了系统的灵活性和可维护性。
 * 在Java中介者模式中，中介者对象负责协调对象之间的通信，它通常包含一些公共方法，用于处理对象之间的交互。
 * 对象之间的通信通过中介者对象进行，从而避免了对象之间的直接耦合。
 */
public class MediatorPattern {
    /*
    // Mediator接口定义了send()方法，用于处理对象之间的交互。
    interface Mediator {
        void send(String message, Colleague colleague);
    }
    // Colleague抽象类表示对象，它包含一个Mediator对象，用于处理对象之间的通信。
    abstract class Colleague {
        protected Mediator mediator;
        public Colleague(Mediator mediator) {
            this.mediator = mediator;
        }
        public abstract void receive(String message);
        public abstract void send(String message);
    }
    // ConcreteColleague1和ConcreteColleague2是具体的对象实现类，它们实现了Colleague抽象类中的方法。
    class ConcreteColleague1 extends Colleague {
        public ConcreteColleague1(Mediator mediator) {
            super(mediator);
        }
        @Override
        public void receive(String message) {
            System.out.println("Colleague1 received message: " + message);
        }
        @Override
        public void send(String message) {
            System.out.println("Colleague1 sends message: " + message);
            mediator.send(message, this);
        }
    }
    class ConcreteColleague2 extends Colleague {
        public ConcreteColleague2(Mediator mediator) {
            super(mediator);
        }
        @Override
        public void receive(String message) {
            System.out.println("Colleague2 received message: " + message);
        }
        @Override
        public void send(String message) {
            System.out.println("Colleague2 sends message: " + message);
            mediator.send(message, this);
        }
    }
    // ConcreteMediator是具体的中介者实现类，它负责协调对象之间的通信。
    class ConcreteMediator implements Mediator {
        private ConcreteColleague1 colleague1;
        private ConcreteColleague2 colleague2;
        public void setColleague1(ConcreteColleague1 colleague1) {
            this.colleague1 = colleague1;
        }
        public void setColleague2(ConcreteColleague2 colleague2) {
            this.colleague2 = colleague2;
        }
        @Override
        public void send(String message, Colleague colleague) {
            if (colleague == colleague1) {
                colleague2.receive(message);
            } else {
                colleague1.receive(message);
            }
        }
    }
    public class MediatorPatternDemo {
        public static void main(String[] args) {
            ConcreteMediator mediator = new ConcreteMediator();
            ConcreteColleague1 colleague1 = new ConcreteColleague1(mediator);
            ConcreteColleague2 colleague2 = new ConcreteColleague2(mediator);
            mediator.setColleague1(colleague1);
            mediator.setColleague2(colleague2);
            colleague1.send("Hello, Colleague2.");
            colleague2.send("Hello, Colleague1.");
        }
    }
    // 在上面的代码中，Mediator接口定义了send()方法，用于处理对象之间的交互。
    // Colleague抽象类表示对象，它包含一个Mediator对象，用于处理对象之间的通信。
    // ConcreteColleague1和ConcreteColleague2是具体的对象实现类，它们实现了Colleague抽象类中的方法。
    // ConcreteMediator是具体的中介者实现类，它负责协调对象之间的通信。
    // 在main()方法中，我们创建了一个ConcreteMediator对象和两个ConcreteColleague对象，
    // 并调用它们的send()方法进行通信。通过中介者对象进行通信，避免了对象之间的直接耦合。
    */
}
