package com.itexico.utilities.lockmydevice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by iTexico Developer on 6/29/2016.
 */
public class DevicePreferencesManager {

    private static final String USER = "USER";
    private static final String PASS = "PASS";

    public enum SHAREDPREFERENCE_KEY {
        KEY_USER(USER),KEY_PASSWORD(PASS);

        private SHAREDPREFERENCE_KEY(final Object defaultValue) {
            mDefaultValue = defaultValue;
        }

        public Object getDefault() {
            return mDefaultValue;
        }

        private final Object mDefaultValue;
    }

    private DevicePreferencesManager() {
    }

    private static SharedPreferences getSharedPreferences(final Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences;
    }

    private static SharedPreferences.Editor getEditor(final Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.edit();
    }

    public static void setString(final Context context, final SHAREDPREFERENCE_KEY key, final String value) {
        final Object defaultValue = key.getDefault();
        if ((defaultValue == null) || (defaultValue instanceof String)) {
            getEditor(context).putString(key.name(), value).commit();
        } else {
            throw new IllegalArgumentException("Can not store String value in " + key.name());
        }
    }

    public static String getString(final Context context, final SHAREDPREFERENCE_KEY key) {
        final Object defaultValue = key.getDefault();

        if ((defaultValue == null) || (defaultValue instanceof String)) {
            return getSharedPreferences(context).getString(key.name(), (String) defaultValue);
        } else {
            throw new IllegalArgumentException("String value does not exist for " + key.name());
        }
    }

    public static void removeString(final Context context, final SHAREDPREFERENCE_KEY key) {
        final Object defaultValue = key.getDefault();
        if ((defaultValue == null) || (defaultValue instanceof String)) {
            getEditor(context).remove((String) defaultValue);
        } else {
            throw new IllegalArgumentException("String value does not exist for " + key.name());
        }
    }

}
