package ale.rains.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import ale.rains.demo.HelloService;
import ale.rains.demo.R;
import ale.rains.demo.databinding.ActivityMainBinding;
import ale.rains.demo.model.EventMessage;
import ale.rains.demo.permission.PermissionTestActivity;
import ale.rains.demo.utils.NotificationUtil;
import ale.rains.demo.utils.Utils;
import ale.rains.demo.view.FloatView;
import ale.rains.lz4.LZ4JNI;
import ale.rains.permission.FloatWindowManager;
import ale.rains.toast.Toaster;
import ale.rains.util.AdbUtils;
import ale.rains.util.Logger;
import ale.rains.util.ShellUtils;
import ale.rains.util.StringUtils;

public class MainActivity extends AppCompatActivity {
    // 渠道名
    private static final String CHANNEL_NAME = "普通通知";
    // 渠道ID
    private static final String CHANNEL_ID = "normal";
    // 渠道描述
    private static final String CHANNEL_DESCRIPTION = "这是一个用来测试的通知";

    // Used to load the 'demo' library on application startup.
    static {
        System.loadLibrary("demo");
    }

    private boolean isFloatViewShown = false;
    private PowerManager.WakeLock mWakelock = null;
    private ActivityMainBinding binding;
    private FloatView floatView;

    private void initWakeLock() {
        if (mWakelock == null) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, this.getClass().getCanonicalName());
        }
    }

    private void acquireWakelock() {
        // 方式一
//        if (mWakelock != null) {
//            mWakelock.acquire();
//        }
        // 方式二
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void releaseWakelock() {
        // 方式一
//        if (mWakelock != null) {
//            mWakelock.release();
//        }
        // 方式二
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void startForegroundService() {
        Logger.d("startForegroundService");
        Intent intent = new Intent(this, HelloService.class);
        startService(intent);
    }

    private void stopForegroundService() {
        Logger.d("stopForegroundService");
        Intent intent = new Intent(this, HelloService.class);
        stopService(intent);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acquireWakelock();
        NotificationUtil.openNotificationSetting(this, new NotificationUtil.OnNextListener() {
            @Override
            public void onNext() {
                Toast.makeText(MainActivity.this, "已开启通知权限", Toast.LENGTH_SHORT).show();
                Logger.d("已开启通知权限");
            }
        });
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());

        binding.btnJni.setOnClickListener((v) -> {
            test(1, 2);
        });

        Button tvNotification = binding.btnNotification;
        tvNotification.setOnClickListener((View v) -> {
            Logger.d("tv_notification onClick");
            startForegroundService();
        });

        Button btnPermission = binding.btnPermission;
        btnPermission.setOnClickListener((View v) -> {
            Logger.d("btn_permission onClick");
            Intent it = new Intent(MainActivity.this, PermissionTestActivity.class);
            startActivity(it);
        });

        Button btnLz4 = binding.btnLz4;
        btnLz4.setOnClickListener((View v) -> {
            Logger.d("btn_lz4 onClick");
            new Thread(() -> {
                Logger.d("lz4 compress");
                try {
                    byte[] data = "1234512345123451234512345345234572".getBytes("UTF-8");
                    final int decompressedLength = data.length;
                    int maxCompressedLength = LZ4JNI.LZ4_compressBound(decompressedLength);
                    byte[] compressedData = new byte[maxCompressedLength];
                    int compressedLength = LZ4JNI.LZ4_compressHC(data, null, 0, decompressedLength, compressedData, null, 0, maxCompressedLength, 9);
                    Logger.d("compressedLength = " + compressedLength);
                    Logger.d("compressedData = " + Arrays.toString(compressedData));
                } catch (UnsupportedEncodingException e) {
                    Logger.e(e.getMessage());
                }
            }).start();
        });

        binding.btnFloatWindow.setOnClickListener((v) -> {
            isFloatViewShown = !isFloatViewShown;
            if (isFloatViewShown) {
                boolean floatEnabled = FloatWindowManager.getInstance().checkFloatPermission(MainActivity.this);
                if (!floatEnabled) {
                    Logger.i("float permission not checked");
                    return;
                }
                if (floatView == null) {
                    floatView = new FloatView(MainActivity.this);
                }
                floatView.show();
            } else {
                if (floatView != null) {
                    Logger.d("remove floatView immediate");
                    floatView.hide();
                }
            }
        });

        binding.btnTest.setOnClickListener((v) -> {
            Utils.test(getApplicationContext());
        });

        binding.btnThreadTest.setOnClickListener((v) -> {
            Thread thread = new Thread(() -> {
                thread_test(0, 8);
            });
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        });

        binding.btnEventbus.setOnClickListener((v) -> {
            //发送数据给监听者
            EventBus.getDefault().post("msg1 coming...");
            EventMessage event = new EventMessage(1000, "msg2 coming...");
            EventBus.getDefault().post(event);
        });

        binding.btnAdbcmd.setOnClickListener((v)-> {
            String cmd = binding.etCmd.getText().toString().trim();
            if (StringUtils.isBlank(cmd)) {
                Toaster.show("Please input command!");
                return;
            }
            String result = AdbUtils.execCommandReturnResult(cmd);
            Logger.i("adb cmd result:\n" + result);
        });

        binding.btnShellcmd.setOnClickListener((v)-> {
            String cmd = binding.etCmd.getText().toString().trim();
            if (StringUtils.isBlank(cmd)) {
                Toaster.show("Please input command!");
                return;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d("onStop");
        releaseWakelock();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy");
        stopForegroundService();
    }

    /**
     * A native method that is implemented by the 'demo' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native int test(int i, int j);

    public native void thread_test(long time, int n);
}