package ale.rains.demo;

import ale.rains.demo.bean.TaskBean;
import ale.rains.demo.proto.TaskProto;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    //private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
    public static void main(String[] args) {
        // protobuf对象设置参数
        TaskProto.Task task = TaskProto.Task.newBuilder()
                .setTaskId("1001")
                .setAction(TaskProto.Action.START)
                .setResult(TaskProto.Result.SUCCESS)
                .build();
        // protobuf对象转字节数组
        byte[] bytes = task.toByteArray();

        try {
            // byte数组生成protobuf
            TaskProto.Task task2 = TaskProto.Task.parseFrom(bytes);

            log.info("taskId:{} action:{} result:{}", task2.getTaskId(), task2.getAction(), task2.getResult());
//            System.out.println(task2.getTaskId());
//            System.out.println(task2.getAction());
//            System.out.println(task2.getResult());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }

        TaskBean taskBean = new TaskBean();
        taskBean.setTaskId(10001);
        taskBean.setAction(TaskBean.Action.START);
        TaskBean.Result result = new TaskBean.Result();
        result.setTag("failed");
        result.setFailInfo("参数错误!");
        taskBean.setResult(result);
        String taskJson = new Gson().toJson(taskBean);
        log.info("taskBean json:{}", taskJson);

        String jsonString = "{\"taskId\":100001,\"action\":\"STOP\",\"result\":{\"tag\":\"fail\",\"failInfo\":\"非法操作!\"}}";
        TaskBean taskBean2 = new Gson().fromJson(jsonString, TaskBean.class);
        log.info("taskId:{} action:{} result:{}", taskBean2.getTaskId(), taskBean2.getAction(), taskBean2.getResult());
    }
}