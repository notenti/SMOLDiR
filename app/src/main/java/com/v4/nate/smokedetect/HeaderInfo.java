package com.v4.nate.smokedetect;

import java.util.ArrayList;

/**
 * Created by nate on 9/20/17.
 */

public class HeaderInfo {
    private String date;
    private ArrayList<EventInfo> eventStringList = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<EventInfo> getEventStringList() {
        return eventStringList;
    }

    public void setEventStringList(ArrayList<EventInfo> eventStringList) {
        this.eventStringList = eventStringList;
    }
}
