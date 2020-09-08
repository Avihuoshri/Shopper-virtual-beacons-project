package com.arielu.shopper.demo.models;

import android.os.Bundle;

import com.arielu.shopper.demo.classes.Product;

public class SessionProduct extends Product {

    //////////////////////////////
    ///////// Properties /////////
    //////////////////////////////

    protected Boolean isBought = false;
    protected int quantity;

    //////////////////////////////
    //////// Constructors ////////
    //////////////////////////////

    // Empty Constructor for firebase
    private SessionProduct() {}

    public SessionProduct(String productCode , String categoryName , String productName, Double productPrice, String productImageUrl, String productManufacturer, String productLocation)
    {
        super(productCode,categoryName,productName,productPrice,productImageUrl,productManufacturer,productLocation);
    }

    // Copy Constructor
    public SessionProduct(Product otherProd)
    {
        super(otherProd);
    }

    public SessionProduct(SessionProduct other)
    {
        super(other);
        setIsBought(other.getIsBought());
        setQuantity(other.getQuantity());
    }


    /////////////////////////////////////
    ///////// Getters & Setters /////////
    /////////////////////////////////////

    public Boolean getIsBought() { return isBought; }
    public void setIsBought(Boolean bought) { isBought = bought; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /////////////////////////////////////
    ///////// Format converters /////////
    /////////////////////////////////////

    @Override
    public Bundle toBundle()
    {
        Bundle b = super.toBundle();
        b.putBoolean("isBought", getIsBought());
        b.putInt("quantity", getQuantity());

        return b;
    }

    public static SessionProduct fromBundle(Bundle b)
    {
        Product p = Product.fromBundle(b);
        SessionProduct sp = new SessionProduct(p);

        sp.setIsBought(b.getBoolean("isBought"));
        sp.setQuantity(b.getInt("quantity"));

        return sp;
    }

    //////////////////////////////////////////////
    ///////// Class Methods & operations /////////
    //////////////////////////////////////////////

    public double computeTotalPrice()
    {
        return getProductPrice() * getQuantity();
    }
}
