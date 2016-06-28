package com.itexico.utilities.lockmydevice;

/**
 * Created by iTexico Developer on 6/28/2016.
 */
public class AppUtils {

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
