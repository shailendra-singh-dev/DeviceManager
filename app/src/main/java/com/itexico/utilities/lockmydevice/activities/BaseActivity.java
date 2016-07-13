package com.itexico.utilities.lockmydevice.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.itexico.utilities.lockmydevice.service.DeviceService;

/**
 * Created by iTexico Developer on 7/6/2016.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected DeviceService mDeviceService;

    // Flag indicating whether we have called bind on the service.
    protected boolean mBound;

    // Class for interacting with the main interface of the service.
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
            // This is called when the connection with the iBinder has been established, giving us the object we can use
            // to interact with the iBinder.  We are communicating with the iBinder using a Messenger, so here we get a
            // client-side representation of that from the raw IBinder object.
            DeviceService.DeviceServiceBinder deviceServiceBinder = (DeviceService.DeviceServiceBinder) iBinder;
            mDeviceService = deviceServiceBinder.getService();
            mBound = true;
            Log.i(TAG,"AAAA onServiceConnected()");
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been unexpectedly disconnected -- that is,
            // its process crashed.
            mBound = false;
            Log.i(TAG,"AAAA onServiceDisconnected()");
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service
        bindService(new Intent(this, DeviceService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    public void showAlertDialogWithPositiveButton(int positiveButtonTitleId, int titleId, String message,final DialogInterface.OnClickListener iOnAlertDialogClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(titleId);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(positiveButtonTitleId),iOnAlertDialogClickListener);
        alertDialogBuilder.create().show();
    }

}
