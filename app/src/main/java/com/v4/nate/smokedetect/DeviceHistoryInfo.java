package com.v4.nate.smokedetect;

/**
 * Created by nate on 11/27/17.
 */

public class DeviceHistoryInfo {

    private String date;
    private String location;
    private int resource;

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
