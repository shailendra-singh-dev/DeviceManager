package com.itexico.utilities.lockmydevice.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.itexico.utilities.lockmydevice.R;

/**
 * Created by iTexico Developer on 6/29/2016.
 */
public class AppUIUtils {

    public static void showErrorBadCredentialsMessage( Context context,int message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getString(R.string.bad_credentials));
        alert.setMessage(message);
        alert.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.create().show();
    }

}
