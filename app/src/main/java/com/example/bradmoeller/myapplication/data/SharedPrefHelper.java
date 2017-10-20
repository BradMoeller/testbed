package com.example.bradmoeller.myapplication.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by bradmoeller on 10/19/17.
 */

public class SharedPrefHelper {
    private static final String PREFS_NAME = "com.example.bradmoeller.myapplication.prefs";
    public static final String TIME_KEY = "com.example.bradmoeller.myapplication.prefs.TIME_KEY";

    public static void putTime(Context c, String time) {
        SharedPreferences sp = c.getSharedPreferences(PREFS_NAME, Context.MODE_MULTI_PROCESS);

        Set<String> times = sp.getStringSet(TIME_KEY, new HashSet<String>());
        times.add(time);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(TIME_KEY, times);
        editor.commit();
    }

    public static Set<String> getTimes(Context c) {
        SharedPreferences sp = c.getSharedPreferences(PREFS_NAME, Context.MODE_MULTI_PROCESS);
        return sp.getStringSet(TIME_KEY, new HashSet<String>());
    }
}
