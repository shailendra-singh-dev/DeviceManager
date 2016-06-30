package com.itexico.utilities.lockmydevice;

import android.app.Application;
import android.util.Log;

/**
 * Created by iTexico Developer on 6/29/2016.
 */
public class DeviceApplication extends Application {

    private static final String TAG = DeviceApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "AAAA onCreate()");
    }

}
