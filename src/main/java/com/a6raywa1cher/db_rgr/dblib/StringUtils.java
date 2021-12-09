package com.a6raywa1cher.db_rgr.dblib;

import java.util.Locale;

public final class StringUtils {
    public static String capitalizeFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
    }
}
