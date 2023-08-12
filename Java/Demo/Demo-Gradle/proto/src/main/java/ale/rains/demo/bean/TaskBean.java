package ale.rains.demo.bean;

public class TaskBean {
    private int taskId;
    private Action action;
    private Result result;
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public enum Action {
        START("start"),
        PAUSE("pause"),
        RESUME("resume"),
        STOP("stop");

        private String code;

        Action(String code) {
            this.code = code;
        }

        String getCode() {
            return code;
        }
    }

    public static class Result {
        private String tag;
        private String failInfo;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getFailInfo() {
            return failInfo;
        }

        public void setFailInfo(String failInfo) {
            this.failInfo = failInfo;
        }

        public String toString() {
            return "{tag:" + tag + " failInfo:" + failInfo + "}";
        }
    }
}