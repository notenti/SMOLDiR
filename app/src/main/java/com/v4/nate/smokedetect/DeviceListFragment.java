package com.v4.nate.smokedetect;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DeviceListFragment extends Fragment {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    SharedPreferences sharedPreferences;
    ArrayList<HeaderInfo> SectionList = new ArrayList<>();
    LinkedHashMap<String, HeaderInfo> linkedHashMap = new LinkedHashMap<>();
    ArrayList<String> deviceIDFromDatabase;

    String homeID;
    String deviceIDList;
    ArrayList<String> deviceListTest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_list, container, false);
        initialize();
        expandableListView = view.findViewById(R.id.device_list);
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), SectionList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(i);
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
            System.out.println(deviceListTest);
        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Grabs all of the database info and stores it in three arraylists
                collectEvents((Map<String, Object>) dataSnapshot.getValue());
                for (int i = 0; i < deviceIDFromDatabase.size(); i++) {

                    int position = addEvent(deviceIDFromDatabase.get(i));
                    ((BaseExpandableListAdapter) expandableListAdapter).notifyDataSetChanged();
                    collapseAll();
                    expandableListView.expandGroup(position);
                    expandableListView.setSelectedGroup(position);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void collectEvents(Map<String, Object> events) {
        deviceIDFromDatabase = new ArrayList<>();

        for (Map.Entry<String, Object> entry : events.entrySet()) { //Gets all of the entries directly beneath the device
            String test = entry.getKey();
            deviceIDFromDatabase.add(test);

        }
    }


    private void collapseAll() {
        int count = expandableListAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.collapseGroup(i);
        }
    }

    private int addEvent(String deviceID) {
        HeaderInfo headerInfo = new HeaderInfo();
        headerInfo.setEventTitle(deviceID);
        linkedHashMap.put(deviceID, headerInfo);
        SectionList.add(headerInfo);
        ArrayList<DetailInfo> productList = headerInfo.getProductList();
        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setEventStrings("Event time: " + deviceID, "From device: " + deviceID);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        return SectionList.indexOf(headerInfo);

    }
}
