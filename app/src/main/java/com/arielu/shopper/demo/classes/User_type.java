package com.arielu.shopper.demo.classes;

public class User_type
{
    int userType_id ;
    String description ;

    public User_type(int userType_id, String description)
    {
        this.userType_id = userType_id;
        this.description = description;
    }

    public int getUserType_id() {
        return userType_id;
    }

    public void setUserType_id(int userType_id) {
        this.userType_id = userType_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
