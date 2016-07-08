package com.itexico.utilities.lockmydevice.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.EditText;

import com.itexico.utilities.lockmydevice.R;
import com.itexico.utilities.lockmydevice.service.DeviceService;

/**
 * Created by iTexico Developer on 7/6/2016.
 */
public class BaseActivity extends AppCompatActivity {

    private IOnAlertDialogClickListener mIOnAlertDialogClickListener;

    public interface IOnAlertDialogClickListener {
        void onAlertDialogPositiveButtonClicked(final String textInput);
    }

    private DeviceService mDeviceService;

    // Flag indicating whether we have called bind on the service.
    boolean mBound;

    // Class for interacting with the main interface of the service.
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
            // This is called when the connection with the iBinder has been established, giving us the object we can use
            // to interact with the iBinder.  We are communicating with the iBinder using a Messenger, so here we get a
            // client-side representation of that from the raw IBinder object.
            DeviceService.DeviceServiceBinder deviceServiceBinder = (DeviceService.DeviceServiceBinder) iBinder;
            mDeviceService = deviceServiceBinder.getService();
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been unexpectedly disconnected -- that is,
            // its process crashed.
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service
//        bindService(new Intent(this, DeviceService.class), mConnection, Context.BIND_AUTO_CREATE);
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

    protected void showErrorBadCredentialsMessage(Context context,int message) {
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

    protected void showAlertDialogWithPositiveNegativeButton(int positiveButtonTitleId, int negativeButtonTitleId, int titleId, String message, IOnAlertDialogClickListener iOnAlertDialogClickListener)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        setIOnAlertDialogClickListener(iOnAlertDialogClickListener);
        alertDialogBuilder.setTitle(titleId);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setPositiveButton(getString(positiveButtonTitleId), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String userInputText = input.getText().toString();
                if (null != mIOnAlertDialogClickListener) {
                    mIOnAlertDialogClickListener.onAlertDialogPositiveButtonClicked(userInputText);
                }
            }
        });
        alertDialogBuilder.setNegativeButton(getString(negativeButtonTitleId), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    protected void showAlertDialogWithPositiveButton(int positiveButtonTitleId, int titleId, String message)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(titleId);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(positiveButtonTitleId), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    protected void setIOnAlertDialogClickListener(IOnAlertDialogClickListener iOnAlertDialogClickListener)
    {
        mIOnAlertDialogClickListener = iOnAlertDialogClickListener;
    }

}
