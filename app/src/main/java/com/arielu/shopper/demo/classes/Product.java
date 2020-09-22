package com.arielu.shopper.demo.classes;

import android.graphics.Bitmap;
import android.os.Bundle;

import java.io.Serializable;

import androidx.annotation.Nullable;

public class Product implements Serializable
{
    protected String productCode ;
    protected String categoryName ;
    protected String productName ;
    protected Double productPrice ;
    protected String productImageUrl ;
    protected String productManufacturer ;
    protected Bitmap productImage;

    protected String productLocation ;

    // firebase needs empty constructor
    public Product() { }

    public Product(String productCode, String categoryName, String productName, Double productPrice, String productImageUrl, String productManufacturer, String productLocation)
    {
        setProductCode(productCode);
        setCategoryName(categoryName);
        setProductName(productName);
        setProductPrice(productPrice);
        setProductImageUrl(productImageUrl);
        setProductManufacturer(productManufacturer);
        setProductLocation(productLocation);
    }

    public Product(Product other)
    {
        setProductCode(other.getProductCode());
        setCategoryName(other.getCategoryName());
        setProductName(other.getProductName());
        setProductPrice(other.getProductPrice());
        setProductImageUrl(other.getProductImageUrl());
        setProductManufacturer(other.getProductManufacturer());
        setProductLocation(other.getProductLocation());

        if(other.ProductImage()!=null)
            productImage = Bitmap.createBitmap(other.ProductImage());

    }

    public Bundle toBundle()
    {
        Bundle b = new Bundle();
        b.putString("productCode",getProductCode());
        b.putString("categoryName",getCategoryName());
        b.putString("productName",getProductName());
        b.putDouble("productPrice",getProductPrice());
        b.putString("productImageUrl",getProductImageUrl());
        b.putString("productManufacturer",getProductManufacturer());
        b.putParcelable("productImage",ProductImage());
        b.putString("productLocation",getProductLocation());

        return b;
    }

    public static Product fromBundle(Bundle b)
    {
        Product p = new Product(
                b.getString("productCode"),
                b.getString("categoryName"),
                b.getString("productName"),
                b.getDouble("productPrice"),
                b.getString("productImageUrl"),
                b.getString("productManufacturer"),
                b.getString("productLocation")
        );
        p.setProductImage(b.getParcelable("productImage"));

        return p;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String product_name) {
        this.productName = product_name;
    }

    public Double getProductPrice() { return productPrice; }

    public void setProductPrice(Double productPrice) { this.productPrice = productPrice; }

    public String getProductImageUrl() { return productImageUrl; }

    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public String getProductManufacturer() { return productManufacturer; }

    public void setProductManufacturer(String productManufacturer) { this.productManufacturer = productManufacturer; }

    public Bitmap ProductImage() { return productImage; }

    public void setProductImage(Bitmap ProductImage) { this.productImage = ProductImage; }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        Product other = (Product) obj;
        return (other.getProductCode().equals(getProductCode()));
    }

    @Override
    public String toString() {
        return  productName + " | " +
                productPrice + " | " +
                 productManufacturer;
    }
}