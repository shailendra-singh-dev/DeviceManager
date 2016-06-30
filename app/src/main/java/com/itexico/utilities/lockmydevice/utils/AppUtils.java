package com.itexico.utilities.lockmydevice.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iTexico Developer on 6/28/2016.
 */
public class AppUtils {

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if(file.getName().endsWith(".mp4")){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }
}
