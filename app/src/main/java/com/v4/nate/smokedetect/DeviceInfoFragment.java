package com.v4.nate.smokedetect;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
    Boolean open = false;
    DataSnapshot totalDatasnapShot;
    String lastTested;
    String batteryStatus;
    String location;
    String locationTitle;
    String imageStr;

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
                open = !open;
                collectEvents((Map<String, Object>) totalDatasnapShot.child("messages").getValue(), 6, true);

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

                TextView imageStringTV = view.findViewById(R.id.imageString);

                String imageString = imageStringTV.getText().toString();

                byte[] imageBytes;

                //decode base64 string to image
                imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ImageView imageView = view1.findViewById(R.id.thermalImage);
                imageView.setImageDrawable(new BitmapDrawable(getResources(), decodedImage));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);


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
                imageStr = dataSnapshot.child("messages").child("12-04-2017 14:30:20").child("eventString").getValue().toString();
                location = dataSnapshot.child("var").child("loc").getValue().toString();
                collectEvents((Map<String, Object>) dataSnapshot.child("messages").getValue(), 5, true);

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
        historyList.clear();

        for (Map.Entry<String, Object> entry : events.entrySet()) { //Gets all of the entries directly beneath the device
            if (i < count || full) {
                Map<String, Object> messagesMap = (Map<String, Object>) entry.getValue();
                List<String> readable;
                readable = convertDateNumToString(messagesMap.get("eventTime").toString());
                String imageString = messagesMap.get("eventString").toString();
                addDeviceHistoryEntry(readable.get(0), readable.get(1), messagesMap.get("eventTime").toString(), imageString, R.drawable.ic_exclamation_mark);
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

        if (open) {
            deviceHistoryListAdapter.notifyDataSetChanged();
            System.out.println(Arrays.toString(historyList.toArray()));
        } else {
            while (historyList.size() > 5) {
                historyList.remove(historyList.size() - 1);
            }
            deviceHistoryListAdapter.notifyDataSetChanged();


        }

    }

    private void addDeviceHistoryEntry(String date, String time, String dateTime, String imageString, int resource) {
        DeviceHistoryInfo deviceHistoryInfo = new DeviceHistoryInfo();
        deviceHistoryInfo.setDate(date);
        deviceHistoryInfo.setTime(time);
        deviceHistoryInfo.setDateTime(dateTime);
        deviceHistoryInfo.setResource(resource);
        deviceHistoryInfo.setImageString(imageString);
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
