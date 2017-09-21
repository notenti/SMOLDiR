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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.ButterKnife;


public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment";
    SharedPreferences sharedPreferences;
    ArrayList<String> eventTitles;
    ArrayList<String> eventTimes;
    ArrayList<String> deviceID;
    LinkedHashMap<String, HeaderInfo> linkedHashMap = new LinkedHashMap<>();
    ArrayList<HeaderInfo> SectionList = new ArrayList<>();
    ExpandableListView expandableListView;
    ExpandableListAdapter expandablelistAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for (int i = 0; i < eventTitles.size(); i++) {

                    int position = addEvent(eventTitles.get(i), deviceID.get(i), eventTimes.get(i));
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
        trigger();

        return view;
    }

    public void trigger() {
        sharedPreferences = getActivity().getSharedPreferences("ID", Context.MODE_PRIVATE);
        final String homeID = sharedPreferences.getString("HomeID", null);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID);
        Query dataOrderedByKey = database.orderByKey();
        dataOrderedByKey.addValueEventListener(new ValueEventListener() {
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

    private void collectEvents(Map<String, Object> events) {
        eventTimes = new ArrayList<>();
        eventTitles = new ArrayList<>();
        deviceID = new ArrayList<>();

        for (Map.Entry<String, Object> entry : events.entrySet()) {
            Map eventStringMap = (Map) entry.getValue();
            Map eventTimeMap = (Map) entry.getValue();
            Map deviceIDMap = (Map) entry.getValue();
            deviceID.add((String) deviceIDMap.get("deviceID"));
            eventTitles.add((String) eventStringMap.get("eventString"));
            eventTimes.add((String) eventTimeMap.get("eventTime"));
        }
        System.out.println(eventTimes);
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
