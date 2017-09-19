package com.v4.nate.smokedetect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment";
    HashMap<String, String> params = new HashMap<>();
    String url = "http://192.168.0.107/history.php";
    SendToDevicesActivity send = new SendToDevicesActivity();
    SharedPreferences sharedPreferences;

    @BindView(R.id.history_button)
    Button _historyButton;
    @BindView(R.id.ll)
    LinearLayout _linearLayout;
    @BindView(R.id.history_list)
    ListView _historyList;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ArrayList<String> eventTitles;
    ArrayList<String> eventTimes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);


        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        _historyList.setAdapter(adapter);
        _historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trigger();
            }
        });


        return view;
    }

    public void trigger() {
        sharedPreferences = getActivity().getSharedPreferences("ID", Context.MODE_PRIVATE);
        final String homeID = sharedPreferences.getString("HomeID", null);
        adapter.notifyDataSetChanged();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectEvents((Map<String, Object>) dataSnapshot.getValue());
                for (String s : eventTimes) {
                    adapter.add("Event: " + s);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void collectEvents(Map<String, Object> users) {
        eventTimes = new ArrayList<>();
        eventTitles = new ArrayList<>();

        for (Map.Entry<String, Object> entry : users.entrySet()) {
            Map eventString = (Map) entry.getValue();
            Map eventTime = (Map) entry.getValue();
            eventTitles.add((String) eventString.get("eventString"));
            eventTimes.add((String) eventTime.get("eventTime"));
        }
        System.out.println(eventTitles.toString());
        System.out.println(eventTimes.toString());
    }
}
