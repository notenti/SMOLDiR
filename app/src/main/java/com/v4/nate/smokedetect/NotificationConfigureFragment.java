package com.v4.nate.smokedetect;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class NotificationConfigureFragment extends Fragment {

    View view;
    Button firstButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification_configure, container, false);
        //Get the reference of Button
        firstButton = (Button) view.findViewById(R.id.firstButton);
        //perform setOnclicklistener on first button
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "First Fragment", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


}
