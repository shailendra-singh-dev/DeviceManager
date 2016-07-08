package com.itexico.utilities.lockmydevice;

import android.app.Application;
import android.util.Log;

import com.itexico.utilities.lockmydevice.utils.DeviceMetaData;

/**
 * Created by iTexico Developer on 6/29/2016.
 */
public class DeviceApplication extends Application {

    private static final String TAG = DeviceApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "AAAA onCreate(),DeviceMetaData.DEVICE_DETAILS:"+ DeviceMetaData.DEVICE_DETAILS);
    }
}
