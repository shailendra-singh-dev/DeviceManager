package com.itexico.utilities.lockmydevice.devicepackage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.itexico.utilities.lockmydevice.activities.UnlockActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iTexico Developer on 6/29/2016.
 */
public class DevicePackageManager {

    private  final String TAG = DevicePackageManager.class.getSimpleName();

    final private static DevicePackageManager DEVICE_PACKAGE_MANAGER = new DevicePackageManager();

    private DevicePackageManager() {
    }

     synchronized public static DevicePackageManager getInstance() {
        return DEVICE_PACKAGE_MANAGER;
    }

    /**
     * method checks to see if app is currently set as default launcher
     *
     * @return boolean true means currently set as default, otherwise false
     */
    public  boolean isMyAppLauncherDefault(final Context context) {
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
    public  void launchAppChooser(final Context context) {
        Log.d(TAG, "AAAA launchAppChooser()");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public  void resetPreferredLauncherAndOpenChooser(Context context, Class componentClassName) {
        Log.i(TAG, "AAAA resetPreferredLauncherAndOpenChooser()");
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, componentClassName);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(selector);

        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }

    public  void setMyLauncherComponentState(Context context, Class componentClassName, boolean isEnable) {
        Log.i(TAG, "AAAA setMyLauncherComponentState(),isEnable:" + isEnable);
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, componentClassName);
        if (isEnable) {
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
        } else {
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
    }

    public  void launchDefaultLauncher(final Context context) {
        Log.i(TAG, "AAAA launchDefaultLauncher()");

        Intent intent = null;
        final PackageManager packageManager = context.getPackageManager();
        for (final ResolveInfo resolveInfo : packageManager.queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), PackageManager.MATCH_DEFAULT_ONLY)) {
            if (!context.getPackageName().equals(resolveInfo.activityInfo.packageName))  //if this activity is not in our activity (in other words, it's another default home screen)
            {
//                intent = packageManager.getLaunchIntentForPackage(resolveInfo.activityInfo.packageName);
                intent = new Intent().addCategory(Intent.CATEGORY_HOME).setAction(Intent.ACTION_MAIN).setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                Log.i(TAG, "AAAA Matched package is " + resolveInfo.activityInfo.packageName + ",resolveInfo.activityInfo.name:" + resolveInfo.activityInfo.name);
                break;
            }
        }
        context.startActivity(intent);
    }

    public boolean isComponentEnabled(final Context context, Class<?> activityClass){
        final ComponentName componentName = new ComponentName(context, activityClass);
        final PackageManager pm = context.getPackageManager();
        final int result = pm.getComponentEnabledSetting(componentName);
        return result == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    public  void enableDefaultLauncher(final Context context, boolean isEnable) {
        Log.i(TAG, "AAAA enableDefaultLauncher(),isEnable:" + isEnable);
        Intent intent = null;
        final PackageManager packageManager = context.getPackageManager();
        packageManager.clearPackagePreferredActivities("com.android.launcher");
        String defaultLauncherPackageName = "";

        for (final ResolveInfo resolveInfo : packageManager.queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), PackageManager.MATCH_DEFAULT_ONLY)) {
            if (!context.getPackageName().equals(resolveInfo.activityInfo.packageName))  //if this activity is not in our activity (in other words, it's another default home screen)
            {
                defaultLauncherPackageName = resolveInfo.activityInfo.packageName;
                Log.i(TAG, "AAAA enableDefaultLauncher(),defaultLauncherPackageName:" + defaultLauncherPackageName + "," +
                        "resolveInfo.activityInfo.packageName:" + resolveInfo.activityInfo.packageName + ",resolveInfo.activityInfo.name:" + resolveInfo.activityInfo.name);
                break;
            }
        }
        if (isEnable) {
            packageManager.setApplicationEnabledSetting(defaultLauncherPackageName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        } else {
            packageManager.setApplicationEnabledSetting(defaultLauncherPackageName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
    }

    public  void makePreferred(final Context context) {
        PackageManager packageManager = context.getPackageManager();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
        intentFilter.addCategory(Intent.CATEGORY_HOME);
        intentFilter.addCategory(Intent.CATEGORY_LAUNCHER);

        ComponentName component = new ComponentName(context.getPackageName(), UnlockActivity.class.getName());

        ComponentName[] components = new ComponentName[]{new ComponentName("com.android.launcher", "com.android.launcher.Launcher"), component};

        packageManager.clearPackagePreferredActivities("com.android.launcher");
        packageManager.addPreferredActivity(intentFilter, IntentFilter.MATCH_CATEGORY_EMPTY, components, component);
    }


    // Requires permission SET_PREFERRED_APPLICATIONS.
    public  boolean setPreferredHomeActivity(Context context, String packageName, String className) {
        ComponentName oldPreferredActivity = getPreferredHomeActivity(context);
        if (oldPreferredActivity != null && packageName.equals(oldPreferredActivity.getPackageName()) && className.equals(oldPreferredActivity.getClassName())) {
            return false;
        }
        if (oldPreferredActivity != null) {
            context.getPackageManager().clearPackagePreferredActivities(oldPreferredActivity.getPackageName()); }
        IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        ComponentName[] currentHomeActivities = getActivitiesListByActionAndCategory(context, Intent.ACTION_MAIN, Intent.CATEGORY_HOME);
        ComponentName newPreferredActivity = new ComponentName(packageName, className);
        context.getPackageManager().addPreferredActivity(filter, IntentFilter.MATCH_CATEGORY_EMPTY, currentHomeActivities, newPreferredActivity);
        return true;
    }

    private  ComponentName getPreferredHomeActivity(Context context) {
        ArrayList<IntentFilter> filters = new ArrayList<>();
        List<ComponentName> componentNames = new ArrayList<>();
        context.getPackageManager().getPreferredActivities(filters, componentNames, null);
        for (int i = 0; i < filters.size(); i++) {
            IntentFilter filter = filters.get(i);
            if (filter.hasAction(Intent.ACTION_MAIN) && filter.hasCategory(Intent.CATEGORY_HOME)) {
                return componentNames.get(i);
            }
        }
        return null;
    }

    private  ComponentName[] getActivitiesListByActionAndCategory(Context context, String action, String category) {
        Intent queryIntent = new Intent(action);
        queryIntent.addCategory(category);
        List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(queryIntent, PackageManager.MATCH_DEFAULT_ONLY);
        ComponentName[] componentNames = new ComponentName[resInfos.size()];
        for (int i = 0; i < resInfos.size(); i++) {
            ActivityInfo activityInfo = resInfos.get(i).activityInfo;
            componentNames[i] = new ComponentName(activityInfo.packageName, activityInfo.name);
        }
        return componentNames;
    }
}
