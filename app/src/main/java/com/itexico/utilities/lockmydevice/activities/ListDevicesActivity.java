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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.itexico.utilities.lockmydevice.R;
import com.itexico.utilities.lockmydevice.devicepackage.DevicePackageManager;
import com.itexico.utilities.lockmydevice.interfaces.OnItemClickListener;
import com.itexico.utilities.lockmydevice.model.DeviceInfo;
import com.itexico.utilities.lockmydevice.views.DevicesListAdapter;
import com.itexico.utilities.lockmydevice.views.RecycleViewItemTouchListener;

import java.util.ArrayList;

/**
 * Created by iTexico Developer on 7/5/2016.
 */
public class ListDevicesActivity extends BaseActivity implements SearchView.OnQueryTextListener   {

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
        if(DevicePackageManager.getInstance().isMyAppLauncherDefault(this)){
            DevicePackageManager.getInstance().setMyLauncherComponentState(this,UnlockActivity.class,false);
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
                                final Intent intent = new Intent(ListDevicesActivity.this,DeviceInfoDetailActivity.class);
                                ListDevicesActivity.this.startActivity(intent);
                                ListDevicesActivity.this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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

        DeviceInfo deviceInfoNexus4 = new DeviceInfo();
        deviceInfoNexus4.setDeviceIMEI("UI7292932398");
        deviceInfoNexus4.setDeviceModel("Nexus 4");

        DeviceInfo deviceInfoSamsungS5 = new DeviceInfo();
        deviceInfoSamsungS5.setDeviceIMEI("UI7292932398");
        deviceInfoSamsungS5.setDeviceModel("Samsung S5");

        for (int i = 0; i < 50; i++) {
            if (i % 2 == 0) {
                mDeviceInfos.add(deviceInfoSamsungS5);
            } else {
                mDeviceInfos.add(deviceInfoNexus4);
            }
        }
    }

}
