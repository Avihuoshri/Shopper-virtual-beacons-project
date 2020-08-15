package com.arielu.shopper.demo.classes;

public class Product_sub_category
{
    int sub_category_id ;
    String description ;
    String category_name ;

    public Product_sub_category(int sub_category_id, String description, String category_name) {
        this.sub_category_id = sub_category_id;
        this.description = description;
        this.category_name = category_name;
    }

    public int getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(int sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}

