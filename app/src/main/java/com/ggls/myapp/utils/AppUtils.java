package com.ggls.myapp.utils;

public class AppUtils {
    public static String USERNAME = "";

    public static boolean isStrEmpty(String str) {
        if (str == null || str.length() < 1)
            return true;
        return false;
    }
}
