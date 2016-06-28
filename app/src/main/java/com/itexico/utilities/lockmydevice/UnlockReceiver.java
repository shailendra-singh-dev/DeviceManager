package com.itexico.utilities.lockmydevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by developeri on 5/26/16.
 */
public class UnlockReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if( intent.getAction().equals(Intent.ACTION_SCREEN_ON) ||
                    intent.getAction().equals(Intent.ACTION_USER_PRESENT )) {
                Intent unlock = new Intent(context, UnlockActivity.class);
                unlock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(unlock);
            }
        }
    }
}