package com.arielu.shopper.demo;

import java.util.Map;

/**
 * Item contains all the data of each item.
 * Example: name,price,sales,onSale,imageId,itemId
 */
public class Item {
    String name,sales,imageId,itemId;
    boolean onSale;
    double price;
    int units;

    public Item(){
        // empty constructor for firebase.
    }

    public Item(String name,double price){
        this.name = name;
        this.price = price;
    }

    public static Item createFromMap(Map<String,Object> map)
    {
        Item I  = new Item((String) map.get("name"),(Double) map.get("price"));

        I.setItemId((String) map.get("itemId"));

        return I;
    }

    public Item(String name, String sales, String imageId, String itemId, boolean onSale, double price, int units) {
        this.name = name;
        this.sales = sales;
        this.imageId = imageId;
        this.itemId = itemId;
        this.onSale = onSale;
        this.price = price;
        this.units = units;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
