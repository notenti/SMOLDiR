package com.v4.nate.smokedetect;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;


public class Utility {
    public static Boolean isUserLoggedIn(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getBoolean("isUserLoggedIn", false);

    }

    public static void setUserLoggedIn(Context context, Boolean isLoggedIn){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isUserLoggedIn", isLoggedIn);
        editor.apply();
    }

    public static void logout(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isUserLoggedIn", false);
        editor.apply();
    }

    public static void saveUsernameAndPassword(Context context, String username, String password) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }
}
