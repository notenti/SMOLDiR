package com.v4.nate.smokedetect;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

public class NotificationPreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String S) {
        addPreferencesFromResource(R.xml.notification_preferences);

    }


    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("hush")) {
            //PreferenceManager.getDefaultSharedPreferences(this).edit().putString()
            Preference preference = findPreference(key);
            SendToDevicesActivity send = new SendToDevicesActivity();
            send.sendHush(getActivity());
        } else if (key.equals("localized")) {
            Preference preference = findPreference(key);
            SendToDevicesActivity send = new SendToDevicesActivity();
            send.sendLocalized(getActivity());
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
