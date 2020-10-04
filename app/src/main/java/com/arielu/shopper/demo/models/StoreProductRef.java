package com.arielu.shopper.demo.models;

public class StoreProductRef {

    private String productCode;
    private double price;

    private String productLocation;

    public StoreProductRef() {}

    public StoreProductRef(String pCode, double price, String productLocation)
    {
        setProductCode(pCode);
        setPrice(price);
        setProductLocation(productLocation);

    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }
}
