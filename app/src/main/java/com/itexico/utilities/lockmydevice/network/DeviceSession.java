package com.itexico.utilities.lockmydevice.network;

import com.itexico.utilities.lockmydevice.BuildConfig;

/**
 * Created by iTexico Developer on 7/5/2016.
 */
public class DeviceSession {

    private static final DeviceSession DEVIDE_SESSION = new DeviceSession();
    public static final String API_BASE_URL = BuildConfig.BASE_URL;
    public static final String CLIENT_ID = BuildConfig.CLIENT_ID;
    public static final String CLIENT_SECRET = BuildConfig.CLIENT_SECRET;

    synchronized public static DeviceSession getInstance() {
        return DEVIDE_SESSION;
    }

    public void saveSession(){

    }

    public void restoreSession(){

    }

    public String getAuthorizationCode() {
        return "";
    }
}
