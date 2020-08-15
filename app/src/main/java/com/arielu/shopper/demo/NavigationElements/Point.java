package com.arielu.shopper.demo.NavigationElements;

import java.io.Serializable;

public class Point implements Serializable {
    private int x;
    private int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point(String point){
        int splitIndex = point.indexOf(',');
        if(splitIndex != -1){
            String iString = point.substring(0,splitIndex);
            String jString = point.substring(splitIndex+1,point.length());
            this.x = Integer.parseInt(iString);
            this.y = Integer.parseInt(jString);
        }

    }
    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return
                " (" + x +
                "," + y +
                ')';
    }
}
