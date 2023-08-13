package ale.rains.demo.pattern;

/**
 * 外观模式
 * 外观模式是一种结构型设计模式，它提供了一个简单的接口来访问复杂系统中的子系统，从而隐藏了子系统的复杂性。
 * 外观模式属于对象型模式，它通过创建一个外观类，将客户端与子系统解耦，使得客户端只需要与外观类交互即可完成操作。
 */
public class FacadePattern {
    /*
    class CPU {
        public void processData() {
            System.out.println("正在处理数据...");
        }
    }
    class Memory {
        public void load() {
            System.out.println("正在加载内存...");
        }
    }
    class HardDrive {
        public void readData() {
            System.out.println("正在读取硬盘数据...");
        }
    }
    // 外观类
    class ComputerFacade {
        private CPU cpu;
        private Memory memory;
        private HardDrive hardDrive;
        public ComputerFacade() {
            cpu = new CPU();
            memory = new Memory();
            hardDrive = new HardDrive();
        }
        public void start() {
            System.out.println("启动计算机...");
            cpu.processData();
            memory.load();
            hardDrive.readData();
            System.out.println("计算机启动完毕！");
        }
    }
    // 演示代码
    public static void main(String[] args) {
        ComputerFacade computer = new ComputerFacade();
        computer.start();
    }
    */
}
