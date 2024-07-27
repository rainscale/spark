package ale.rains.demo.uiautomator;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import androidx.test.InstrumentationRegistry;
import androidx.test.filters.LargeTest;
import androidx.test.filters.SdkSuppress;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ale.rains.util.Logger;

/**
 *
 * @author xiaocong@gmail.com
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class Stub {
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final int PORT = 9009;
    AutomatorHttpServer server = new AutomatorHttpServer(PORT);

    @Before
    public void setUp() throws Exception {
        Logger.i("setUp");
        launchService();
        server.start();
    }

    private void launchPackage(String packageName) {
        Logger.i("Launch " + packageName);
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        device.wait(Until.hasObject(By.pkg(packageName).depth(0)), LAUNCH_TIMEOUT);
        device.pressHome();
    }

    private void launchService() throws RemoteException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = InstrumentationRegistry.getContext();
        device.wakeUp();

        // Wait for launcher
        String launcherPackage = device.getLauncherPackageName();
        Boolean ready = device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
        if (!ready) {
            Logger.i("Wait for launcher timeout");
            return;
        }
        startMonitorService(context);
    }

    private void startMonitorService(Context context) {
        Intent intent = new Intent("ale.rains.demo.ACTION_START");
        intent.setPackage("ale.rains.demo"); // fix error: Service Intent must be explicit
        context.startService(intent);
    }

    @After
    public void tearDown() {
        Logger.i("tearDown");
        server.stop();
        Context context = InstrumentationRegistry.getContext();
        stopMonitorService(context);
    }

    private void stopMonitorService(Context context) {
        Intent intent = new Intent("ale.rains.demo.ACTION_STOP");
        intent.setPackage("ale.rains.demo");
        context.startService(intent);
    }

    @Test
    @LargeTest
    public void testUIAutomatorStub() throws InterruptedException {
        Logger.i("testUIAutomatorStub");
        while (server.isAlive()) {
            Thread.sleep(100);
        }
    }
}