package com.v4.nate.smokedetect;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandingFragment extends Fragment {

    @BindView(R.id.landingButton)
    ImageButton _flameButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        ButterKnife.bind(this, view);

        _flameButton = view.findViewById(R.id.landingButton);
        _flameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                function();
            }
        });

        return view;
    }

    public void function() {
        System.out.println("Hey nate");
    }
}
