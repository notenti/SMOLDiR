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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
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



        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID).child(device).child("messages");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectEvents((Map<String, Object>) dataSnapshot.getValue());
                expandableListAdapter.notifyDataSetChanged();
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
            System.out.println(messagesMap);
            System.out.println(messagesMap.get("eventTime").toString());
            List<String> readable;
            readable = convertDateNumToString(messagesMap.get("eventTime").toString());
            addProduct(readable.get(0), readable.get(1), "Kitchen");


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
        eventInfo.setEvent(event);
        eventInfo.setLocation(location);
        eventStringList.add(eventInfo);
        headerInfo.setEventStringList(eventStringList);

        groupPosition = SectionList.indexOf(headerInfo);
        return groupPosition;
    }

    private List<String> convertDateNumToString(String eventTime) {
        String month;
        String day;
        String year;
        String hour;
        List<String> readable = new ArrayList<>();
        List<String> dateAndTime = Arrays.asList(eventTime.split(" "));
        List<String> date = Arrays.asList(dateAndTime.get(0).split("-"));
        List<String> time = Arrays.asList(dateAndTime.get(1).split(":"));

        switch (date.get(0)) {
            case "1":
                month = "January";
                break;
            case "2":
                month = "February";
                break;
            case "3":
                month = "March";
                break;
            case "4":
                month = "April";
                break;
            case "5":
                month = "May";
                break;
            case "6":
                month = "June";
                break;
            case "7":
                month = "July";
                break;
            case "8":
                month = "August";
                break;
            case "9":
                month = "September";
                break;
            case "10":
                month = "October";
                break;
            case "11":
                month = "November";
                break;
            case "12":
                month = "December";
                break;
            default:
                month = "Invalid month";
                break;

        }
        String readableDate = new StringBuilder(month).append(" ").append(date.get(1)).append(", ").append(date.get(2)).toString();
        String readableTime = new StringBuilder(time.get(0)).append(time.get(1)).toString();

        readable.add(readableDate);
        readable.add(readableTime);

        return readable;

    }
}
