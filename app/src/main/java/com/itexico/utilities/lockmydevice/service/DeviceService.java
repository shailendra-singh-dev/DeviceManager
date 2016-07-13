package com.itexico.utilities.lockmydevice.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.itexico.utilities.lockmydevice.activities.UnlockActivity;
import com.itexico.utilities.lockmydevice.devicepackage.DevicePackageManager;
import com.itexico.utilities.lockmydevice.preferences.DevicePreferencesManager;
import com.itexico.utilities.lockmydevice.utils.DeviceMetaData;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by iTexico Developer on 7/5/2016.
 */
public class DeviceService extends Service {

    private static final String TAG = DeviceService.class.getSimpleName();
    public static final String ACTION = "com.itexico.utilities.lockmydevice.service.action.MAIN";
    // Random number generator
    private final Random mGenerator = new Random();
    // Binder given to clients
    private final IBinder mBinder = new DeviceServiceBinder();

    // Determine the number of cores on the device
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    // Construct thread pool passing in configuration options
    // int minPoolSize, int maxPoolSize, long keepAliveTime, TimeUnit unit,
    // BlockingQueue<Runnable> workQueue
    final private ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(
            NUMBER_OF_CORES * 2,
            NUMBER_OF_CORES * 2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>()
    );
    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    public void onCreate() {
        super.onCreate();
        // Get access to local broadcast manager
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // run until explicitly stopped.
    }

    public void launchDefaultLauncher(final Context context) {
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DevicePackageManager.getInstance().launchDefaultLauncher(context);
                final boolean isMyAppLauncherDefault = DevicePackageManager.getInstance().isMyAppLauncherDefault(context);
                Log.i(TAG, "AAAA unlock(), isMyAppLauncherDefault:" + isMyAppLauncherDefault);
                if (isMyAppLauncherDefault) {
                    DevicePackageManager.getInstance().setMyLauncherComponentState(context, UnlockActivity.class, false);
                }
            }
        });
    }

    public void saveCredentials(final String user, final String pass) {
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final String imeiNumber = DeviceMetaData.getDeviceIMEINumber(DeviceService.this);
                Log.i(TAG,"AAAA saveCredentials imeiNumber:"+imeiNumber);
                DevicePreferencesManager.setString(DeviceService.this, DevicePreferencesManager.SHAREDPREFERENCE_KEY.KEY_USER, user);
                DevicePreferencesManager.setString(DeviceService.this, DevicePreferencesManager.SHAREDPREFERENCE_KEY.KEY_PASSWORD, pass);
            }
        });
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public final class DeviceServiceBinder extends Binder {
        public DeviceService getService() {
            // Return this instance of LocalService so clients can call public methods
            return DeviceService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
