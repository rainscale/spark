package ale.rains.demo.pattern;

/**
 * 状态模式
 * Java状态模式是一种行为型设计模式，它允许对象在内部状态改变时改变它的行为。
 * 状态模式将状态封装成独立的类，并将请求委托给当前状态对象，从而实现状态的切换和状态行为的变化。
 * 通过使用状态模式，我们可以将状态和行为分离，使得对象的行为可以随着状态的改变而改变，从而实现更加灵活的设计。
 * 使用状态模式的场景包括：
 * 1.当一个对象的行为取决于它的状态，并且它必须在运行时根据状态改变它的行为时。
 * 2.当一个对象需要根据状态改变它的数据和方法时。
 * 3.当一个对象需要在多个状态之间切换，并且每个状态都有不同的行为时。
 * 注意事项：
 * 1.状态模式可以增加类的数量，因此需要在设计时考虑类的数量和复杂性。
 * 2.状态模式需要对状态进行封装，因此需要在设计时考虑状态的可扩展性和可维护性。
 */
public class StatePattern {
    /*
    interface State {
        void handle();
    }
    // 具体状态类1
    class ConcreteState1 implements State {
        public void handle() {
            System.out.println("ConcreteState1 is handling.");
        }
    }
    // 具体状态类2
    class ConcreteState2 implements State {
        public void handle() {
            System.out.println("ConcreteState2 is handling.");
        }
    }
    // 环境类
    class Context {
        private State state;
        public void setState(State state) {
            this.state = state;
        }
        public void request() {
            state.handle();
        }
    }
    // 使用状态模式实现的客户端代码
    public class Main {
        public static void main(String[] args) {
            Context context = new Context();
            State state1 = new ConcreteState1();
            State state2 = new ConcreteState2();
            context.setState(state1);
            context.request();
            context.setState(state2);
            context.request();
        }
    }
    */
}
