package com.v4.nate.smokedetect;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandingFragment extends Fragment {

    @BindView(R.id.landingButton)
    ImageButton _flameButton;
    @BindView(R.id.detectorStatus)
    TextView _detectorStatus;
    String homeID = "1376hh";
    String deviceID = "12ab12";
    String deviceIDList;
    String status;
    SharedPreferences sharedPreferences;

    ArrayList<String> deviceIDFromDatabase;
    ArrayList<String> deviceListTest;

    //ListView stuff
    ListView list;
    CustomAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        ButterKnife.bind(this, view);

        deviceIDFromDatabase = new ArrayList<>();
        initialize();

        list = view.findViewById(R.id.device_list);
        adapter = new CustomAdapter(getContext(), deviceIDFromDatabase);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), Integer.toString(i), Toast.LENGTH_SHORT).show();
                DeviceInfoFragment deviceInfoFragment = new DeviceInfoFragment();
                ((LandingActivity) getActivity()).setNewFragment(deviceInfoFragment);
            }
        });

        sharedPreferences = getActivity().getSharedPreferences("ID", Context.MODE_PRIVATE);
//        homeID = sharedPreferences.getString("HomeID", null);
//        deviceID = sharedPreferences.getString("DeviceID", null);

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
                } else if (status.equals("warning")) {
                    _flameButton.setImageResource(R.drawable.orange_flame);
                    _detectorStatus.setText("Warning.");
                } else if (status.equals("info")) {
                    _flameButton.setImageResource(R.drawable.purple_flame);
                    _detectorStatus.setText("Something about info.");
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

    public void initialize() {
        sharedPreferences = getActivity().getSharedPreferences("ID", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("HomeID") && sharedPreferences.contains("DeviceID")) {
            Gson gson = new Gson();
            homeID = sharedPreferences.getString("HomeID", null);
            deviceIDList = sharedPreferences.getString("DeviceID", null);
            deviceListTest = gson.fromJson(deviceIDList, new TypeToken<ArrayList<String>>() {
            }.getType());
        } else {
            Gson gson = new Gson();
            homeID = "1376hh";
            deviceIDList = "[\"12ab12\",\"45tt45\"]";
            deviceListTest = gson.fromJson(deviceIDList, new TypeToken<ArrayList<String>>() {
            }.getType());

        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    deviceIDFromDatabase.add(ds.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
