package com.itexico.utilities.lockmydevice.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
    private Button unlockButton;
    private LinearLayout logInSection;
    private EditText emailField;
    private EditText passField;
    private LinearLayout unlockSection;
    private TextView userNameLabel;
    private Button yesIAm;
    private Button noImNot;


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.unlockButton:
                    addUser();
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

        unlockButton = (Button) findViewById(R.id.unlockButton);
        logInSection = (LinearLayout) findViewById(R.id.logInSection);

        emailField = (EditText) findViewById(R.id.emailField);

        passField = (EditText) findViewById(R.id.passField);
        passField.setTypeface(Typeface.DEFAULT);
        passField.setTransformationMethod(new PasswordTransformationMethod());

        unlockSection = (LinearLayout) findViewById(R.id.unlockSection);
        userNameLabel = (TextView) findViewById(R.id.userNameLabel);
        yesIAm = (Button) findViewById(R.id.yesIAm);
        noImNot = (Button) findViewById(R.id.noImNot);

        unlockButton.setOnClickListener(listener);
        yesIAm.setOnClickListener(listener);
        noImNot.setOnClickListener(listener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "AAAA onResume()");
        String user = getActiveUser();
        if (user != null && user.length() > 0) {
            unlockView(user);
        } else {
            loginView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "AAAA onPause()");
    }

    private void loginView() {
        logInSection.setVisibility(View.VISIBLE);
        unlockSection.setVisibility(View.GONE);

        removeUser();
    }

    private void unlockView(String user) {
        logInSection.setVisibility(View.GONE);
        unlockSection.setVisibility(View.VISIBLE);

        userNameLabel.setText("ARE YOU " + user + " ? ");
    }

    private String getActiveUser() {
        return DevicePreferencesManager.getString(this, DevicePreferencesManager.SHAREDPREFERENCE_KEY.KEY_USER);
    }

    private String getActiveUserPass() {
        return DevicePreferencesManager.getString(this, DevicePreferencesManager.SHAREDPREFERENCE_KEY.KEY_PASSWORD);
    }

    private void addUser() {
        if (isFieldsValid()) {
            addUser(emailField.getText().toString(), passField.getText().toString());
            unlock();
        }
    }

    private void addUser(String user, String pass) {
        DevicePreferencesManager.setString(this, DevicePreferencesManager.SHAREDPREFERENCE_KEY.KEY_USER, user);
        DevicePreferencesManager.setString(this, DevicePreferencesManager.SHAREDPREFERENCE_KEY.KEY_PASSWORD, pass);
    }

    private void removeUser() {
        DevicePreferencesManager.removeString(this, DevicePreferencesManager.SHAREDPREFERENCE_KEY.KEY_USER);
    }

    private void unlock() {
        finish();
        DevicePackageManager.getInstance().launchDefaultLauncher(this);
        final boolean isMyAppLauncherDefault = DevicePackageManager.getInstance().isMyAppLauncherDefault(this);
        Log.i(TAG, "AAAA unlock(), isMyAppLauncherDefault:" + isMyAppLauncherDefault);
        if(isMyAppLauncherDefault) {
            DevicePackageManager.getInstance().setMyLauncherComponentState(this, UnlockActivity.class, false);
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.i(TAG, "AAAA onUserLeaveHint()");
        final boolean isMyAppLauncherDefault = DevicePackageManager.getInstance().isMyAppLauncherDefault(this);
        Log.i(TAG, "AAAA unlock(), isMyAppLauncherDefault:" + isMyAppLauncherDefault);
        if(DevicePackageManager.getInstance().isMyAppLauncherDefault(this) && DevicePackageManager.getInstance().isComponentEnabled(this,UnlockActivity.class)){
            Intent intent = new Intent().addCategory(Intent.CATEGORY_HOME).setAction(Intent.ACTION_MAIN).
                    setClassName(getPackageName(), getPackageName() + "." + UnlockActivity.class.getSimpleName());
            Log.i(TAG, "AAAA onUserLeaveHint(),DevicePackageManager.getInstance().isMyAppLauncherDefault");
            startActivity(intent);
        }else{
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
        final String emailId = emailField.getText().toString();
        final String password = passField.getText().toString();

        boolean valid = true;
        if (!(emailId.length() > 0)) {
            valid = false;
            showErrorBadCredentialsMessage(this, R.string.error_email_field_empty);
        }
        if (!AppUtils.isValidEmail(emailId)) {
            valid = false;
            showErrorBadCredentialsMessage(this, R.string.error_email_field_invalid);
        }
        if (!(password.length() > 0)) {
            valid = false;
            showErrorBadCredentialsMessage(this, R.string.error_password_field_empty);
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "AAAA Disabled Back click...");
    }

}
