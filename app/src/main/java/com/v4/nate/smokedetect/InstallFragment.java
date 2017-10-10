package com.v4.nate.smokedetect;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InstallFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanaceState) {
        View view = inflater.inflate(R.layout.fragment_install, container, false);
        return view;
    }
}
