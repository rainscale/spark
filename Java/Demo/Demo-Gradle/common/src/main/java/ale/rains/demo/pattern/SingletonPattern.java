package ale.rains.demo.pattern;

/**
 * 单例模式
 */
public class SingletonPattern {
    /**
     * 恶汉模式
     * 这种方式比较常用，但容易产生垃圾对象
     * 优点：没有加锁，执行效率会提高。
     * 缺点：类加载时就初始化，浪费内存。
     */
   /*
    private static SingletonPattern instance = new SingletonPattern();
    private SingletonPattern() {}
    public static SingletonPattern getInstance() {
        return instance;
    }
    */

    /**
     * 懒汉模式
     * 在懒汉式基础上加入双重检验锁，保证线程安全和性能
     */
    /*
    private volatile static SingletonPattern instance = null;
    private SingletonPattern() {}
    public static SingletonPattern getInstance() {
        if (instance == null) {
            synchronized (SingletonPattern.class) {
                if (instance == null) {
                    instance = new SingletonPattern();
                }
            }
        }
        return instance;
    }
    */

    /**
     * 静态内部类
     * 使用静态内部类来实现懒汉式单例模式，保证线程安全和性能。
     * 这种方式能达到双检锁方式一样的功效，但实现更简单
     */
    /*
    private SingletonPattern() {}
    private static class SingletonHolder {
        private static final SingletonPattern INSTANCE = new SingletonPattern();
    }
    public static SingletonPattern getInstance() {
        return SingletonHolder.INSTANCE;
    }
    */

    /**
     * 枚举
     * 使用枚举来实现单例模式，保证线程安全和防止反射攻击
     * 它也是《Effective Java》作者极力推荐地单例实现方式，
     * 因为枚举的实现方式不仅是线程安全的，而且只会装载一次，无论是序列化、反序列化、反射还是克隆都不会新创建对象
     */
    private enum SingletonEnum {
        INSTANCE;
        private final SingletonPattern instance;
        SingletonEnum() {
            instance = new SingletonPattern();
        }
        private SingletonPattern getInstance() {
            return instance;
        }
    }
    private SingletonPattern() {}
    public static SingletonPattern getInstance() {
        return SingletonEnum.INSTANCE.getInstance();
    }
}
