package com.v4.nate.smokedetect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DeviceInfoFragment extends Fragment {

    //ListView stuff
    ListView list;
    CustomAdapter adapter;
    String homeID = "1376hh";
    String device;
    ArrayList<String> deviceHistory;
    ArrayList<String> eventTimesList;
    ArrayList<String> eventTitlesList;
    private CustomExpandableListAdapter expandableListAdapter;
    private ArrayList<HeaderInfo> SectionList = new ArrayList<>();
    private LinkedHashMap<String, HeaderInfo> mySection = new LinkedHashMap<>();
    private ExpandableListView expandableListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_info, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            device = bundle.getString("device");
        }

        deviceHistory = new ArrayList<>();
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), SectionList);
        expandableListView = view.findViewById(R.id.myList);
        expandableListView.setAdapter(expandableListAdapter);


        addProduct("Nov. 12, 2017", "Fire Alarm", "Kitchen");
        addProduct("Nov. 13, 2017", "CO Alarm", "Kitchen");

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID).child(device).child("messages");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectEvents((Map<String, Object>) dataSnapshot.getValue());
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

    private void collectEvents(Map<String, Object> events) {

        for (Map.Entry<String, Object> entry : events.entrySet()) { //Gets all of the entries directly beneath the device
            Map<String, Object> messagesMap = (Map<String, Object>) entry.getValue();
            System.out.println(device);
//            deviceHistory.add(messagesMap.get("eventTime").toString());

        }
    }

    private int addProduct(String date, String event, String location) {
        int groupPosition = 0;

        HeaderInfo headerInfo = mySection.get(date);
        if (headerInfo == null) {
            headerInfo = new HeaderInfo();
            headerInfo.setDate(date);
            mySection.put(date, headerInfo);
            SectionList.add(headerInfo);
        }

        ArrayList<EventInfo> eventStringList = headerInfo.getEventStringList();
        EventInfo eventInfo = new EventInfo();
        eventInfo.setEventStrings(event, location);
        eventStringList.add(eventInfo);
        headerInfo.setEventStringList(eventStringList);

        groupPosition = SectionList.indexOf(headerInfo);
        return groupPosition;
    }
}
