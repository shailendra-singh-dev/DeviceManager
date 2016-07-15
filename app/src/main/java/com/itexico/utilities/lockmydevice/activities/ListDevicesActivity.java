package com.itexico.utilities.lockmydevice.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.itexico.utilities.lockmydevice.R;
import com.itexico.utilities.lockmydevice.adapter.DevicesListAdapter;
import com.itexico.utilities.lockmydevice.devicepackage.DevicePackageManager;
import com.itexico.utilities.lockmydevice.interfaces.OnItemClickListener;
import com.itexico.utilities.lockmydevice.interfaces.RecycleViewItemTouchListener;
import com.itexico.utilities.lockmydevice.model.DeviceInfo;

import java.util.ArrayList;

/**
 * Created by iTexico Developer on 7/5/2016.
 */
public class ListDevicesActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = ListDevicesActivity.class.getSimpleName();
    private MenuItem mMenuItem;
    private RecyclerView mRecyclerView;
    private DevicesListAdapter mDevicesListAdapter;
    private ArrayList<DeviceInfo> mDeviceInfos;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_devices);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.string_empty);
        setSupportActionBar(toolbar);

//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);

        if (DevicePackageManager.getInstance().isMyAppLauncherDefault(this)) {
            DevicePackageManager.getInstance().setMyLauncherComponentState(this, UnlockActivity.class, false);
        }
        handleIntent(getIntent());

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        prepareDataset();
        mRecyclerView.setHasFixedSize(true);
        mDevicesListAdapter = new DevicesListAdapter(mRecyclerView);
        mDevicesListAdapter.setDataset(mDeviceInfos);
        mRecyclerView.setAdapter(mDevicesListAdapter);

        mRecyclerView.addOnItemTouchListener(new RecycleViewItemTouchListener(this,
                        new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                // close search view if its visible
                                if (searchView.isShown()) {
                                    mMenuItem.collapseActionView();
                                    searchView.setQuery("", false);
                                }
                                final DeviceInfo deviceInfo = mDeviceInfos.get(position);
                                final Intent intent = new Intent(ListDevicesActivity.this, DeviceInfoDetailActivity.class);
                                ListDevicesActivity.this.startActivity(intent);
                                ListDevicesActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }
                )
        );
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_devices, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mMenuItem = menu.findItem(R.id.menu_item_search);
        searchView = (SearchView) MenuItemCompat.getActionView(mMenuItem);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), ListDevicesActivity.class)));

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handles ToolBar backArrow Navigation...
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG, "handleIntent(),query:" + query);
            mDevicesListAdapter.getFilter().filter(query);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i(TAG, "onQueryTextSubmit(),query:" + query);
        mDevicesListAdapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i(TAG, "onQueryTextSubmit(),query:" + newText);
        mDevicesListAdapter.getFilter().filter(newText);
        return true;
    }


    private void prepareDataset() {
        mDeviceInfos = new ArrayList<DeviceInfo>();

        DeviceInfo deviceInfoNexus5 = new DeviceInfo();
        deviceInfoNexus5.setDeviceIMEI("UI7292932398");
        deviceInfoNexus5.setDeviceModel("Nexus 5");
        deviceInfoNexus5.setDeviceiTexicoID("i0001");
        deviceInfoNexus5.setDeviceOwnerEmailID("user1222@itexico.net");

        DeviceInfo deviceInfoNexus4 = new DeviceInfo();
        deviceInfoNexus4.setDeviceIMEI("UI7292932398");
        deviceInfoNexus4.setDeviceModel("Nexus 4");
        deviceInfoNexus4.setDeviceiTexicoID("i00016");
        deviceInfoNexus4.setDeviceOwnerEmailID("user454@itexico.net");

        DeviceInfo deviceInfoSamsungS5 = new DeviceInfo();
        deviceInfoSamsungS5.setDeviceIMEI("UI7292932398");
        deviceInfoSamsungS5.setDeviceModel("Samsung S5");
        deviceInfoSamsungS5.setDeviceiTexicoID("i00034");
        deviceInfoSamsungS5.setDeviceOwnerEmailID("user445@itexico.net");

        DeviceInfo motorolla = new DeviceInfo();
        motorolla.setDeviceIMEI("UI7292932398");
        motorolla.setDeviceModel("Samsung S5");
        motorolla.setDeviceiTexicoID("i00022");
        motorolla.setDeviceOwnerEmailID("user188@itexico.net");

        for (int i = 0; i < 50; i++) {
            if (i % 2 == 0) {
                mDeviceInfos.add(deviceInfoSamsungS5);
            } else if (i % 3 == 0) {
                mDeviceInfos.add(deviceInfoNexus4);
            } else if (i % 5 == 0) {
                mDeviceInfos.add(deviceInfoNexus5);
            } else if (i % 7 == 0) {
                mDeviceInfos.add(motorolla);
            }
        }
    }

}
