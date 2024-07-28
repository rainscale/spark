package ale.rains.test;

import android.Manifest;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import ale.rains.remote.RemoteConstants;
import ale.rains.remote.RemoteManager;
import ale.rains.remote.IMessageCallback;
import ale.rains.util.AppContext;
import ale.rains.util.AppUtils;
import ale.rains.util.Logger;
import ale.rains.util.ShellUtils;
import ale.rains.util.StringUtils;
import ale.rains.util.ThreadManager;

public class MainActivity extends AppCompatActivity {
    private EditText mCommandEt;
    private EditText mMessageEt;
    private Button mLogCatchBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCommandEt = findViewById(R.id.et_command);
        mMessageEt = findViewById(R.id.et_message);
        mLogCatchBt = findViewById(R.id.bt_log_catch);
        ((TextView) findViewById(R.id.tv_version)).setText("当前版本:" + BuildConfig.VERSION_NAME + ", pid:" + AppUtils.getPidByPackageName(getPackageName()));
        requestPermissions();
        RemoteManager.getInstance().registerRemoteMessageCallback(new IMessageCallback() {
            @Override
            public void replyMessage(int type, String message) {
                Logger.i("remote reply " + type + ":" + message);
                ThreadManager.getInstance().postUITask(() -> {
                    Toast.makeText(MainActivity.this, type + ":" + message, Toast.LENGTH_SHORT).show();
                });
            }
        });

        RemoteManager.getInstance().registerLocalMessageCallback(new IMessageCallback() {
            @Override
            public void replyMessage(int type, String message) {
                Logger.i("local reply " + type + ":" + message);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            return true;
        } else if (id == R.id.action_exit) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        Logger.d("onResume");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Logger.d("onDestroy");
        super.onDestroy();
    }

    private void requestPermissions() {
        List<String> permissionList = Arrays.asList(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.RECORD_AUDIO);
    }

    public void applyPermission(View view) {
        requestPermissions();
    }

    public void runCommand(View view) {
        String command = mCommandEt.getText().toString().trim();
        if (StringUtils.isBlank(command)) {
            Toast.makeText(this, "请输入命令", Toast.LENGTH_SHORT).show();
            return;
        }
        Logger.d("command：" + command);
        ThreadManager.getInstance().postWorkTask(() -> {
            String result = ShellUtils.run(command);
            Logger.d("result: " + result);
        });
    }

    public void getPid(View view) {
        String packageName = mCommandEt.getText().toString().trim();
        if (StringUtils.isBlank(packageName)) {
            packageName = getPackageName();
        }
        int pid = AppUtils.getPidByPackageName(packageName);
        Logger.i(packageName + "->pid:" + pid);
        Toast.makeText(this, packageName + "->" + pid, Toast.LENGTH_SHORT).show();
    }

    public void sendMessage(View view) {
        String msg = mMessageEt.getText().toString().trim();
        if (StringUtils.isBlank(msg)) {
            msg = "test";
        }
        Logger.d("msg: " + msg);
        /*
        String finalMsg = msg;
        MessageAgentContext.getInstance().sendMessage(msg, true, new OPCResultCallback() {
            @Override
            public void onSuccess() {
                Logger.d(finalMsg);
                Toast.makeText(MainActivity.this, finalMsg + "发送成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String msg) {
                Logger.i(msg);
                Toast.makeText(MainActivity.this, finalMsg + "发送失败:" + msg, Toast.LENGTH_SHORT).show();
            }
        });
         */
    }

    public void catchLog(View view) {

    }

    public void testCommunication(View view) {
        Logger.i("testCommunication");
        new Thread(() -> {
            if (!RemoteManager.getInstance().isBound()) {
                RemoteManager.getInstance().init(AppContext.getContext());
                ThreadManager.sleep(5000);
            }
            RemoteManager.getInstance().sendMessage(RemoteConstants.MsgType.TEST, "Hello!");
        }).start();
    }
}