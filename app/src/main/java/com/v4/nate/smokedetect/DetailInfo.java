package com.v4.nate.smokedetect;


public class DetailInfo {

    private String eventTime;
    private String eventDevice;

    public String getEventTime() {
        return eventTime;
    }

    public String getEventDevice() {
        return eventDevice;
    }

    public void setEventStrings(String eventTime, String eventDevice) {
        this.eventTime = eventTime;
        this.eventDevice = eventDevice;
    }

}
