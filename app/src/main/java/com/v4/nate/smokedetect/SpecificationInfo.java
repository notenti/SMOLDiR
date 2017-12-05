package com.v4.nate.smokedetect;

/**
 * Created by nate on 11/26/17.
 */

public class SpecificationInfo {

    private String specification;
    private String status;
    private int resource;

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
