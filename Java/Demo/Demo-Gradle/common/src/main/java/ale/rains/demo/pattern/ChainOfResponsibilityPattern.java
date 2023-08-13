package ale.rains.demo.pattern;

/**
 * 责任链模式
 * 责任链模式是一种行为设计模式，它允许将请求沿着处理链进行传递，直到有一个处理程序处理它为止。
 * 在Java中实现责任链模式通常需要以下几个步骤：
 * 1.定义一个处理器接口，该接口包含一个处理请求的方法。
 * 2.创建一个抽象处理器类，它实现处理器接口，并包含一个指向下一个处理器的引用。
 * 3.创建具体的处理器类，它们继承自抽象处理器类，并实现处理请求的方法。
 * 4.在客户端代码中，创建处理器链，并将请求发送到链的第一个处理器。
 */
public class ChainOfResponsibilityPattern {
    /*
    public interface Handler {
        Handler setNextHandler(Handler nextHandler);
        void handleRequest(Request request);
    }
    // 创建抽象处理器类
    public abstract class AbstractHandler implements Handler {
        private Handler nextHandler;
        public Handler setNextHandler(Handler nextHandler) {
            this.nextHandler = nextHandler;
            return this.nextHandler;
        }
        public Handler getNextHandler() {
            return nextHandler;
        }
    }
    // 创建具体的处理器类
    public class ConcreteHandler1 extends AbstractHandler {
        public void handleRequest(Request request) {
            if (request.getType().equals("Type1")) {
                System.out.println("ConcreteHandler1 handles request " + request);
            } else {
                getNextHandler().handleRequest(request);
            }
        }
    }
    public class ConcreteHandler2 extends AbstractHandler {
        public void handleRequest(Request request) {
            if (request.getType().equals("Type2")) {
                System.out.println("ConcreteHandler2 handles request " + request);
            } else {
                getNextHandler().handleRequest(request);
            }
        }
    }
    public class ConcreteHandler3 extends AbstractHandler {
        public void handleRequest(Request request) {
            if (request.getType().equals("Type3")) {
                System.out.println("ConcreteHandler3 handles request " + request);
            } else {
                getNextHandler().handleRequest(request);
            }
        }
    }
    // 创建请求类
    public class Request {
        private String type;
        public Request(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }
        public String toString() {
            return "Request [type=" + type + "]";
        }
    }
    // 客户端代码
    public class Client {
        public static void main(String[] args) {
            Handler handler1 = new ConcreteHandler1();
            Handler handler2 = new ConcreteHandler2();
            Handler handler3 = new ConcreteHandler3();
            handler1.setNextHandler(handler2)
                    .setNextHandler(handler3);
            handler1.handleRequest(new Request("Type1"));
            handler1.handleRequest(new Request("Type2"));
            handler1.handleRequest(new Request("Type3"));
        }
    }
    // 输入结果：
    // ConcreteHandler1 handles request Request [type=Type1]
    // ConcreteHandler2 handles request Request [type=Type2]
    // ConcreteHandler3 handles request Request [type=Type3]
    // 以上代码演示了如何创建一个处理器链，并将请求沿着链传递，直到有一个处理程序处理它为止。
    // 在这个例子中，ConcreteHandler1、ConcreteHandler2和ConcreteHandler3都是具体的处理器类，
    // 它们继承自AbstractHandler类，并实现handleRequest方法。
    // 客户端代码创建了一个处理器链，并将请求发送到链的第一个处理器。
    // 当请求到达处理器时，它会检查请求类型是否与处理器可以处理的类型匹配。
    // 如果是，处理器将处理请求。否则，它将请求传递给链中的下一个处理器，直到有一个处理程序处理它为止。
    */
}
