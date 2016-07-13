package com.itexico.utilities.lockmydevice.activities;

import android.os.Bundle;
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
        updateDeviceInfo();
    }

    private void updateDeviceInfo(){
        final TextView deviceIMEIView = (TextView)findViewById(R.id.device_imei);
        final TextView deviceModelView = (TextView)findViewById(R.id.device_model);
        final TextView deviceOSVersionView = (TextView)findViewById(R.id.device_os_version);
        final TextView deviceSerialNumberView = (TextView)findViewById(R.id.device_serial_number);
        final TextView deviceiTexicoIDView = (TextView)findViewById(R.id.device_itexico_id);
        final TextView deviceOwnerEmailIDView = (TextView)findViewById(R.id.device_owner_email_id);
        final TextView deviceOwnerUsernameView = (TextView)findViewById(R.id.device_owner_username);

        final String deviceIMEI = "IMEI number-93029302399203929";//String.format(getString(R.string.device_info_imei), DeviceMetaData.getDeviceIMEINumber(this))+",Type:"+DeviceMetaData.getDeviceType(this);
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

    @Override
    protected void onStart() {
        super.onStart();
    }
}
