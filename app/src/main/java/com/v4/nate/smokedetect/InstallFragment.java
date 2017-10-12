package com.v4.nate.smokedetect;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;

public class InstallFragment extends Fragment {
    @BindView(R.id.directions)
    TextView _directions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanaceState) {
        View view = inflater.inflate(R.layout.fragment_install, container, false);
        return view;
    }
}
