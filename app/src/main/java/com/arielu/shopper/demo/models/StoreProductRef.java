package com.arielu.shopper.demo.models;

public class StoreProductRef {

    private String productCode;
    private double price;

    public StoreProductRef() {}

    public StoreProductRef(String pCode, double price)
    {
        setProductCode(pCode);
        setPrice(price);
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


}
