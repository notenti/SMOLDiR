package com.v4.nate.smokedetect;

import java.util.ArrayList;

/**
 * Created by nate on 9/20/17.
 */

public class HeaderInfo {
    private String eventTitle;
    private ArrayList<DetailInfo> productList = new ArrayList<>();

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public ArrayList<DetailInfo> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<DetailInfo> productList) {
        this.productList = productList;
    }
}
