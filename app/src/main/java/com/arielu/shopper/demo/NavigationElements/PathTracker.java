package com.arielu.shopper.demo.NavigationElements;


import java.io.Serializable;
import java.util.ArrayList;

public class PathTracker implements Serializable {
    public Point startPoint;
    public Path firstPath;
    public CycleLinkedList list;

    public PathTracker() {
        initList();
    }

    private void initList() {
        startPoint = new Point(250, 660);
        firstPath = new Path();
        list = new CycleLinkedList();

        firstPath.add(startPoint);
        list.add(firstPath);
        list.head.setSource("enter");
        list.head.setDestination("enter");
    }

    public void saveRoute(ArrayList<Float> degrees, String destination) {

        //get last point
        Path lastPath = list.tail.getPath();
        int lastRouteSize = lastPath.size();
        Point lastPoint = lastPath.getPoints().get(lastRouteSize - 1);

        //set first point of new path
        Path path = new Path();
        path.add(lastPoint);

        //add points to new path
        for (int i = 0; i < degrees.size(); i++) {
            Point point = path.DegreeToPoint(degrees.get(i));
            path.add(point);
        }

        list.add(path);

        //set source and destination
        String source = list.tail.prev.getDestination();
        list.tail.setSource(source);
        list.tail.setDestination(destination);

    }

    public String toString() {
        return "PathTracker{" +
                " list = " + list.toString() +
                '}';
    }

    public String ProductsToString() {
        return list.sourceDestToString();
    }

    public CycleLinkedList getList() {
        return list;
    }
}
