package com.v4.nate.smokedetect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetails = new HashMap<String, List<String>>();
        List<String> cricket = new ArrayList<>();
        cricket.add("India");
        cricket.add("England");

        List<String> football = new ArrayList<>();
        football.add("USA");
        football.add("Brazil");

        expandableListDetails.put("Cricket Teams", cricket);
        expandableListDetails.put("Football Teams", football);
        return expandableListDetails;
    }
}
