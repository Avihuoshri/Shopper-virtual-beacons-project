package com.arielu.shopper.demo.models;

import java.util.List;

public class Permission {
    private String userID;
    public enum PermissionType {Member,Owner}
    private PermissionType permissionType;

    // Firebase need an empty constructor
    public Permission() { }

    public Permission(String userID, PermissionType pType) {
        this.userID = userID;
        this.permissionType = pType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(int pTypeNum)
    {
        this.permissionType = permissionType.values()[pTypeNum];
    }

    // multiple setters confuses firebase.
//    public void setPermissionType(PermissionType permissionType) {
//        this.permissionType = permissionType;
//    }


    @Override
    public String toString() {
        return "Permission{" +
                "userID='" + userID + '\'' +
                ", permissionType=" + permissionType +
                '}';
    }
}
