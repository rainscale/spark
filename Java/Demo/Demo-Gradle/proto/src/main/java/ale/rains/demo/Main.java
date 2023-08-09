package ale.rains.demo;

import ale.rains.demo.proto.TaskProto;
import com.google.protobuf.InvalidProtocolBufferException;

public class Main {
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
            System.out.println(task2.getTaskId());
            System.out.println(task2.getAction());
            System.out.println(task2.getResult());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}