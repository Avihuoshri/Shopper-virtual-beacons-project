package com.arielu.shopper.demo.NavigationElements;

import java.io.Serializable;

public class Point implements Serializable, Comparable<Point> {
    private int x;
    private int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point(String point){
        int splitIndex = point.indexOf(',');
        if(splitIndex != -1){
            String xString = point.substring(0,splitIndex);
            String yString = point.substring(splitIndex+1,point.length());
            this.x = Integer.parseInt(xString);
            this.y = Integer.parseInt(yString);
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

    @Override
    public int compareTo(Point point) {
        return (int) Math.sqrt((this.x-point.getX())*(this.x-point.getX()) + (this.y-point.getY())*(this.y-point.getY()));
    }
}
