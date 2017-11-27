package com.v4.nate.smokedetect;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceInfoFragment extends Fragment {

    //ListView stuff
    final Context c = getContext();
    @BindView(R.id.btn_showMoreHistory)
    Button _changeName;
    ListView list;
    DeviceSpecificationsListAdapter adapter;
    String homeID = "1376hh";
    String device;
    ArrayList<String> deviceHistory;
    ArrayList<SpecificationInfo> specificationList = new ArrayList<>();
    private CustomExpandableListAdapter expandableListAdapter;
    private ArrayList<HeaderInfo> SectionList = new ArrayList<>();
    private LinkedHashMap<String, HeaderInfo> mySection = new LinkedHashMap<>();
    private ExpandableListView expandableListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_info, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            device = bundle.getString("device");
        }


        _changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view1 = layoutInflater.inflate(R.layout.user_input_dialog_box, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
                alertDialogBuilderUserInput.setView(view1);

                final EditText userInputDialogEditText = view1.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String ttt = userInputDialogEditText.getText().toString();
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID).child(device);
                        database.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.child("var").child("loc").getRef().setValue(ttt);
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(getContext(), ttt, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilderUserInput.create();
                alertDialog.show();
            }
        });

        deviceHistory = new ArrayList<>();
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), SectionList);
        expandableListView = view.findViewById(R.id.myList);
        expandableListView.setAdapter(expandableListAdapter);


        list = view.findViewById(R.id.status);
        adapter = new DeviceSpecificationsListAdapter(getContext(), specificationList);
        list.setAdapter(adapter);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID).child(device);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectEvents((Map<String, Object>) dataSnapshot.child("messages").getValue());
                addSpecificationEntry("Location", dataSnapshot.child("var").child("loc").getValue().toString());
                expandableListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addSpecificationEntry("Battery Status", "Low");
        addSpecificationEntry("Hushed?", "Yes");






        return view;
    }

    private void collectEvents(Map<String, Object> events) {

        for (Map.Entry<String, Object> entry : events.entrySet()) { //Gets all of the entries directly beneath the device
            Map<String, Object> messagesMap = (Map<String, Object>) entry.getValue();
            System.out.println(messagesMap);
            System.out.println(messagesMap.get("eventTime").toString());
            List<String> readable;
            readable = convertDateNumToString(messagesMap.get("eventTime").toString());
            addHistoryEntry(readable.get(0), readable.get(1), "Kitchen");


        }
    }

    private int addHistoryEntry(String date, String event, String location) {
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

    private void addSpecificationEntry(String specification, String status) {
        SpecificationInfo specificationInfo = new SpecificationInfo();
        specificationInfo.setSpecification(specification);
        specificationInfo.setStatus(status);
        specificationList.add(specificationInfo);
    }

    private List<String> convertDateNumToString(String eventTime) {
        String month;
        String day;
        String year;
        String hour;
        String denote;
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

        if (Integer.parseInt(time.get(0)) > 12) {
            hour = String.valueOf(Integer.parseInt(time.get(0)) - 12);
            denote = " p.m.";

        } else {
            hour = time.get(0);
            denote = " a.m.";
        }
        String readableDate = new StringBuilder(month).append(" ").append(date.get(1)).append(", ").append(date.get(2)).toString();
        String readableTime = new StringBuilder(hour).append(":").append(time.get(1)).append(denote).toString();

        readable.add(readableDate);
        readable.add(readableTime);

        return readable;

    }
}
