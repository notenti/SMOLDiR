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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    ArrayList<String> deviceID;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);

        expandableListView = view.findViewById(R.id.history_list);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getContext(), expandableListTitle.get(groupPosition) + " -> " + expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

//
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
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectEvents((Map<String, Object>) dataSnapshot.getValue());
//                adapter.clear();
//                for (int i = 0; i < eventTitles.size(); i++) {
//                    adapter.add(eventTitles.get(i) + " on " + eventTimes.get(i) + " from device " + deviceID.get(i));
//                }
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
    }
}
