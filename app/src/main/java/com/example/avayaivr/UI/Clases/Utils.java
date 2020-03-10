package com.example.avayaivr.UI.Clases;

import android.content.pm.PackageManager;

public class Utils {
    public static boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageGids(packagename);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
