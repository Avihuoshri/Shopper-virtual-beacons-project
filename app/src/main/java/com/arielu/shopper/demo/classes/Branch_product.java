package com.arielu.shopper.demo.classes;

public class Branch_product
{
    int product_id ;
    int branch_id ;
    double price ;

    public Branch_product(int product_id, int branch_id, double price) {
        this.product_id = product_id;
        this.branch_id = branch_id;
        this.price = price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
