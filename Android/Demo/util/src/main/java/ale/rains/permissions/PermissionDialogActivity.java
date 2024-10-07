package ale.rains.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ale.rains.adb.cmd.CmdTools;
import ale.rains.util.Logger;
import ale.rains.util.R;
import ale.rains.util.RomUtils;
import ale.rains.util.SPUtils;
import ale.rains.util.StringUtils;
import ale.rains.util.ThreadManager;

public class PermissionDialogActivity extends Activity implements View.OnClickListener {
    public static final String PERMISSIONS_KEY = "permissions";
    private static final String PERMISSION_SKIP_RECORD = "skipRecord";
    private static final String PERMISSION_GRANT_RECORD = "grantRecord";
    private static final String PERMISSION_GRANT_ADB = "grantAdb";
    public static final int PERMISSION_FLOAT = 1;
    public static final int PERMISSION_ADB = 2;
    public static final int PERMISSION_ROOT = 3;
    public static final int PERMISSION_TOAST = 4;
    public static final int PERMISSION_ACCESSIBILITY = 5;
    public static final int PERMISSION_USAGE = 6;
    public static final int PERMISSION_RECORD = 7;
    public static final int PERMISSION_ANDROID = 8;
    public static final int PERMISSION_DYNAMIC = 9;
    public static volatile boolean sRunningStatus = false;
    private TextView mPermissionPassed;
    private TextView mPermissionTotal;
    private ProgressBar mProgressBar;
    private TextView mPermissionText;
    private LinearLayout mActionLayout;
    private LinearLayout mPositiveButton;
    private TextView mPositiveBtnText;
    private LinearLayout mNegativeButton;
    private TextView mNegativeBtnText;
    private int mCurrentIdx;
    private int mTotalIdx;
    private int USAGE_REQUEST = 10001;
    private int ACCESSIBILITY_REQUEST = 10002;
    private int M_PERMISSION_REQUEST = 10003;
    private int MEDIA_PROJECTION_REQUEST = 10004;

    private List<GroupPermission> mAllPermissions;

    /**
     * 权限名称映射表
     */
    public static final Map<String, String> PERMISSION_NAMES = new HashMap<String, String>() {
        {
            put(Manifest.permission.READ_CALENDAR, "读取日历");
            put(Manifest.permission.WRITE_CALENDAR, "写入日历");
            put(Manifest.permission.CAMERA, "相机");
            put(Manifest.permission.READ_CONTACTS, "读取联系人");
            put(Manifest.permission.WRITE_CONTACTS, "写入联系人");
            put(Manifest.permission.GET_ACCOUNTS, "获取账户");
            put(Manifest.permission.ACCESS_FINE_LOCATION, "获取精确定位");
            put(Manifest.permission.ACCESS_COARSE_LOCATION, "获取粗略定位");
            put(Manifest.permission.RECORD_AUDIO, "录音");
            put(Manifest.permission.READ_PHONE_STATE, "读取设备信息");
            put(Manifest.permission.CALL_PHONE, "拨打电话");
            put(Manifest.permission.READ_CALL_LOG, "读取通话记录");
            put(Manifest.permission.WRITE_CALL_LOG, "写入通话记录");
            put(Manifest.permission.ADD_VOICEMAIL, "添加语音邮箱");
            put(Manifest.permission.USE_SIP, "使用SIP");
            put(Manifest.permission.BODY_SENSORS, "获取传感器数据");
            put(Manifest.permission.SEND_SMS, "发送短信");
            put(Manifest.permission.RECEIVE_SMS, "接收短信");
            put(Manifest.permission.READ_SMS, "获取短信信息");
            put(Manifest.permission.RECEIVE_WAP_PUSH, "接收Wap Push");
            put(Manifest.permission.RECEIVE_MMS, "接收MMS");
            put(Manifest.permission.READ_EXTERNAL_STORAGE, "读取外部存储");
            put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入外部存储");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.permission_dialog);
        setupWindow();
        initView();
        initControl();
    }

    @Override
    public void onBackPressed() {
        finish();
        PermissionUtils.onPermissionResult(false, "取消授权");
    }

    /**
     * 设置窗体信息
     */
    private void setupWindow() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = display.getWidth() - dip2px(48);
        getWindow().setGravity(Gravity.CENTER);
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void finish() {
        sRunningStatus = false;
        super.finish();
    }

    @Override
    protected void onDestroy() {
        sRunningStatus = false;
        super.onDestroy();
    }

    private void initView() {
        mPermissionPassed = findViewById(R.id.permission_success);
        mPermissionTotal = findViewById(R.id.permission_all);
        mProgressBar = findViewById(R.id.permission_loading_progress);
        mPermissionText = findViewById(R.id.permission_text);
        mActionLayout = findViewById(R.id.permission_action_layout);
        mPositiveButton = findViewById(R.id.permission_positive_button);
        mPositiveBtnText = (TextView) mPositiveButton.getChildAt(0);
        mNegativeButton = findViewById(R.id.permission_negative_button);
        mNegativeBtnText = (TextView) mNegativeButton.getChildAt(0);
    }

    private void initControl() {
        mPositiveButton.setOnClickListener(this);
        mNegativeButton.setOnClickListener(this);
        groupPermissions();
        processPermission();
    }

    /**
     * 权限分组
     */
    private void groupPermissions() {
        List<String> permissions = getIntent().getStringArrayListExtra(PERMISSIONS_KEY);
        Map<Integer, GroupPermission> currentPermissions = new LinkedHashMap<>();

        for (String permission : permissions) {
            int group;
            switch (permission) {
                case "float":
                    group = PERMISSION_FLOAT;
                    break;
                case "root":
                    group = PERMISSION_ROOT;
                    break;
                case "adb":
                    group = PERMISSION_ADB;
                    break;
                case Settings.ACTION_USAGE_ACCESS_SETTINGS:
                    group = PERMISSION_USAGE;
                    break;
                case Settings.ACTION_ACCESSIBILITY_SETTINGS:
                    group = PERMISSION_ACCESSIBILITY;
                    break;
                case "screenRecord":
                    group = PERMISSION_RECORD;
                    break;
                default:
                    if (permission.startsWith("Android=")) {
                        group = PERMISSION_ANDROID;
                    } else if (permission.startsWith("toast:")) {
                        group = PERMISSION_TOAST;
                    } else {
                        group = PERMISSION_DYNAMIC;
                    }
                    break;
            }

            GroupPermission permissionG = currentPermissions.get(group);
            if (permissionG == null) {
                permissionG = new GroupPermission(group);
                currentPermissions.put(group, permissionG);
            }

            permissionG.addPermission(permission);
        }
        mAllPermissions = new ArrayList<>(currentPermissions.values());
    }

    /**
     * 开始处理权限
     */
    public void processPermission() {
        if (mAllPermissions == null || mAllPermissions.size() == 0) {
            showAction(getString(R.string.permission_list_error), getString(R.string.confirm), new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }

        mCurrentIdx = -1;
        mTotalIdx = mAllPermissions.size();

        // 设置待处理总数
        mPermissionTotal.setText(mTotalIdx + "");

        // 开始处理权限
        processedAction();
    }

    /**
     * 处理单项权限
     */
    private void processSinglePermission() {
        final GroupPermission permission = mAllPermissions.get(mCurrentIdx);
        switch (permission.permissionType) {
            case PERMISSION_FLOAT:
                if (!processFloatPermission()) {
                    return;
                }
                break;
            case PERMISSION_ROOT:
                if (!processRootPermission()) {
                    return;
                }
                break;
            case PERMISSION_ADB:
                if (!processAdbPermission()) {
                    return;
                }
                break;
            case PERMISSION_TOAST:
                if (!processToastPermission(permission)) {
                    return;
                }
                break;
            case PERMISSION_USAGE:
                if (!processUsagePermission()) {
                    return;
                }
                break;
            case PERMISSION_ACCESSIBILITY:
                if (!processAccessibilityPermission()) {
                    return;
                }
                break;
            case PERMISSION_RECORD:
                if (!processRecordPermission()) {
                    return;
                }
                break;
            case PERMISSION_ANDROID:
                if (!processAndroidVersionPermission(permission)) {
                    return;
                }
                break;
            case PERMISSION_DYNAMIC:
                if (!processDynamicPermission(permission)) {
                    return;
                }
                break;
        }
        processedAction();
    }

    /**
     * 悬浮窗权限判断
     */
    private boolean processFloatPermission() {
        if (!FloatWindowManager.getInstance().checkPermission(this)) {
            showAction(getString(R.string.float_permission), getString(R.string.permission_i_grant), new Runnable() {
                @Override
                public void run() {
                    if (FloatWindowManager.getInstance().checkPermission(PermissionDialogActivity.this)) {
                        processedAction();
                    } else {
                        Toast.makeText(PermissionDialogActivity.this, R.string.permission_no_float_permission, Toast.LENGTH_LONG).show();
                    }
                }
            }, getString(R.string.confirm), new Runnable() {
                @Override
                public void run() {
                    FloatWindowManager.getInstance().applyPermissionDirect(PermissionDialogActivity.this);
                }
            });
            return false;
        }
        return true;
    }

    /**
     * 判断root权限
     */
    private boolean processRootPermission() {
        if (!RomUtils.isRooted()) {
            showAction(getString(R.string.root_permission), getString(R.string.confirm), new Runnable() {
                @Override
                public void run() {
                    finish();
                    PermissionUtils.onPermissionResult(false, "该需要Root权限，请Root后使用");
                }
            });
            return false;
        }

        return true;
    }

    /**
     * 处理ADB权限
     */
    private boolean processAdbPermission() {
        ThreadManager.getInstance().postWorkTask(new Runnable() {
            @Override
            public void run() {
                boolean status;
                if (SPUtils.getBoolean(PERMISSION_GRANT_ADB, false)) {
                    status = CmdTools.generateConnection();
                } else {
                    status = CmdTools.isInitialized();
                }
                if (!status) {
                    showAction(getString(R.string.adb_permission), getString(R.string.confirm), new Runnable() {
                        @Override
                        public void run() {
                            SPUtils.put(PERMISSION_GRANT_ADB, true);
                            mProgressBar.setVisibility(View.VISIBLE);
                            mPermissionText.setText(R.string.adb_open_advice);
                            mPositiveButton.setEnabled(false);
                            ThreadManager.getInstance().postWorkTask(new Runnable() {
                                @Override
                                public void run() {
                                    boolean result;
                                    try {
                                        result = CmdTools.generateConnection();
                                    } catch (Exception e) {
                                        Logger.e(e);
                                        result = false;
                                    }

                                    if (result) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mPositiveButton.setEnabled(true);
                                            }
                                        });
                                        processedAction();
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressBar.setVisibility(View.GONE);
                                                mPermissionText.setText(R.string.open_adb_permission_failed);
                                                mPositiveButton.setEnabled(true);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }, getString(R.string.cancel), new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            PermissionUtils.onPermissionResult(false, "ADB连接失败");
                        }
                    });
                } else {
                    processedAction();
                }
            }
        });
        return false;
    }

    /**
     * 处理提示信息
     */
    private boolean processToastPermission(GroupPermission permissionG) {
        List<String> permissions = permissionG.permissions;
        final List<String> real = new ArrayList<>(permissions.size() + 1);

        for (String p : permissions) {
            String permission = p.substring(6);
            if (!SPUtils.getBoolean(permission, false)) {
                real.add(permission);
            }
        }

        if (!real.isEmpty()) {
            showAction(StringUtils.join("\n", real), getString(R.string.permission_i_know), new Runnable() {
                @Override
                public void run() {
                    processedAction();
                }
            }, getString(R.string.no_inform), new Runnable() {
                @Override
                public void run() {
                    for (String p : real) {
                        SPUtils.put(p, true);
                    }
                    processedAction();
                }
            });
            return false;
        }
        return true;
    }

    /**
     * 处理使用情况权限
     */
    private boolean processUsagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !PermissionUtils.isUsageStatPermissionOn(this)) {
            showAction(getString(R.string.device_usage_permission), getString(R.string.permission_i_open), new Runnable() {
                @Override
                public void run() {
                    if (PermissionUtils.isUsageStatPermissionOn(PermissionDialogActivity.this)) {
                        processedAction();
                    } else {
                        Toast.makeText(PermissionDialogActivity.this, getString(R.string.permission_valid_fail), Toast.LENGTH_SHORT).show();
                    }
                }
            }, getString(R.string.confirm), new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivityForResult(intent, USAGE_REQUEST);
                }
            });
            return false;
        }

        return true;
    }

    /**
     * 处理辅助功能权限
     */
    private boolean processAccessibilityPermission() {
        return true;
    }

    private boolean processRecordPermission() {
        return true;
    }

    /**
     * 处理系统权限版本
     */
    private boolean processAndroidVersionPermission(GroupPermission permission) {
        int maxVersion = 0;

        // 计算需要的最高系统版本
        for (String per : permission.permissions) {
            int currentMax = Integer.parseInt(per.substring(8));
            if (currentMax > maxVersion) {
                maxVersion = currentMax;
            }
        }


        if (Build.VERSION.SDK_INT < maxVersion) {
            showAction(getString(R.string.android_version_error, maxVersion, Build.VERSION.SDK_INT), getString(R.string.confirm), new Runnable() {
                @Override
                public void run() {
                    finish();
                    PermissionUtils.onPermissionResult(false, "系统版本过低");
                }
            });
            return false;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        sRunningStatus = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 处理需要动态授权的权限
     */
    private boolean processDynamicPermission(GroupPermission permission) {
        // 动态申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] requestPermissions = permission.permissions.toArray(new String[0]);

            // 看下哪些权限没有被授权
            final List<String> ungrantedPermissions = PermissionUtils.checkUngrantedPermission(this, requestPermissions);
            if (ungrantedPermissions != null && ungrantedPermissions.size() > 0) {
                List<String> mappedName = new ArrayList<>();
                for (String dynPermission : ungrantedPermissions) {
                    String mapName = PERMISSION_NAMES.get(dynPermission);
                    if (mapName != null) {
                        mappedName.add(mapName);
                    } else {
                        mappedName.add(dynPermission);
                    }
                }

                String permissionNames = StringUtils.join("、", mappedName);

                showAction(getString(R.string.request_dynamic_permission, permissionNames, ungrantedPermissions.size()), getString(R.string.confirm), new Runnable() {
                    @Override
                    public void run() {
                        ActivityCompat.requestPermissions(PermissionDialogActivity.this, ungrantedPermissions.toArray(new String[0]), M_PERMISSION_REQUEST);
                    }
                }, getString(R.string.cancel), new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        PermissionUtils.onPermissionResult(false, "用户不进行授权");
                    }
                });
                return false;
            }
        }
        return true;
    }

    /**
     * 显示操作框
     *
     * @param message        显示文案
     * @param positiveText   确定文案
     * @param positiveAction 确定动作
     */
    private void showAction(String message, String positiveText, Runnable positiveAction) {
        showAction(message, positiveText, positiveAction, null, null);
    }

    /**
     * 显示操作框
     *
     * @param message      显示文案
     * @param positiveText 确定文案
     * @param positiveAct  确定动作
     * @param negativeText 取消文案
     * @param negativeAct  取消动作
     */
    private void showAction(final String message, final String positiveText, final Runnable positiveAct,
                            final String negativeText, final Runnable negativeAct) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                positiveAction = positiveAct;
                negativeAction = negativeAct;

                mProgressBar.setVisibility(View.GONE);
                mActionLayout.setVisibility(View.VISIBLE);

                // 显示文字
                mPermissionText.setText(Html.fromHtml(StringUtils.patternReplace(message, "\n", "<br/>")));

                // 设置按钮文本
                mPositiveBtnText.setText(positiveText);
                // 如果取消非空
                if (!TextUtils.isEmpty(negativeText)) {
                    mNegativeButton.setVisibility(View.VISIBLE);
                    mNegativeBtnText.setText(negativeText);
                } else {
                    mNegativeButton.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 当前权限已处理完毕
     */
    private void processedAction() {
        mCurrentIdx++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.VISIBLE);
                mActionLayout.setVisibility(View.GONE);
                mPermissionPassed.setText((mCurrentIdx + 1) + "");
                if (mCurrentIdx >= mTotalIdx) {
                    finish();
                    PermissionUtils.onPermissionResult(true, null);
                    return;
                }
                processSinglePermission();
            }
        });
    }

    private Runnable positiveAction;
    private Runnable negativeAction;

    @Override
    public void onClick(View v) {
        if (v == mPositiveButton) {
            if (positiveAction != null) {
                positiveAction.run();
            }
        } else if (v == mNegativeButton) {
            if (negativeAction != null) {
                negativeAction.run();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USAGE_REQUEST) {
            mCurrentIdx--;
            processedAction();
        } else if (requestCode == MEDIA_PROJECTION_REQUEST && resultCode == RESULT_OK) {
            processedAction();
            SPUtils.put(PERMISSION_GRANT_RECORD, true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == M_PERMISSION_REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                int result = grantResults[i];
                if (result != PackageManager.PERMISSION_GRANTED) {
                    // 重新去检查权限
                    processSinglePermission();
                    return;
                }
            }
            processedAction();
        }
    }

    /**
     * 权限分组
     */
    private static class GroupPermission {
        private int permissionType;
        private List<String> permissions;

        private GroupPermission(int permissionType) {
            this.permissionType = permissionType;
        }

        /**
         * 添加一条权限
         */
        private void addPermission(String permission) {
            if (permissions == null) {
                permissions = new ArrayList<>();
            }

            if (!permissions.contains(permission)) {
                permissions.add(permission);
            }
        }
    }
}