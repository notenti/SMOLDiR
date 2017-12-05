package com.v4.nate.smokedetect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<HeaderInfo> expandableListTitle;


    public CustomExpandableListAdapter(Context context, ArrayList<HeaderInfo> expandableListTitle) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        ArrayList<EventInfo> productList = expandableListTitle.get(listPosition).getEventStringList();
        return productList.get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        EventInfo detailInfo = (EventInfo) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_row, null);
        }
        TextView time = convertView.findViewById(R.id.time);
        time.setText(detailInfo.getEvent().trim());
        TextView location = convertView.findViewById(R.id.location);
        location.setText(detailInfo.getLocation().trim());
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        ArrayList<EventInfo> productList = expandableListTitle.get(listPosition).getEventStringList();
        return productList.size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        HeaderInfo headerInfo = (HeaderInfo) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_heading, null);
        }
        TextView heading = convertView.findViewById(R.id.heading);
        heading.setText(headerInfo.getDate().trim());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}

