package com.arielu.shopper.demo.classes;

import android.location.Location;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class Branch implements Serializable
{
    String branch_id ;
    String company_id ;
    String branch_address ;
    String branch_name;
    String location;

    private transient Location location_data;

    // for firebase
    public Branch()
    {

    }

    public Branch(String branch_id, String company_id, String branch_address, String branch_name)
    {
        this.branch_id = branch_id;
        this.company_id = company_id;
        this.branch_address = branch_address;
        this.branch_name = branch_name;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getBranch_address() {
        return branch_address;
    }

    public void setBranch_address(String branch_address) {
        this.branch_address = branch_address;
    }

    public String getBranch_name() {
        return branch_address;
    }

    public void setBranch_name(String name) {
        this.branch_name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Location LocationData()
    {
        if(location_data == null)
            location_data = parseLocation(location);

        return location_data;
    }

    private Location parseLocation(String loc)
    {
        String[] latlonString = loc.split(", ");
        Location loc_data = new Location("string parse");
        loc_data.setLatitude(Double.parseDouble(latlonString[0]));
        loc_data.setLongitude(Double.parseDouble(latlonString[1]));

        return loc_data;
    }

    @NonNull
    @Override
    public String toString() {
        return branch_name;
    }
}
