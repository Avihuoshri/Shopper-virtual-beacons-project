package com.arielu.shopper.demo.models;

import java.io.Serializable;

/**
 * User class for describing a user entity with Login details and
 * User's data in the system.
 */
public class User implements Serializable {

    //// private variables.
    private String username,name,password,phoneNumber,companybranchID,SessionListID;
    private UserType userType;
    //// public enum
    public enum UserType{Customer,Worker};
    //// Constructors

    public User() {
    }

    public User(String username, String name){
        setUsername(username);
        setName(name);
    }
    public User(String name,String phoneNumber,int userType)
    {
        setName(name);
        setPhoneNumber(phoneNumber);
        idUserType(userType);
    }

    public User(String username, String name, String password, String phoneNumber, UserType userType) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }
//// Getters and Setters

    // username
    public void setUsername(String newUsername)
    {
        this.username = newUsername;
    }

    public String getUsername()
    {
        return this.username;
    }

    // name
    public void setName(String newName)
    {
        this.name = newName;
    }

    public String getName()
    {
        return this.name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserType getUserType() {
        return userType;
    }

    public void idUserType(int userType){
        this.userType = UserType.values()[userType];
    }
    public void setUserType(String userType) {
        this.userType = UserType.valueOf(userType);
    }

    public String getCompanybranchID() {
        return companybranchID;
    }

    public void setCompanybranchID(String companybranchID) {
        this.companybranchID = companybranchID;
    }

    public String getSessionListID() {
        return SessionListID;
    }

    public void setSessionListID(String sessionListID) {
        SessionListID = sessionListID;
    }
}
