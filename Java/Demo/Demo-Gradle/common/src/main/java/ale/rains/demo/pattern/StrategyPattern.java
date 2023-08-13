package ale.rains.demo.pattern;

/**
 * 策略模式
 * 策略模式是一种行为型设计模式，它允许您定义一系列算法，将每个算法封装起来，并使它们可以互换使用。
 * 这种模式使得算法可以独立于使用它们的客户端而变化。
 *
 * 在Java中，策略模式通常由一个接口和多个实现该接口的类组成。
 * 客户端将使用该接口来调用算法，而不是直接调用实现类。
 * 这样，客户端就可以在运行时选择不同的算法实现，而不需要修改代码。
 */
public class StrategyPattern {
    /*
    // 下面的例子展示了如何使用策略模式来实现一个简单的支付系统。
    // 我们定义了一个接口 PaymentStrategy ，并创建了多个实现该接口的类，
    // 每个类代表一种不同的支付方式。客户端可以根据需要选择使用哪种支付方式。
    interface PaymentStrategy {
        void pay(double amount);
    }
    class CreditCardPayment implements PaymentStrategy {
        private String cardNumber;
        private String cvv;
        private String expiryDate;
        public CreditCardPayment(String cardNumber, String cvv, String expiryDate) {
            this.cardNumber = cardNumber;
            this.cvv = cvv;
            this.expiryDate = expiryDate;
        }
        public void pay(double amount) {
            System.out.println("Paying " + amount + " using credit card.");
        }
    }
    class PayPalPayment implements PaymentStrategy {
        private String email;
        private String password;
        public PayPalPayment(String email, String password) {
            this.email = email;
            this.password = password;
        }
        public void pay(double amount) {
            System.out.println("Paying " + amount + " using PayPal.");
        }
    }
    class CashPayment implements PaymentStrategy {
        public void pay(double amount) {
            System.out.println("Paying " + amount + " using cash.");
        }
    }
    class PaymentProcessor {
        private PaymentStrategy strategy;
        public PaymentProcessor(PaymentStrategy strategy) {
            this.strategy = strategy;
        }
        public void setStrategy(PaymentStrategy strategy) {
            this.strategy = strategy;
        }
        public void processPayment(double amount) {
            strategy.pay(amount);
        }
    }
    public class PaymentSystem {
        public static void main(String[] args) {
            PaymentProcessor processor = new PaymentProcessor(new CreditCardPayment("1234 5678 9012 3456", "123", "12/23"));
            processor.processPayment(100.0);
            processor.setStrategy(new PayPalPayment("example@example.com", "password"));
            processor.processPayment(50.0);
            processor.setStrategy(new CashPayment());
            processor.processPayment(25.0);
        }
    }
    // 在上面的示例中， PaymentStrategy 接口定义了一种支付方式，并包含一个 pay 方法，该方法接受一个金额参数。
    // 我们创建了三个实现该接口的类，分别代表信用卡支付、PayPal支付和现金支付。
    // PaymentProcessor 类接受一个 PaymentStrategy 实例作为参数，并使用它来执行支付操作。
    // 在 main 方法中，我们创建了一个 PaymentProcessor 实例，并使用不同的支付方式来进行支付。
    */
}
