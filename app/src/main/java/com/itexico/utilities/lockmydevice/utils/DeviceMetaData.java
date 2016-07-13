package com.itexico.utilities.lockmydevice.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.itexico.utilities.lockmydevice.R;

/**
 *  * Created by iTexico Developer on 7/1/2016.
 */
public class DeviceMetaData {

    public static final String DEVICE_DETAILS = "VERSION.RELEASE : " + Build.VERSION.RELEASE
            + "\nVERSION.INCREMENTAL : " + Build.VERSION.INCREMENTAL
            + "\nVERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT
            + "\nBOARD : " + Build.BOARD
            + "\nBOOTLOADER : " + Build.BOOTLOADER
            + "\nBRAND : " + Build.BRAND
            + "\nCPU_ABI : " + Build.CPU_ABI
            + "\nCPU_ABI2 : " + Build.CPU_ABI2
            + "\nDISPLAY : " + Build.DISPLAY
            + "\nFINGERPRINT : " + Build.FINGERPRINT
            + "\nHARDWARE : " + Build.HARDWARE
            + "\nHOST : " + Build.HOST
            + "\nID : " + Build.ID
            + "\nMANUFACTURER : " + Build.MANUFACTURER
            + "\nMODEL : " + Build.MODEL
            + "\nPRODUCT : " + Build.PRODUCT
            + "\nSERIAL : " + Build.SERIAL
            + "\nTAGS : " + Build.TAGS
            + "\nTIME : " + Build.TIME
            + "\nTYPE : " + Build.TYPE
            + "\nUNKNOWN : " + Build.UNKNOWN
            + "\nUSER : " + Build.USER;

    private static final String DEVICE_TYPE_PHONE = "Phone";
    private static final String DEVICE_TYPE_TABLET = "Tablet";

    private DeviceMetaData() {
    }

    public static String getDeviceModelNumber() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return AppUtils.capitalize(model);
        }
        return AppUtils.capitalize(manufacturer) + " " + model;
    }

    public static String getDeviceIMEINumber(final Context context) {
        String deviceID ="";
        String serviceName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(serviceName);
        deviceID = telephonyManager.getDeviceId();
        if(null == deviceID || deviceID.isEmpty()){
            deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceID;
    }

    public static String getDeviceType(final Context context) {
        return context.getResources().getBoolean(R.bool.is_phone) ? DEVICE_TYPE_PHONE : DEVICE_TYPE_TABLET;
    }

    public static String getDeviceOSVersion() {
        return "" + Build.VERSION.RELEASE;
    }

    public static String getDeviceSerialNumber() {
        return Build.SERIAL;
    }

    @Override
    public String toString() {
        return DEVICE_DETAILS;
    }
}
