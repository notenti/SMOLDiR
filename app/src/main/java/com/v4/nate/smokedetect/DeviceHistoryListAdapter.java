package com.v4.nate.smokedetect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nate on 11/27/17.
 */

public class DeviceHistoryListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DeviceHistoryInfo> data;

    public DeviceHistoryListAdapter(Context context, ArrayList<DeviceHistoryInfo> d) {
        this.context = context;
        this.data = d;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceHistoryInfo deviceHistoryInfo = data.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.device_history_list_row, null);
        }

        TextView date = convertView.findViewById(R.id.date);
        date.setText(deviceHistoryInfo.getDate().trim());
        TextView location = convertView.findViewById(R.id.location);
        location.setText(deviceHistoryInfo.getLocation().trim());
        return convertView;


    }
}
