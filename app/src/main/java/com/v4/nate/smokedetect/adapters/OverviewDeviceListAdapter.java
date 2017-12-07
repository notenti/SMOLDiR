package com.v4.nate.smokedetect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.v4.nate.smokedetect.R;
import com.v4.nate.smokedetect.objects.DeviceOverviewInfo;

import java.util.ArrayList;


public class OverviewDeviceListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DeviceOverviewInfo> data;

    public OverviewDeviceListAdapter(Context context, ArrayList<DeviceOverviewInfo> d) {
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
        DeviceOverviewInfo deviceOverviewInfo = data.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_device_row, null);
        }

        TextView device = convertView.findViewById(R.id.deviceTitle);
        device.setText(deviceOverviewInfo.getDevice().trim());
        TextView location = convertView.findViewById(R.id.locationTitle);
        location.setText(deviceOverviewInfo.getLocation().trim());
//        ImageView resource = convertView.findViewById(R.id.statusCircle);
//        resource.setImageResource(deviceOverviewInfo.getResource());
        return convertView;


    }

}
