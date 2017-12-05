package com.v4.nate.smokedetect;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 12/4/17.
 */

public class DateSort implements Comparable<DeviceHistoryInfo> {
    private Date dateTime;


    public Date getDateTime() {
        return dateTime;
    }

    @Override
    public int compareTo(DeviceHistoryInfo deviceHistoryInfo) {
        return getDateTime().compareTo(convertDate(deviceHistoryInfo.getDate()));
    }

    private Date convertDate(String dateString) {
        Date test = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        try {
            Date date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return test;
        }


    }

}
