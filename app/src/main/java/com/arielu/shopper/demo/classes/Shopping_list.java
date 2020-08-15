package com.arielu.shopper.demo.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Shopping_list implements Serializable
{
    String shopping_list_id ;
    String user_id ;
    String shopping_list_title ;

    public Shopping_list(String shopping_list_id, String user_id, String shopping_list_title) {
        this.shopping_list_id = shopping_list_id;
        this.user_id = user_id;
        this.shopping_list_title = shopping_list_title;
    }

    public Shopping_list() {
        this.shopping_list_id = "";
        this.user_id = "";
        this.shopping_list_title = "";
    }

    public String getShopping_list_id() {
        return shopping_list_id;
    }

    public void setShopping_list_id(String shopping_list_id) {
        this.shopping_list_id = shopping_list_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getShopping_list_title() {
        return shopping_list_title;
    }

    public void setShopping_list_title(String shopping_list_title) {
        this.shopping_list_title = shopping_list_title;
    }

    @Override
    public String toString() {
        return shopping_list_title;

    }

}
