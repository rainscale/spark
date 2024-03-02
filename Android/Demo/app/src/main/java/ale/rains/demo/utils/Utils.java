package ale.rains.demo.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
    private static final String TAG = "Utils";

    public static String testAssets(Context context) {
        InputStream is;
        try {
            is = context.getResources().getAssets().open("assets.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public static void test(Context context) {
        String str;
        str = testAssets(context);
        Log.i(TAG, str);
    }
}
