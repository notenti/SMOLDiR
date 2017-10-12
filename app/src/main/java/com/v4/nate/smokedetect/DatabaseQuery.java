package com.v4.nate.smokedetect;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class DatabaseQuery {
    String hold;

    public String requestFromDatabase(String homeID, String deviceID, String type, final String field) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID).child(deviceID).child(type);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> messagesMap = (Map<String, Object>) dataSnapshot.getValue();
                hold = messagesMap.get(field).toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return hold;
    }
}
