package ale.rains.processors.base;

import ale.rains.util.Logger;

public class Invoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void invoke() {
        if (command == null) {
            try {
                throw new Exception("the command is null, you must set command.");
            } catch (Exception e) {
                Logger.e(e);
            }
        } else {
            command.execute();
        }
    }
}