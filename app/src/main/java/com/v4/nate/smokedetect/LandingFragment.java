package com.v4.nate.smokedetect;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import java.util.concurrent.TimeUnit;

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
    View view;
    SharedPreferences sharedPreferences;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    ArrayList<DeviceOverviewInfo> deviceIDFromDatabase;
    ArrayList<String> deviceListTest;

    FrameLayout progressBarHolder;

    //ListView stuff
    ListView list;
    OverviewDeviceListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_landing, container, false);
        ButterKnife.bind(this, view);

        getActivity().setTitle("Home");


        progressBarHolder = view.findViewById(R.id.progressBarHolder);
        new MyTask().execute();
        initialize();
        deviceIDFromDatabase = new ArrayList<>();


        list = view.findViewById(R.id.device_list);
        adapter = new OverviewDeviceListAdapter(getContext(), deviceIDFromDatabase);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView mytv = view.findViewById(R.id.deviceTitle);
                Bundle bundle = new Bundle();
                bundle.putString("device", mytv.getText().toString());
                Toast.makeText(getActivity(), mytv.getText().toString(), Toast.LENGTH_SHORT).show();
                DeviceInfoFragment deviceInfoFragment = new DeviceInfoFragment();
                deviceInfoFragment.setArguments(bundle);
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

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                while (!deviceIDFromDatabase.isEmpty()) {
                    deviceIDFromDatabase.remove(0);
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    addOverviewDeviceEntry(ds.getKey(), ds.child("var").child("loc").getValue().toString());

                }
                adapter.notifyDataSetChanged();
                //view.findViewById(R.id.progressBarHolder).setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void addOverviewDeviceEntry(String device, String location) {
        DeviceOverviewInfo deviceOverviewInfo = new DeviceOverviewInfo();
        deviceOverviewInfo.setDevice(device);
        deviceOverviewInfo.setLocation(location);
        deviceIDFromDatabase.add(deviceOverviewInfo);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(1f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < 2; i++) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
