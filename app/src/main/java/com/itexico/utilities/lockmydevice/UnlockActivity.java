package com.itexico.utilities.lockmydevice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UnlockActivity extends AppCompatActivity {

    private static final String USER = "USER";
    private static final String PASS = "PASS";
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

    private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch ( v.getId() ){
                case R.id.unlockButton:
                    addUser();
                case R.id.yesIAm:
                    unlock();
                    break;
                case R.id.noImNot:
                    loginView();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();

        setContentView(R.layout.activity_unlock);

        unlockButton = (Button) findViewById(R.id.unlockButton);
        logInSection = (LinearLayout) findViewById(R.id.logInSection);

        emailField = (EditText)findViewById(R.id.emailField);

        passField = (EditText)findViewById(R.id.passField);
        passField.setTypeface(Typeface.DEFAULT);
        passField.setTransformationMethod(new PasswordTransformationMethod());

        unlockSection = (LinearLayout) findViewById(R.id.unlockSection);
        userNameLabel = (TextView)findViewById(R.id.userNameLabel);
        yesIAm = (Button)findViewById(R.id.yesIAm);
        noImNot = (Button)findViewById(R.id.noImNot);

        unlockButton.setOnClickListener(listener);
        yesIAm.setOnClickListener(listener);
        noImNot.setOnClickListener(listener);

        String user = getActiveUser();
        if( user != null && user.length() > 0 ){
            unlockView( user );
        } else {
            loginView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void loginView(){
        logInSection.setVisibility(View.VISIBLE);
        unlockSection.setVisibility(View.GONE);

        removeUser();
    }

    private void unlockView( String user ){
        logInSection.setVisibility(View.GONE);
        unlockSection.setVisibility(View.VISIBLE);

        userNameLabel.setText("ARE YOU " + user + " ? ");
    }

    private String getActiveUser(){
        SharedPreferences sharedPref = getSharedPreferences(UNLOCK_ACTIVITY, Context.MODE_PRIVATE);

        String user = sharedPref.getString(USER, "");
        return user;
    }

    private String getActiveUserPass(){
        SharedPreferences sharedPref = getSharedPreferences(UNLOCK_ACTIVITY, Context.MODE_PRIVATE);

        String pass = sharedPref.getString(PASS, "");
        return pass;
    }

    private void addUser(){
        if(isFieldsValidated()){
            addUser(emailField.getText().toString(), passField.getText().toString());
        }
    }

    private void addUser( String user, String pass){
        SharedPreferences sharedPref = getSharedPreferences(UNLOCK_ACTIVITY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER, user);
        editor.putString(PASS, pass);
        editor.commit();
    }

    private void removeUser(){
        SharedPreferences sharedPref = getSharedPreferences(UNLOCK_ACTIVITY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(USER);
        editor.remove(PASS);
        editor.commit();
    }

    private void unlock(){
        this.finish();
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

    public boolean isFieldsValidated(){
        final String emailId = emailField.getText().toString();
        final String password = passField.getText().toString();

        boolean validated = true;
        emailField.setError(null);
        passField.setError(null);
        if (!(emailId.length() > 0)) {
            emailField.setError(getString(R.string.error_email_field_empty));
            emailField.requestFocus();
            validated = false;
        }
        if (AppUtils.isValidEmail(emailId)) {
            emailField.setError(getString(R.string.error_email_field_invalid));
            emailField.requestFocus();
            validated = false;
        }
        if (!(password.length() > 0)) {
            passField.setError(getString(R.string.error_password_field_empty));
            passField.requestFocus();
            validated = false;
        }
        return validated;
    }
}
