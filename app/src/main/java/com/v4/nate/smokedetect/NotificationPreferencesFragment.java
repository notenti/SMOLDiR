package com.v4.nate.smokedetect;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import java.util.HashMap;


public class NotificationPreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String S) {
        addPreferencesFromResource(R.xml.notification_preferences);

    }


    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("hush")) {
            String url = "192.168.0.107/hush.php";
            HashMap<String, String> params = new HashMap<>();
            params.put("user", "4321");
            params.put("hush", "1");
            SendToDevicesActivity send = new SendToDevicesActivity();
            send.sendToServer(getActivity(), url, params);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


}
