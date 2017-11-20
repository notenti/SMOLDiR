package com.v4.nate.smokedetect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_info, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            device = bundle.getString("device");
        }

        deviceHistory = new ArrayList<>();

        list = view.findViewById(R.id.granular_device_list);
        adapter = new CustomAdapter(getContext(), deviceHistory);
        list.setAdapter(adapter);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID).child(device).child("messages");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectEvents((Map<String, Object>) dataSnapshot.getValue());
                adapter.notifyDataSetChanged();
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
}
