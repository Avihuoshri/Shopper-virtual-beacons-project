package com.arielu.shopper.demo.models;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String title,content,date;

    //for firebase
    private Message(){}

    public Message(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String retrieveParsedDate()
    {
        Timestamp ts = new Timestamp(Long.parseLong(getDate()));
        Date date = new Date(ts.getTime());
        DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        return f.format(date);
    }
}
