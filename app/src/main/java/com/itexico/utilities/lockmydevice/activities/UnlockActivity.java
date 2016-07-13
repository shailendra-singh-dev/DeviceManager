package com.itexico.utilities.lockmydevice.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itexico.utilities.lockmydevice.R;
import com.itexico.utilities.lockmydevice.devicepackage.DevicePackageManager;
import com.itexico.utilities.lockmydevice.preferences.DevicePreferencesManager;
import com.itexico.utilities.lockmydevice.utils.AppUtils;

public class UnlockActivity extends BaseActivity {

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
    private Button mNoImNotButton;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.unlockButton:
                    validateUserAndUnLock();
                    break;
                case R.id.yesIAm:
                    unlock();
                    break;
                case R.id.noImNot:
                    loginView();
                    break;
            }
        }
    };

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
        mPassField.setTransformationMethod(new PasswordTransformationMethod());

        mUnlockSection = (LinearLayout) findViewById(R.id.unlockSection);
        mUserNameLabel = (TextView) findViewById(R.id.userNameLabel);
        mYesIAmButton = (Button) findViewById(R.id.yesIAm);
        mNoImNotButton = (Button) findViewById(R.id.noImNot);

        mUnlockButton.setOnClickListener(listener);
        mYesIAmButton.setOnClickListener(listener);
        mNoImNotButton.setOnClickListener(listener);

        String user = getActiveUser();
        if (user != null && user.length() > 0) {
            unlockView(user);
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

    private void unlockView(String user) {
        mLogInSection.setVisibility(View.GONE);
        mUnlockSection.setVisibility(View.VISIBLE);

        mUserNameLabel.setText("ARE YOU " + user + " ? ");
    }

    private String getActiveUser() {
        return DevicePreferencesManager.getString(this, DevicePreferencesManager.SHAREDPREFERENCE_KEY.KEY_USER);
    }

    private String getActiveUserPass() {
        return DevicePreferencesManager.getString(this, DevicePreferencesManager.SHAREDPREFERENCE_KEY.KEY_PASSWORD);
    }

    public void validateUserAndUnLock() {
        if (isFieldsValid()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                    showAlertDialogWithPositiveButton(R.string.dialog_postive_buttton_id, R.string.permission_dialog_title, getString(R.string.permission_dialog_message), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UnlockActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_CODE);
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_CODE);
                }
            } else {
                unlockAndAddUser();
            }
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
        switch (requestCode) {
            case READ_PHONE_STATE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    unlockAndAddUser();
                }

                break;
        }
    }

}
