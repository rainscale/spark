package ale.rains.demo.pattern;

/**
 * 空对象模式
 * Java空对象模式是一种行为设计模式，它允许我们在不返回null的情况下提供默认行为。
 * 这种模式通常用于需要处理null对象的情况下，以避免NullPointerException异常。
 * 该模式包括两个主要组件：抽象类和具体类。
 */
public class NullObjectPattern {
    /*
    interface User {
        String getName();
        boolean hasAccess();
    }
    // 定义一个具体类，表示一个真实的用户
    class RealUser implements User {
        private String name;
        private boolean hasAccess;
        public RealUser(String name, boolean hasAccess) {
            this.name = name;
            this.hasAccess = hasAccess;
        }
        public String getName() {
            return name;
        }
        public boolean hasAccess() {
            return hasAccess;
        }
    }
    // 定义一个空对象，表示一个空的用户
    class NullUser implements User {
        public String getName() {
            return "Guest";
        }
        public boolean hasAccess() {
            return false;
        }
    }
    // 定义一个工厂类，用于创建用户
    class UserFactory {
        // 根据名称和权限创建一个用户
        public static User getUser(String name, boolean hasAccess) {
            if (name == null) {
                return new NullUser();
            }
            return new RealUser(name, hasAccess);
        }
    }
    public class NullObjectPatternDemo {
        public static void main(String[] args) {
            User user1 = UserFactory.getUser("Alice", true);
            User user2 = UserFactory.getUser(null, false);
            System.out.println("User 1: " + user1.getName() + ", has access: " + user1.hasAccess());
            System.out.println("User 2: " + user2.getName() + ", has access: " + user2.hasAccess());
        }
    }
    // 在上面的代码中，我们定义了一个接口User，它表示一个用户，并包含两个方法：getName()和hasAccess()。
    // 接着，我们定义了一个具体类RealUser，它表示一个真实的用户，并实现了User接口的两个方法。
    // 我们还定义了一个空对象NullUser，它表示一个空的用户，并实现了User接口的两个方法。
    // 最后，我们定义了一个工厂类UserFactory，用于创建用户。
    // 如果传入的名称为null，则返回一个空对象NullUser，否则返回一个真实的用户RealUser。
    // 在main()方法中，我们使用UserFactory创建了两个用户user1和user2，并打印它们的名称和是否有权限。
    // 由于user2的名称为null，因此它将返回一个空对象NullUser，并打印出"Guest"字符串和false布尔值。
    // 通过空对象模式，我们可以在不返回null的情况下提供默认行为，避免NullPointerException异常。
    */
}
