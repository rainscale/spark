package ale.rains.demo.pattern;

/**
 * 模版模式
 * Java模板模式是一种行为设计模式，它定义了一个操作中的程序骨架，将一些步骤延迟到子类中实现。
 * 这使得子类可以在不改变程序结构的情况下重新定义程序中的某些步骤。
 * 在Java模板模式中，有两种类型的方法：抽象方法和具体方法。
 * 抽象方法由子类实现，而具体方法由父类实现。
 * 模板方法是由具体方法和抽象方法组成的，它定义了程序的骨架，而具体方法则实现了算法的部分步骤。
 *
 * Java模板模式适用于以下场景：
 * 1.当需要定义一组算法，并且这些算法的结构相似，但是实现细节不同时，可以使用模板模式。
 * 2.当需要控制算法的流程，并且在算法的不同阶段需要不同的行为时，可以使用模板模式。
 * 3.当需要在不影响算法整体结构的情况下，对算法的某些步骤进行定制化时，可以使用模板模式。
 * 4.当需要在多个类中使用相同的算法时，可以使用模板模式，避免代码重复。
 * 总之，Java模板模式适用于那些需要在程序中定义骨架，并且在子类中实现具体步骤的情况。
 * 它可以提高代码的复用性和可维护性，同时也可以使代码更加灵活和可扩展。
 */
public class TemplatePattern {
    /*
    abstract class Game {
        abstract void initialize();
        abstract void startPlay();
        abstract void endPlay();
        // 模板方法
        public final void play() {
            // 初始化游戏
            initialize();
            // 开始游戏
            startPlay();
            // 结束游戏
            endPlay();
        }
    }
    class Cricket extends Game {
        @Override
        void endPlay() {
            System.out.println("Cricket Game Finished!");
        }
        @Override
        void initialize() {
            System.out.println("Cricket Game Initialized! Start playing.");
        }
        @Override
        void startPlay() {
            System.out.println("Cricket Game Started. Enjoy the game!");
        }
    }
    class Football extends Game {
        @Override
        void endPlay() {
            System.out.println("Football Game Finished!");
        }
        @Override
        void initialize() {
            System.out.println("Football Game Initialized! Start playing.");
        }
        @Override
        void startPlay() {
            System.out.println("Football Game Started. Enjoy the game!");
        }
    }
    public class TemplatePatternDemo {
        public static void main(String[] args) {
            Game game = new Cricket();
            game.play();
            System.out.println();
            game = new Football();
            game.play();
        }
    }
    */
}
