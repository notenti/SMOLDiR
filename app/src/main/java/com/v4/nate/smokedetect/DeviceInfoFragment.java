package com.v4.nate.smokedetect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class DeviceInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_info, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int i = bundle.getInt("device", -1);
            if (i == 1) {
                RelativeLayout relativeLayout = view.findViewById(R.id.rel);
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.color_accent));
            }
        }

        return view;
    }
}
