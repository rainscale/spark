package ale.rains.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import ale.rains.demo.view.FloatView;
import ale.rains.util.LogUtils;

public class ToastActivity extends Activity {
    private static FloatView floatView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i("onCreate");
        Intent intent = getIntent();

        String message = intent.getStringExtra("message");
        if (message != null && !"".equals(message)) {
            Toast.makeText(this, "openatx: " + message, Toast.LENGTH_SHORT).show();
        }

        String showFloat = intent.getStringExtra("showFloatWindow");
        LogUtils.i("showFloat: " + showFloat);
        if ("true".equals(showFloat)) {
            getFloatView().show();
        } else if ("false".equals(showFloat)) {
            getFloatView().hide();
        }

        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private FloatView getFloatView() {
        if (floatView == null) {
            floatView = new FloatView(ToastActivity.this);
        }
        return floatView;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}