package com.v4.nate.smokedetect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment";
    SharedPreferences sharedPreferences;

    @BindView(R.id.history_button)
    Button _historyButton;
    @BindView(R.id.ll)
    LinearLayout _linearLayout;
    @BindView(R.id.history_list)
    ListView _historyList;

    //Retrieve from database
    ArrayList<String> eventTitles;
    ArrayList<String> eventTimes;
    ArrayList<String> deviceID;

    LinkedHashMap<String, HeaderInfo> mySection = new LinkedHashMap<>();
    ArrayList<HeaderInfo> SectionList = new ArrayList<>();

    ExpandableListView expandableListView;
    ExpandableListAdapter listAdapter;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);

        expandableListView = view.findViewById(R.id.history_list);
        listAdapter = new CustomExpandableListAdapter(getContext(), SectionList);
        expandableListView.setAdapter(listAdapter);

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

                for (int i = 0; i < eventTitles.size(); i++) {
                    int groupPosition = addProduct(deviceID.get(i), eventTitles.get(i) + " on " + eventTimes.get(i) + " from device " + deviceID.get(i));
                    ((BaseExpandableListAdapter) listAdapter).notifyDataSetChanged();
                }
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


    private int addProduct(String department, String product) {
        int groupPosition;

        HeaderInfo headerInfo = mySection.get(department);

        if (headerInfo == null) {
            headerInfo = new HeaderInfo();
            headerInfo.setName(department);
            mySection.put(department, headerInfo);
            SectionList.add(headerInfo);
        }

        ArrayList<DetailInfo> productList = headerInfo.getProductList();
        int listSize = productList.size();
        listSize++;

        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setSequence(String.valueOf(listSize));
        detailInfo.setName(product);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        groupPosition = SectionList.indexOf(headerInfo);
        return groupPosition;
    }
}
