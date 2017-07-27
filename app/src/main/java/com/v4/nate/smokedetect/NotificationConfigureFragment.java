package com.v4.nate.smokedetect;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

public class NotificationConfigureFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String S) {
        addPreferencesFromResource(R.xml.notification_preferences);
    }


}
