package com.v4.nate.smokedetect;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceInfoFragment extends Fragment {

    //ListView stuff
    final Context c = getContext();
    @BindView(R.id.imageHeading)
    TextView _testButton;
    @BindView(R.id.locationStatus)
    TextView _location;
    @BindView(R.id.batteryStatus)
    TextView _batteryStatus;

    ListView deviceHistoryList;
    DeviceHistoryListAdapter deviceHistoryListAdapter;
    String homeID = "1376hh";
    String device;
    ArrayList<DeviceHistoryInfo> historyList = new ArrayList<>();
    ArrayList<SpecificationInfo> specificationList = new ArrayList<>();
    Boolean open = false;
    DataSnapshot totalDatasnapShot;
    String lastTested;
    String batteryStatus;
    String location;
    String locationTitle;

    private ArrayList<HeaderInfo> SectionList = new ArrayList<>();
    private LinkedHashMap<String, HeaderInfo> mySection = new LinkedHashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_info, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            device = bundle.getString("device");
            locationTitle = bundle.getString("location");
        }


        getActivity().setTitle(locationTitle);

        _batteryStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        _location.setOnClickListener(new View.OnClickListener() {
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
                        final String location = userInputDialogEditText.getText().toString();
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID).child(device);
                        database.child("var").child("loc").getRef().setValue(location);

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

        _testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID).child(device);
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                database.child("var").child("test").setValue(true);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                System.out.println("NEGATIVE");
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            }
        });


        View footerView = inflater.inflate(R.layout.bottom_list, null);
        TextView footer = footerView.findViewById(R.id.loadMore);

        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (open) {
                    open = false;
                    for (int i = historyList.size(); i > 5; i--) {
                        historyList.remove(historyList.size() - 1);
                    }
                    TextView tv1 = view.findViewById(R.id.loadMore);
                    tv1.setText("Show more");

                    deviceHistoryListAdapter.notifyDataSetChanged();
                } else {
                    historyList.remove(4);
                    historyList.remove(3);
                    historyList.remove(2);
                    historyList.remove(1);
                    historyList.remove(0);
                    TextView tv1 = view.findViewById(R.id.loadMore);
                    tv1.setText("Show less");

                    collectEvents((Map<String, Object>) totalDatasnapShot.child("messages").getValue(), 6, true);
                    open = true;
                }

            }
        });


        deviceHistoryList = view.findViewById(R.id.deviceHistoryList);
        deviceHistoryList.addFooterView(footer);
        deviceHistoryListAdapter = new DeviceHistoryListAdapter(getContext(), historyList);
        deviceHistoryList.setAdapter(deviceHistoryListAdapter);

        deviceHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view1 = layoutInflater.inflate(R.layout.event_info_expanded, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
                alertDialogBuilderUserInput.setView(view1);

                AlertDialog alertDialog = alertDialogBuilderUserInput.create();
                alertDialog.show();


                TextView locationStatusTV = view1.findViewById(R.id.eventLocationStatus);
                locationStatusTV.setText(location);
                TextView eventTypeStatusTV = view1.findViewById(R.id.eventTypeStatus);
                eventTypeStatusTV.setText("EVENT TYPE");
                TextView eventDateStatusTV = view1.findViewById(R.id.eventDateStatus);
                TextView dateTV = view.findViewById(R.id.date);
                eventDateStatusTV.setText(dateTV.getText().toString());

            }
        });

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID).child(device);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalDatasnapShot = dataSnapshot;
                lastTested = dataSnapshot.child("var").child("lastTest").getValue().toString();
                batteryStatus = dataSnapshot.child("var").child("batt_status").getValue().toString();
                location = dataSnapshot.child("var").child("loc").getValue().toString();
                addSpecificationEntry("Last Tested", convertDateNumToString(lastTested).get(0));
                if (historyList.isEmpty()) {
                    collectEvents((Map<String, Object>) dataSnapshot.child("messages").getValue(), 5, false);
                } else {
                    historyList.remove(4);
                    historyList.remove(3);
                    historyList.remove(2);
                    historyList.remove(1);
                    historyList.remove(0);
                    collectEvents((Map<String, Object>) dataSnapshot.child("messages").getValue(), 5, false);
                }

                TextView batteryTV = view.findViewById(R.id.batteryStatus);
                batteryTV.setText(batteryStatus);
                TextView locationTV = view.findViewById(R.id.locationStatus);
                locationTV.setText(location);
                TextView lastTestTV = view.findViewById(R.id.lastTest);
                lastTestTV.setText(convertDateNumToString(lastTested).get(2));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void collectEvents(Map<String, Object> events, int count, boolean full) {
        int i = 0;

        for (Map.Entry<String, Object> entry : events.entrySet()) { //Gets all of the entries directly beneath the device
            if (i < count || full) {
                Map<String, Object> messagesMap = (Map<String, Object>) entry.getValue();
                List<String> readable;
                readable = convertDateNumToString(messagesMap.get("eventTime").toString());
                addDeviceHistoryEntry(readable.get(0), readable.get(1), messagesMap.get("eventTime").toString(), R.drawable.ic_exclamation_mark);
                System.out.println(messagesMap.get("eventTime").toString());
                i++;
            }

        }

        Collections.sort(historyList, new Comparator<DeviceHistoryInfo>() {
            @Override
            public int compare(DeviceHistoryInfo dh1, DeviceHistoryInfo dh2) {
                return convertDate(dh1.getDateTime()).compareTo(convertDate(dh2.getDateTime()));
            }
        });
        Collections.reverse(historyList);
        deviceHistoryListAdapter.notifyDataSetChanged();
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

    private void addDeviceHistoryEntry(String date, String time, String dateTime, int resource) {
        DeviceHistoryInfo deviceHistoryInfo = new DeviceHistoryInfo();
        deviceHistoryInfo.setDate(date);
        deviceHistoryInfo.setTime(time);
        deviceHistoryInfo.setDateTime(dateTime);
        deviceHistoryInfo.setResource(resource);
        historyList.add(deviceHistoryInfo);
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
            denote = " PM";

        } else {
            hour = time.get(0);
            denote = " AM";
        }
        String readableDate = new StringBuilder(month).append(" ").append(date.get(1)).toString();
        String readableTime = new StringBuilder(hour).append(":").append(time.get(1)).append(denote).toString();
        String numberedDate = new StringBuilder(date.get(0)).append("/").append(date.get(1)).toString();

        readable.add(readableDate);
        readable.add(readableTime);
        readable.add(numberedDate);

        return readable;

    }

    private Date convertDate(String dateString) {
        Date test = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        try {
            Date date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return test;
        }
    }

}
