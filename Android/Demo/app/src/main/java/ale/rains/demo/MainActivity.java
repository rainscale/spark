package ale.rains.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import ale.rains.demo.databinding.ActivityMainBinding;
import ale.rains.demo.permission.PermissionTestActivity;
import ale.rains.demo.utils.NotificationUtil;
import ale.rains.demo.utils.Utils;
import ale.rains.lz4.LZ4JNI;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
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

    private PowerManager.WakeLock mWakelock = null;
    private ActivityMainBinding binding;

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
        Log.d(TAG, "startForegroundService");
        Intent intent = new Intent(this, HelloService.class);
        startService(intent);
    }

    private void stopForegroundService() {
        Log.d(TAG, "stopForegroundService");
        Intent intent = new Intent(this, HelloService.class);
        stopService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acquireWakelock();
        NotificationUtil.openNotificationSetting(this, new NotificationUtil.OnNextListener() {
            @Override
            public void onNext() {
                Toast.makeText(MainActivity.this, "已开启通知权限", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "已开启通知权限");
            }
        });
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());

        binding.btnJni.setOnClickListener((v)->{
            test(1, 2);
        });

        Button tvNotification = binding.btnNotification;
        tvNotification.setOnClickListener((View v) -> {
            Log.d(TAG, "tv_notification onClick");
            startForegroundService();
        });

        Button btnPermission = binding.btnPermission;
        btnPermission.setOnClickListener((View v) -> {
            Log.d(TAG, "btn_permission onClick");
            Intent it = new Intent(MainActivity.this, PermissionTestActivity.class);
            startActivity(it);
        });

        Button btnLz4 = binding.btnLz4;
        btnLz4.setOnClickListener((View v) -> {
            Log.d(TAG, "btn_lz4 onClick");
            new Thread(() -> {
                Log.d(TAG, "lz4 compress");
                try {
                    byte[] data = "1234512345123451234512345345234572".getBytes("UTF-8");
                    final int decompressedLength = data.length;
                    int maxCompressedLength = LZ4JNI.LZ4_compressBound(decompressedLength);
                    byte[] compressedData = new byte[maxCompressedLength];
                    int compressedLength = LZ4JNI.LZ4_compressHC(data, null, 0, decompressedLength, compressedData, null, 0, maxCompressedLength, 9);
                    Log.d(TAG, "compressedLength = " + compressedLength);
                    Log.d(TAG, "compressedData = " + Arrays.toString(compressedData));
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, e.getMessage());
                }
            }).start();
        });

        binding.btnTest.setOnClickListener((v)->{
            Utils.test(getApplicationContext());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        releaseWakelock();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        stopForegroundService();
    }

    /**
     * A native method that is implemented by the 'demo' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native int test(int i, int j);
}