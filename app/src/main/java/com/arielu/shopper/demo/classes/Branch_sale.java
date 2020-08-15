package com.arielu.shopper.demo.classes;

public class Branch_sale
{
    int sale_id ;
    int quantity ;
    int product_id ;
    int branch_id ;
    double sale_price ;

    public Branch_sale(int sale_id, int quantity, int product_id, int branch_id, double sale_price)
    {
        this.sale_id = sale_id;
        this.quantity = quantity;
        this.product_id = product_id;
        this.branch_id = branch_id;
        this.sale_price = sale_price;
    }

    public int getSale_id() {
        return sale_id;
    }

    public void setSale_id(int sale_id) {
        this.sale_id = sale_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public double getSale_price() {
        return sale_price;
    }

    public void setSale_price(double sale_price) {
        this.sale_price = sale_price;
    }
}
