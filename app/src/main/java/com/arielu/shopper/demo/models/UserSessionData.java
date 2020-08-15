package com.arielu.shopper.demo.models;

public class UserSessionData {
    private String listID;
    private String companybranchID;

    // for firebase
    private UserSessionData() {}

    public UserSessionData(String listid, String cbID)
    {
        setListID(listid);
        setCompanybranchID(cbID);
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    public String getCompanybranchID() {
        return companybranchID;
    }

    public void setCompanybranchID(String companybranchID) {
        this.companybranchID = companybranchID;
    }
}
