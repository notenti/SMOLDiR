package com.v4.nate.smokedetect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceSpecificationsListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SpecificationInfo> data;

    public DeviceSpecificationsListAdapter(Context context, ArrayList<SpecificationInfo> d) {
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
        SpecificationInfo specificationInfo = data.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.device_specifications_list_row, null);
        }

        TextView specification = convertView.findViewById(R.id.specification);
        specification.setText(specificationInfo.getSpecification().trim());
        TextView status = convertView.findViewById(R.id.status);
        status.setText(specificationInfo.getStatus().trim());
        return convertView;


    }
}
