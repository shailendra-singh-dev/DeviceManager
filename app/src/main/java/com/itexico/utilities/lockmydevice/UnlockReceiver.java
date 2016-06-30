package com.itexico.utilities.lockmydevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.itexico.utilities.lockmydevice.utils.DevicePackageManagerUtil;

/**
 * Created by developeri on 5/26/16.
 */
public class UnlockReceiver extends BroadcastReceiver {

    private static final String TAG =UnlockReceiver.class.getSimpleName() ;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            Log.i(TAG, "AAAA onReceive()");
            DevicePackageManagerUtil.setLauncherComponentState(context, UnlockActivity.class, true);
            if( intent.getAction().equals(Intent.ACTION_SCREEN_ON) ||
                    intent.getAction().equals(Intent.ACTION_USER_PRESENT )) {
                Intent unlock = new Intent(context, UnlockActivity.class);
                unlock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(unlock);
            }
        }
    }
}