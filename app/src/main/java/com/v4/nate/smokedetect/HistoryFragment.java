package com.v4.nate.smokedetect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import butterknife.ButterKnife;


public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment";
    SharedPreferences sharedPreferences;
    ArrayList<String> eventTitlesList;
    ArrayList<String> eventTimesList;
    ArrayList<String> deviceIDList;
    LinkedHashMap<String, HeaderInfo> linkedHashMap = new LinkedHashMap<>();
    ArrayList<HeaderInfo> SectionList = new ArrayList<>();
    ExpandableListView expandableListView;
    ExpandableListAdapter expandablelistAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String homeID = "1376hh";
    String deviceID = "12ab12";
    String deviceIDJson;
    ArrayList<String> deviceListTest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        initialize();
        swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for (int i = 0; i < eventTitlesList.size(); i++) {

                    int position = addEvent(eventTitlesList.get(i), deviceIDList.get(i), eventTimesList.get(i));
                    ((BaseExpandableListAdapter) expandablelistAdapter).notifyDataSetChanged();
                    collapseAll();
                    expandableListView.expandGroup(position);
                    expandableListView.setSelectedGroup(position);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        expandableListView = view.findViewById(R.id.history_list);
        expandablelistAdapter = new CustomExpandableListAdapter(getContext(), SectionList);
        expandableListView.setAdapter(expandablelistAdapter);


        return view;
    }

    public void initialize() {
        sharedPreferences = getActivity().getSharedPreferences("ID", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("HomeID") && sharedPreferences.contains("DeviceID")) {
            Gson gson = new Gson();
            homeID = sharedPreferences.getString("HomeID", null);
            deviceIDJson = sharedPreferences.getString("DeviceID", null);
            deviceListTest = gson.fromJson(deviceIDJson, new TypeToken<ArrayList<String>>() {
            }.getType());

            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID).child(deviceID).child("messages");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Grabs all of the database info and stores it in three arraylists
                    collectEvents((Map<String, Object>) dataSnapshot.getValue());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    private void collectEvents(Map<String, Object> events) {
        eventTimesList = new ArrayList<>();
        eventTitlesList = new ArrayList<>();
        deviceIDList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : events.entrySet()) { //Gets all of the entries directly beneath the device
            Map<String, Object> messagesMap = (Map<String, Object>) entry.getValue();
            deviceIDList.add(deviceID);
            eventTitlesList.add(messagesMap.get("eventString").toString());
            eventTimesList.add(messagesMap.get("eventTime").toString());

        }
    }


    private int addEvent(String eventTitle, String eventDevice, String eventTime) {
        HeaderInfo headerInfo = new HeaderInfo();
        headerInfo.setEventTitle(eventTitle);
        linkedHashMap.put(eventTitle, headerInfo);
        SectionList.add(headerInfo);
        ArrayList<DetailInfo> productList = headerInfo.getProductList();
        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setEventStrings("Event time: " + eventTime, "From device: " + eventDevice);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        return SectionList.indexOf(headerInfo);

    }

    private void expandAll() {
        int count = expandablelistAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.expandGroup(i);
        }
    }

    private void collapseAll() {
        int count = expandablelistAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.collapseGroup(i);
        }
    }
}
