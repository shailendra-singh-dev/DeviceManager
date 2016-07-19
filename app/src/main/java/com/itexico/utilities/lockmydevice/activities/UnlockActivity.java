package com.itexico.utilities.lockmydevice.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itexico.utilities.lockmydevice.R;
import com.itexico.utilities.lockmydevice.devicepackage.DevicePackageManager;
import com.itexico.utilities.lockmydevice.preferences.DevicePreferencesManager;
import com.itexico.utilities.lockmydevice.utils.AppUtils;
import com.itexico.utilities.lockmydevice.utils.DeviceMetaData;

public class UnlockActivity extends BaseActivity implements View.OnClickListener {

    private static final String UNLOCK_ACTIVITY = UnlockActivity.class.getCanonicalName();
    private static final String TAG = UnlockActivity.class.getSimpleName();
    private static final int READ_PHONE_STATE_PERMISSION_CODE = 10;
    private Button mUnlockButton;
    private LinearLayout mLogInSection;
    private EditText mEmailField;
    private EditText mPassField;
    private LinearLayout mUnlockSection;
    private TextView mUserNameLabel;
    private Button mYesIAmButton;
    private TextView mNoImNotButton;
    private boolean isPasswordToggle;
    private TextView mUserEmailIDLabel;

//    private View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.unlockButton:{
//                    if (ActivityCompat.checkSelfPermission(UnlockActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(UnlockActivity.this, Manifest.permission.READ_PHONE_STATE)) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(UnlockActivity.this);
//                            builder.setTitle("Requesting Read External Storage");
//                            builder.setMessage("This app requires accesss to External Storage to get the Video for editing..");
//                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface arg0, int arg1) {
//                                    ActivityCompat.requestPermissions(UnlockActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_CODE);
//                                }
//                            });
//                            builder.show();
//                        } else {
//                            ActivityCompat.requestPermissions(UnlockActivity.this,new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_CODE);
//                        }
//                    } else {
//                        Log.i(TAG, "AAAA validateUserAndUnLock(),permission granted");
//                        //openGalleryPicker();
//                    }
//                }
//                    break;
//                case R.id.yesIAm:
//                    unlock();
//                    break;
//                case R.id.noImNot:
//                    loginView();
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "AAAA onCreate()");
        hideSystemUI();

        setContentView(R.layout.activity_unlock);

        mUnlockButton = (Button) findViewById(R.id.unlockButton);
        mLogInSection = (LinearLayout) findViewById(R.id.logInSection);

        mEmailField = (EditText) findViewById(R.id.emailField);

        mPassField = (EditText) findViewById(R.id.passField);
        mPassField.setTypeface(Typeface.DEFAULT);
        mPassField.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mPassField.getRight() - mPassField.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (!isPasswordToggle) {
                            mPassField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordToggle = true;
                        } else {
                            mPassField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isPasswordToggle = false;
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        mUnlockSection = (LinearLayout) findViewById(R.id.unlockSection);
        mUserNameLabel = (TextView) findViewById(R.id.userNameLabel);
        mUserEmailIDLabel = (TextView) findViewById(R.id.user_email_id);
        mYesIAmButton = (Button) findViewById(R.id.yesIAm);
        mNoImNotButton = (TextView) findViewById(R.id.noImNot);

        mUnlockButton.setOnClickListener(this);
        mYesIAmButton.setOnClickListener(this);
        mNoImNotButton.setOnClickListener(this);

        String user = getActiveUser();
        if (user != null && user.length() > 0) {
            unlockView(user, user);
        } else {
            loginView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "AAAA onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "AAAA onPause()");
    }

    private void loginView() {
        mLogInSection.setVisibility(View.VISIBLE);
        mUnlockSection.setVisibility(View.GONE);

        removeUser();
    }

    private void unlockView(String user, String emailID) {
        mLogInSection.setVisibility(View.GONE);
        mUnlockSection.setVisibility(View.VISIBLE);

        mUserNameLabel.setText(getString(R.string.hello) + " " + user + getString(R.string.hello_user_exclamatorty_mark));
        mUserEmailIDLabel.setText(emailID);
    }

    private String getActiveUser() {
        return DevicePreferencesManager.getString(this, DevicePreferencesManager.SHAREDPREFERENCE_KEY.KEY_USER);
    }

    private String getActiveUserPass() {
        return DevicePreferencesManager.getString(this, DevicePreferencesManager.SHAREDPREFERENCE_KEY.KEY_PASSWORD);
    }

//    public void validateUserAndUnLock() {
//        if (isFieldsValid()) {
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
//                int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//                Log.i(TAG, "AAAA validateUserAndUnLock(),permissionCheck:" + permissionCheck);
//                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
//                        showAlertDialogWithPositiveButton(R.string.dialog_postive_buttton_id, R.string.permission_dialog_title, getString(R.string.permission_dialog_message), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ActivityCompat.requestPermissions(UnlockActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_CODE);
//                            }
//                        });
//                    } else {
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_CODE);
//                    }
//                } else {
//                    unlockAndAddUser();
//                }
//            } else {
//                unlockAndAddUser();
//            }
//        }
//    }

    public void openGallery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Requesting Read External Storage");
                builder.setMessage("This app requires accesss to External Storage to get the Video for editing..");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ActivityCompat.requestPermissions(UnlockActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_CODE);
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_CODE);
            }
        } else {
            Log.i(TAG, "AAAA validateUserAndUnLock(),permission granted");
            //openGalleryPicker();
        }
    }




    private void unlockAndAddUser() {
        Log.i(TAG, "AAAA unlockAndAddUser()...");
        unlock();
        if (mBound) {
            mDeviceService.saveCredentials(mEmailField.getText().toString(), mPassField.getText().toString());
        }
    }

    private void removeUser() {
        DevicePreferencesManager.removeString(this, DevicePreferencesManager.SHAREDPREFERENCE_KEY.KEY_USER);
    }

    private void unlock() {
        finish();
        if (mBound) {
            mDeviceService.launchDefaultLauncher(this);
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.i(TAG, "AAAA onUserLeaveHint()");
        final boolean isMyAppLauncherDefault = DevicePackageManager.getInstance().isMyAppLauncherDefault(this);
        Log.i(TAG, "AAAA unlock(), isMyAppLauncherDefault:" + isMyAppLauncherDefault);
        if (DevicePackageManager.getInstance().isMyAppLauncherDefault(this) && DevicePackageManager.getInstance().isComponentEnabled(this, UnlockActivity.class)) {
            Intent intent = new Intent().addCategory(Intent.CATEGORY_HOME).setAction(Intent.ACTION_MAIN).
                    setClassName(getPackageName(), getPackageName() + "." + UnlockActivity.class.getSimpleName());
            Log.i(TAG, "AAAA onUserLeaveHint(),DevicePackageManager.getInstance().isMyAppLauncherDefault");
            startActivity(intent);
        } else {
            Log.i(TAG, "AAAA onUserLeaveHint(),DevicePackageManager.getInstance().resetPreferredLauncherAndOpenChooser()");
            DevicePackageManager.getInstance().resetPreferredLauncherAndOpenChooser(this, UnlockActivity.class);
        }
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public boolean isFieldsValid() {
        final String emailId = mEmailField.getText().toString();
        final String password = mPassField.getText().toString();
        int messageId = 0;

        boolean valid = true;
        if (!(emailId.length() > 0)) {
            valid = false;
            messageId = R.string.dialog_error_email_field_empty;
        }
        if (!AppUtils.isValidEmail(emailId)) {
            valid = false;
            messageId = R.string.dialog_error_email_field_invalid;
        }
        if (!(password.length() > 0)) {
            valid = false;
            messageId = R.string.dialog_error_password_field_empty;
        }
        if (!valid) {
            showAlertDialogWithPositiveButton(R.string.dialog_postive_buttton_id, R.string.dialog_title, getString(messageId), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "AAAA Disabled Back click...");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG,"onRequestPermissionsResult() requestCode:"+requestCode);
        switch (requestCode) {
            case READ_PHONE_STATE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    unlockAndAddUser();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.unlockButton:{
                if (ActivityCompat.checkSelfPermission(UnlockActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(UnlockActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UnlockActivity.this);
                        builder.setTitle("Requesting Read External Storage");
                        builder.setMessage("This app requires access to External Storage to get the Video for editing..");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                ActivityCompat.requestPermissions(UnlockActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_CODE);
                            }
                        });
                        builder.show();
                    } else {
                        ActivityCompat.requestPermissions(UnlockActivity.this,new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_CODE);
                    }
                } else {
                    //Can't use IMEI since for Tablets will not have this..And we can get only for Phone which has SIM card on it. And it requires from user on 6.0 Above devices.
                    String deviceSerialNumber =DeviceMetaData.getDeviceSerialNumber();
                    Log.i(TAG, "AAAA deviceID:"+deviceSerialNumber);
                    unlockAndAddUser();
                }
            }
            break;
            case R.id.yesIAm:
                unlock();
                break;
            case R.id.noImNot:
                loginView();
                break;
        }
    }


}
