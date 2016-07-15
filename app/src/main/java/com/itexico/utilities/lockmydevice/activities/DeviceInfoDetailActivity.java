package com.itexico.utilities.lockmydevice.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.itexico.utilities.lockmydevice.R;
import com.itexico.utilities.lockmydevice.utils.AppUtils;
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
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        updateDeviceInfo();
    }

    private void updateDeviceInfo(){
        final TextView deviceTypeView = (TextView)findViewById(R.id.device_info_details_device_type_value);
        final TextView deviceModelView = (TextView)findViewById(R.id.device_info_details_model_number);
        final TextView deviceOSVersionView = (TextView)findViewById(R.id.device_info_details_os_version_value);
        final TextView deviceSerialNumberView = (TextView)findViewById(R.id.device_info_details_serial_number_value);
        final TextView deviceiTexicoIDView = (TextView)findViewById(R.id.device_info_details_itexico_id_value);
        final TextView deviceLastLoginView = (TextView)findViewById(R.id.device_info_details_last_login);

        final String deviceIMEI = DeviceMetaData.getDeviceType(this);
        final String deviceModel = DeviceMetaData.getDeviceModelNumber();
        final String deviceOSVersion = DeviceMetaData.getDeviceOSVersion();
        final String deviceSerialNumber = DeviceMetaData.getDeviceSerialNumber();
        final String deviceiTexicoID = "iTexico7382732h938203";
        final String deviceOwnerEmailID = "ssingh@itexico.net";

        deviceTypeView.setText(deviceIMEI);
        deviceModelView.setText(deviceModel);
        deviceOSVersionView.setText(deviceOSVersion);
        deviceSerialNumberView.setText(deviceSerialNumber);
        deviceiTexicoIDView.setText(deviceiTexicoID);
        String deviceLastLogin = String.format(getString(R.string.device_info_last_login), deviceOwnerEmailID,
                AppUtils.getDateFormattedTime(System.currentTimeMillis()));
        deviceLastLoginView.setText(deviceLastLogin);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Handles ToolBar backArrow Navigation...
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
