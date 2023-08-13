package ale.rains.demo.pattern;

/**
 * 适配器模式
 * Java适配器模式是一种结构型设计模式，它允许不兼容的接口之间进行通信。
 * 适配器模式通过将一个类的接口转换为客户端所期望的另一个接口来实现这一点。
 * 这种模式可以在不修改现有代码的情况下重用现有类。
 * 适配器模式可以帮助我们在不修改现有代码的情况下重用现有类，并且可以使不兼容的接口之间进行通信。
 */
public class AdapterPattern {
    /*
    // 源接口
    public class Adaptee {
        public void specificRequest() {
            System.out.println("Adaptee's specific request");
        }
    }
    public interface Target {
        public void request();
    }
    // 适配器类
    public class Adapter implements Target {
        private Adaptee adaptee;
        public Adapter(Adaptee adaptee) {
            this.adaptee = adaptee;
        }
        public void request() {
            adaptee.specificRequest();
        }
    }
    // 演示代码
    public static void main(String[] args) {
        Adaptee adaptee = new Adaptee();
        Target target = new Adapter(adaptee);
        target.request();
    }
    */
}