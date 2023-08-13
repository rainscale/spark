package ale.rains.demo.pattern;

/**
 * 备忘录模式
 * Java备忘录模式是一种行为设计模式，它允许在不破坏封装性的情况下捕获和恢复对象的内部状态。
 * 备忘录模式通常用于需要撤销操作或恢复先前状态的情况下。
 * 该模式包括三个主要组件：原始对象、备忘录对象和负责管理备忘录对象的对象。
 */
public class MementoPattern {
    /*
    // Originator类表示原始对象，它包含需要保存的状态。
    class Originator {
        private String state;
        public void setState(String state) {
            this.state = state;
        }
        public String getState() {
            return state;
        }
        // createMemento()方法创建备忘录对象，并将当前状态保存到备忘录对象中。
        public Memento createMemento() {
            return new Memento(state);
        }
        // restore()方法用于从备忘录对象中恢复先前的状态。
        public void restore(Memento memento) {
            state = memento.getState();
        }
    }
    // Memento类表示备忘录对象，它包含需要保存的状态。
    class Memento {
        private String state;
        public Memento(String state) {
            this.state = state;
        }
        public String getState() {
            return state;
        }
    }
    // Caretaker类负责管理备忘录对象，它包含一个Memento对象。
    class Caretaker {
        private Memento memento;
        public void setMemento(Memento memento) {
            this.memento = memento;
        }
        public Memento getMemento() {
            return memento;
        }
    }
    public class MementoPatternDemo {
        public static void main(String[] args) {
            Originator originator = new Originator();
            Caretaker caretaker = new Caretaker();
            // 保存原始对象的状态到备忘录对象中
            originator.setState("State 1");
            caretaker.setMemento(originator.createMemento());
            // 修改原始对象的状态
            originator.setState("State 2");
            // 从备忘录对象中恢复先前的状态
            originator.restore(caretaker.getMemento());
            System.out.println("Current state: " + originator.getState());
        }
    }
    // 在上面的代码中，Originator类表示原始对象，它包含需要保存的状态。
    // createMemento()方法创建备忘录对象，并将当前状态保存到备忘录对象中。
    // restore()方法用于从备忘录对象中恢复先前的状态。
    // Memento类表示备忘录对象，它包含需要保存的状态。
    // Caretaker类负责管理备忘录对象，它包含一个Memento对象。
    // 在main()方法中，我们创建了一个Originator对象和一个Caretaker对象，
    // 并调用它们的方法进行状态保存和恢复。通过备忘录模式，我们可以在不破坏封装性的情况下捕获和恢复对象的内部状态。
    */
}
