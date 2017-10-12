package com.v4.nate.smokedetect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetectorHealthFragment extends Fragment {
    String homeID;
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detector_health, container, false);
        sharedPreferences = getActivity().getSharedPreferences("ID", Context.MODE_PRIVATE);
        homeID = sharedPreferences.getString("HomeID", null);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Grabs all of the database info and stores it in three arraylists
                //collectEvents((Map<String, Object>) dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

//    private void collectEvents(Map<String, Object> events) {
//        deviceIDList = new ArrayList<>();
//
//        for (Map.Entry<String, Object> entry : events.entrySet()) { //Gets all of the entries directly beneath the device
//            Map<String, Object> messagesMap = (Map<String, Object>) entry.getValue();
//            for (Map.Entry<String, Object> innerEntry : messagesMap.entrySet()) { //gets all of the entries directly beneath the time stamp (will always be messages and var)
//                Map entryMessages = (Map) innerEntry.getValue();
//                Map entryVariables = (Map) innerEntry.getValue();
//                deviceIDList.add(deviceID);
//                eventTitlesList.add(entryMessages.get("eventString").toString());
//                eventTimesList.add(entryMessages.get("eventTime").toString());
//            }
//        }
//    }
}
