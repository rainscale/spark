package ale.rains.demo.pattern;

/**
 * 命令模式
 * 命令模式是一种行为设计模式，它允许将请求封装为一个对象，
 * 从而使不同的请求可以使用不同的参数进行参数化，队列或日志请求，
 * 以及支持可撤销的操作。在Java中，命令模式的实现通常涉及一个命令接口和一个或多个具体命令类，
 * 这些类实现了命令接口，并在其execute()方法中定义了实际的命令逻辑。
 * 此外，命令模式还可能涉及一个调用者类，该类将一个或多个命令对象与接收者对象关联起来，
 * 并在需要时调用它们的execute()方法。命令模式是一种非常灵活和可扩展的模式，
 * 可以用于许多不同的应用程序场景。
 */
public class CommandPattern {
    /*
    interface Command {
        void execute(String[] args);
    }
    // 定义具体命令
    class CreateFileCommand implements Command {
        public void execute(String[] args) {
            // 根据给定的名称和内容创建文件的代码
            System.out.println("创建文件: " + String.join(", ", args));
        }
    }
    class DeleteFileCommand implements Command {
        public void execute(String[] args) {
            // 根据给定的名称删除文件的代码
            System.out.println("删除文件: "+String.join(",",args) );
        }
    }
    // 定义命令执行者
    class CommandExecutor {
        private Map<String, Command> commands = new HashMap<>();
        public CommandExecutor() {
            // 将具体命令与命令名称关联起来
            commands.put("create", new CreateFileCommand());
            commands.put("delete", new DeleteFileCommand());
        }
        public void executeCommand(String commandName, String[] args) {
            // 查找对应的命令并执行
            Command command = commands.get(commandName);
            if (command != null) {
                command.execute(args);
            } else {
                System.out.println("Unknown command: " + commandName);
            }
        }
    }
    // 使用命令执行者执行命令
    public class Main {
        public static void main(String[] args) {
            CommandExecutor executor = new CommandExecutor();
            executor.executeCommand("create", new String[]{"file.txt", "Hello World!"});
            executor.executeCommand("delete", new String[]{"file.txt"});
            executor.executeCommand("unknown", new String[]{});
        }
    }
    */
}
