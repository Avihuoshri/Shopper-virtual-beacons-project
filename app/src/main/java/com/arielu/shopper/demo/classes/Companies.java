package com.arielu.shopper.demo.classes;

public class Companies
{
    int company_id ;
    String company_name ;

    public Companies(int company_id, String company_name) {
        this.company_id = company_id;
        this.company_name = company_name;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
