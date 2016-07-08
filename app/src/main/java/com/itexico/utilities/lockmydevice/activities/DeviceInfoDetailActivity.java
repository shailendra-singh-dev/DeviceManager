package com.itexico.utilities.lockmydevice.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.itexico.utilities.lockmydevice.R;
import com.itexico.utilities.lockmydevice.utils.DeviceMetaData;

/**
 * Created by iTexico Developer on 7/7/2016.
 */
public class DeviceInfoDetailActivity extends BaseActivity {

    private static final int REQUEST_READ_PHONE_STATE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_info_details);
        openDeviceInfo();
    }

    private void updateDeviceInfo(){
        final TextView deviceIMEIView = (TextView)findViewById(R.id.device_imei);
        final TextView deviceModelView = (TextView)findViewById(R.id.device_model);
        final TextView deviceOSVersionView = (TextView)findViewById(R.id.device_os_version);
        final TextView deviceSerialNumberView = (TextView)findViewById(R.id.device_serial_number);
        final TextView deviceiTexicoIDView = (TextView)findViewById(R.id.device_itexico_id);
        final TextView deviceOwnerEmailIDView = (TextView)findViewById(R.id.device_owner_email_id);
        final TextView deviceOwnerUsernameView = (TextView)findViewById(R.id.device_owner_username);

        final String deviceIMEI = String.format(getString(R.string.device_info_imei), DeviceMetaData.getDeviceIMEINumber(this))+",Type:"+DeviceMetaData.getDeviceType(this);
        final String deviceModel = String.format(getString(R.string.device_info_model), DeviceMetaData.getDeviceModelNumber());
        final String deviceOSVersion = String.format(getString(R.string.device_info_os_version), DeviceMetaData.getDeviceOSVersion());
        final String deviceSerialNumber = String.format(getString(R.string.device_info_serial_number), DeviceMetaData.getDeviceSerialNumber());
        final String deviceiTexicoID = String.format(getString(R.string.device_info_itexico_id), "iTexico7382732h938203");
        final String deviceOwnerEmailID = String.format(getString(R.string.device_info_owner_email_id), "ssingh@itexico.net");
        final String deviceOwnerUsername = String.format(getString(R.string.device_info_owner_username), "Shailendra Singh");

        deviceIMEIView.setText(deviceIMEI);
        deviceModelView.setText(deviceModel);
        deviceOSVersionView.setText(deviceOSVersion);
        deviceSerialNumberView.setText(deviceSerialNumber);
        deviceiTexicoIDView.setText(deviceiTexicoID);
        deviceOwnerEmailIDView.setText(deviceOwnerEmailID);
        deviceOwnerUsernameView.setText(deviceOwnerUsername);
    }

    public void openDeviceInfo() {
        final String[] permissionsForCameraRecording = new String[]{Manifest.permission.READ_PHONE_STATE};
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                showAlertDialogWithPositiveButton(R.string.dialog_postive_buttton_id,R.string.dialog_title,getString(R.string.dialog_message));
            } else {
                ActivityCompat.requestPermissions(DeviceInfoDetailActivity.this, permissionsForCameraRecording, REQUEST_READ_PHONE_STATE);
            }
        } else {
            updateDeviceInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_READ_PHONE_STATE:
                updateDeviceInfo();
                break;
        }
    }
}
