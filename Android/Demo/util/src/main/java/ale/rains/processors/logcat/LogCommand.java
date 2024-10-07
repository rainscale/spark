package ale.rains.processors.logcat;

import ale.rains.processors.base.Analyser;
import ale.rains.processors.base.Command;

public class LogCommand extends Command {
    public LogCommand(Analyser analyser) {
        super(analyser);
    }
    public LogCommand(Analyser analyser, Object... objects) {
        super(analyser, objects);
    }
}