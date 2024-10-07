package ale.rains.processors.base;

public abstract class Command {
    private Analyser analyser;
    private Object[] params;

    public Command(Analyser analyser) {
        this(analyser, new Object[]{null});
    }

    public Command(Analyser analyser, Object... objects) {
        this.analyser = analyser;
        this.params = objects;
    }

    public void setParam(Object... objects) {
        this.params = objects;
    }

    protected Object[] getParam() {
        return params;
    }

    public void setAnalyser(Analyser analyser) {
        this.analyser = analyser;
    }

    public Analyser getAnalyser() {
        return analyser;
    }

    protected void execute() {
        analyser.internalOperation(getParam());
    }
}