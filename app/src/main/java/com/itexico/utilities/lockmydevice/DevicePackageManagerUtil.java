package com.itexico.utilities.lockmydevice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iTexico Developer on 6/29/2016.
 */
public class DevicePackageManagerUtil {

    private DevicePackageManagerUtil(){
    }

    private static final String TAG = DevicePackageManagerUtil.class.getSimpleName();

    /**
     * method checks to see if app is currently set as default launcher
     * @return boolean true means currently set as default, otherwise false
     */
    public static boolean isMyAppLauncherDefault(final Context context) {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);

        List<IntentFilter> filters = new ArrayList<IntentFilter>();
        filters.add(filter);

        final String myPackageName = context.getPackageName();
        List<ComponentName> activities = new ArrayList<ComponentName>();
        final PackageManager packageManager = (PackageManager) context.getPackageManager();

        packageManager.getPreferredActivities(filters, activities, null);

        for (ComponentName activity : activities) {
            if (myPackageName.equals(activity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * method starts an intent that will bring up a prompt for the user
     * to select their default launcher. It comes up each time it is
     * detected that our app is not the default launcher
     */
    public static void launchAppChooser(final Context context) {
        Log.d(TAG, "launchAppChooser()");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void resetPreferredLauncherAndOpenChooser(Context context, Class componentClassName) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, componentClassName);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(selector);

        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }

    public static void launchDefaultLauncher(final Context context) {
//        getPackageManager().clearPackagePreferredActivities(getPackageName());
//        final Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(intent);

        Intent intent = null;
        final PackageManager packageManager = context.getPackageManager();
        for (final ResolveInfo resolveInfo : packageManager.queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), PackageManager.MATCH_DEFAULT_ONLY)) {
            if (!context.getPackageName().equals(resolveInfo.activityInfo.packageName))  //if this activity is not in our activity (in other words, it's another default home screen)
            {
//                intent = packageManager.getLaunchIntentForPackage(resolveInfo.activityInfo.packageName);
                intent = new Intent().addCategory(Intent.CATEGORY_HOME).setAction(Intent.ACTION_MAIN).setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                Log.i(TAG, "Matched package is " + resolveInfo.activityInfo.packageName+",resolveInfo.activityInfo.name:"+resolveInfo.activityInfo.name);
                break;
            }
        }
        context.startActivity(intent);
    }
}
