package com.v4.nate.smokedetect.fragments;


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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.v4.nate.smokedetect.R;
import com.v4.nate.smokedetect.activities.LandingActivity;
import com.v4.nate.smokedetect.adapters.OverviewDeviceListAdapter;
import com.v4.nate.smokedetect.objects.DeviceOverviewInfo;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandingFragment extends Fragment {

    @BindView(R.id.landingButton)
    ImageButton _flameButton;
    @BindView(R.id.detectorStatus)
    TextView _detectorStatus;
    String homeID = "1376hh";
    String deviceID = "Nate1";
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

                TextView deviceTitleTV = view.findViewById(R.id.deviceTitle);
                TextView locationTV = view.findViewById(R.id.locationTitle);
                Bundle bundle = new Bundle();
                bundle.putString("device", deviceTitleTV.getText().toString());
                bundle.putString("location", locationTV.getText().toString());
                DeviceInfoFragment deviceInfoFragment = new DeviceInfoFragment();
                deviceInfoFragment.setArguments(bundle);
                ((LandingActivity) getActivity()).setNewFragment(deviceInfoFragment);
            }
        });

        sharedPreferences = getActivity().getSharedPreferences("ID", Context.MODE_PRIVATE);

        _flameButton = view.findViewById(R.id.landingButton);
        _detectorStatus.setText("Everything is ok.");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(homeID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int status = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.child("var").child("status").getValue().toString().equals("ok") && status < 1) {
                        status = 1;

                    } else if (ds.child("var").child("status").getValue().toString().equals("warning") && status < 2) {
                        status = 2;

                    } else if (ds.child("var").child("status").getValue().toString().equals("alarm") && status < 3) {
                        status = 3;
                    }
                }

                switch (status) {
                    case 1:
                    default:
                        _flameButton.setImageResource(R.drawable.green_flame);
                        _detectorStatus.setText("Everything is ok.");
                        break;
                    case 2:
                        _flameButton.setImageResource(R.drawable.orange_flame);
                        _detectorStatus.setText("Warning.");
                        break;
                    case 3:
                        _flameButton.setImageResource(R.drawable.red_flame);
                        _detectorStatus.setText("An alarm is being raised.");
                        break;

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
