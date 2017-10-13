package com.v4.nate.smokedetect;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandingFragment extends Fragment {

    @BindView(R.id.landingButton)
    ImageButton _flameButton;
    @BindView(R.id.detectorStatus)
    TextView _detectorStatus;
    String homeID;
    String deviceID;
    String status;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        ButterKnife.bind(this, view);

        sharedPreferences = getActivity().getSharedPreferences("ID", Context.MODE_PRIVATE);
        homeID = sharedPreferences.getString("HomeID", null);
        deviceID = sharedPreferences.getString("DeviceID", null);

        _flameButton = view.findViewById(R.id.landingButton);
        _detectorStatus.setText("Everything is ok.");
        _flameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID).child(deviceID).child("var");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> statusMap = (Map<String, Object>) dataSnapshot.getValue();
                status = statusMap.get("status").toString();

                if (status.equals("ok")) {
                    _flameButton.setImageResource(R.drawable.green_flame);
                    _detectorStatus.setText("Everything is ok.");
                } else if (status.equals("low battery")) {
                    _flameButton.setImageResource(R.drawable.orange_flame);
                    _detectorStatus.setText("You should change your battery.");
                } else if (status.equals("alarm")) {
                    _flameButton.setImageResource(R.drawable.red_flame);
                    _detectorStatus.setText("An alarm is being raised.");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void function() {
        System.out.println("Hey nate");
    }
}
