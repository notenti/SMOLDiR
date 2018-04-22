package com.v4.nate.smokedetect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.v4.nate.smokedetect.R;
import com.v4.nate.smokedetect.objects.DeviceHistoryInfo;

import java.util.ArrayList;

/* Adapter for displaying the device's history
in a digestible manner
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
            convertView = layoutInflater.inflate(R.layout.list_device_history_row, null);
        }

        // Setting text and image elements appropriately
        TextView date = convertView.findViewById(R.id.date);
        date.setText(deviceHistoryInfo.getDate().trim());
        TextView location = convertView.findViewById(R.id.location);
        location.setText(deviceHistoryInfo.getTime().trim());
        ImageView icon = convertView.findViewById(R.id.headingImage);
        icon.setImageResource(deviceHistoryInfo.getResource());
        TextView imageString = convertView.findViewById(R.id.imageString);
        imageString.setText(deviceHistoryInfo.getImageString().trim());
        TextView type = convertView.findViewById(R.id.type);
        type.setText(deviceHistoryInfo.getType());
        return convertView;


    }
}
