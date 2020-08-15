package com.arielu.shopper.demo.NavigationElements;

import java.io.Serializable;
import java.util.ArrayList;

public class Path implements Serializable {
    private ArrayList<Point> points;

    public Path() {
        this.points = new ArrayList<>();
    }

    public Path(Path other)
    {
        points = new ArrayList<>();
        for (Point p : other.getPoints())
        {
            points.add(p);
        }
    }

    //TODO: check borders method + check valid walking way (by pixels)

    //convert degree that comes from the senssor to point on the route
    public Point DegreeToPoint(float degree){
        //get the coordinate of the lest point on the route
        if(points.size() != 0) {
            int x = points.get(points.size()-1).getX();
            int y = points.get(points.size()-1).getY();

            //not a valid degree
            if (degree < 0 || degree > 360) {
                return null;
            }
            // 1) go up on the map
            else if (degree < 26 || degree > 339) {
                return new Point(x - 1, y);
            }
            // 2) go right up diagonal on the map
            else if (degree < 64) {
                return new Point(x - 1, y + 1);
            }
            // 3) go right on the map
            else if (degree < 109) {
                return new Point(x, y + 1);
            }
            // 4) go right down diagonal on the map
            else if(degree < 154){
                return new Point(x + 1, y + 1);
            }
            // 5) go down on the map
            else if(degree < 199){
                return new Point(x + 1, y);
            }
            // 6) go left down diagonal on the map
            else if(degree < 244){
                return new Point(x + 1, y - 1);
            }
            // 7) go left on the map
            else if(degree < 289){
                return new Point(x, y - 1);
            }
            // 8) go left up diagonal on the map
            else{
                return new Point(x - 1, y - 1);
            }
        }
        return  null;
    }

    public void add(Point point) {
        points.add(point);
    }

    public int size(){
        return points.size();
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public String toString() {
        return "Path{" +
                "points=" + points.toString() +
                ", size = " + points.size() +
                '}';
    }

}
