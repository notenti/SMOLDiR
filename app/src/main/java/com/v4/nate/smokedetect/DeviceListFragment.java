package com.v4.nate.smokedetect;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class DeviceListFragment extends Fragment {

    SharedPreferences sharedPreferences;
    ArrayList<String> deviceIDFromDatabase;

    String homeID;
    String deviceIDList;
    ArrayList<String> deviceListTest;

    //ListView stuff
    ListView list;
    CustomAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_list, container, false);
        deviceIDFromDatabase = new ArrayList<>();
        initialize();
        list = view.findViewById(R.id.device_list_2);
        adapter = new CustomAdapter(getContext(), deviceIDFromDatabase);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), Integer.toString(i), Toast.LENGTH_SHORT).show();
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
