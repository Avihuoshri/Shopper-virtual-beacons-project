package com.arielu.shopper.demo;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.arielu.shopper.demo.Map.Line;
import com.arielu.shopper.demo.Map.LineView;
import com.arielu.shopper.demo.NavigationElements.Path;
import com.arielu.shopper.demo.NavigationElements.Point;
import com.arielu.shopper.demo.NavigationElements.PathTracker;
import com.arielu.shopper.demo.NavigationElements.PathNode;
import com.arielu.shopper.demo.R;

import java.util.ArrayList;

public class DrawMapActivity extends AppCompatActivity {
    private PathTracker tracker;
    PathTracker sortedTracker = new PathTracker();
    private Line line;
    private LineView mlineView;
    private Intent intent;
    private int width,height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        tracker = (PathTracker) intent.getSerializableExtra("tracker");
        sortedTracker = sortListforDrawingLines(tracker);
        mlineView = new LineView(this);
        mlineView.setBackgroundResource(R.drawable.map);
        setContentView(mlineView);
        width = mlineView.getBackground().getIntrinsicWidth();
        height = mlineView.getBackground().getIntrinsicHeight();
        mlineView.setFixWidth(width);
        mlineView.setFixHeight(height);
        fixPaths(sortedTracker);
        addLines();
        mlineView.draw();
    }

    private PathTracker sortListforDrawingLines(PathTracker tracker) {
        PathTracker tempTracker = new PathTracker();
        tempTracker.firstPath = null;
        ArrayList<Point> points = new ArrayList<>();

        PathNode node = tracker.list.head;
        Path path = new Path();
        while (node != tracker.list.tail) {//iterate path points
            path = node.getPath();
            for(Point p : path.getPoints()){
                points.add(p);
            }
            node = node.next;
        }
        //tail
        path = node.getPath();
        for(Point p : path.getPoints()){
            points.add(p);
        }

        int i = 0;
        while( i < ( points.size() - 1)){
            path = new Path();
            path.add(points.get(i));
            path.add(points.get(i + 1));
            tempTracker.list.add(path);
            i++;
        }
        //points list size is not even
        int isEven = ((points.size() + 1) % 2);
        if(isEven == 0){
            path = new Path();
            path.add(points.get(i - 1));
            path.add(points.get(i));
            tempTracker.list.add(path);
        }
        return tempTracker;
    }

    private void fixPaths(PathTracker pathTracker)
    {
        // get list of points between two PathNodes

        PathNode pathNode = tracker.list.head;
        Path path;
        ArrayList<Point> points2fix = new ArrayList<>();
        while (pathNode != tracker.list.tail) {//iterate path points
            path = pathNode.getPath();
            for(Point p : path.getPoints()){
                points2fix.add(p);
            }
            pathNode = pathNode.next;
        }
    }
    private PointF lineFixer(PointF a,PointF b)
    {
        float x = (a.x+b.x)/2;
        float y = (a.y+b.y)/2;
        PointF mid = new PointF(x,y);
        return mid;
    }
    private void addLines() {
        PathNode node = sortedTracker.list.head;
        if (!node.hasNext()) {
            //TODO: print in dialog message
            Log.d("error", "no products in the list");
        } else {
            //iterate the nodes in the list
            //node = node.getNext();
            while (node != sortedTracker.list.tail) {//iterate path points
                int pathSize = node.getPath().getPoints().size();
                for (int i = 0; i <= (pathSize - 1); i++) {
                    Point pCurrent = node.getPath().getPoints().get(i);
                    Point pNext;

                    if( i < pathSize - 1) {
                        pNext = node.getPath().getPoints().get(i + 1);
                    }
                    else{
                        pNext = pCurrent;
                    }

                    //create new line from two points in the path
                    PointF pointA = new PointF(pCurrent.getX(), pCurrent.getY());
                    PointF pointB = new PointF(pNext.getX(), pNext.getY());
                    //PointF mid = lineFixer(pointA,pointB);
                    //Line line1 = new Line(pointA, mid);
                    //Line line2 = new Line(pointB,mid);
                    Line line = new Line(pointA,pointB);
                    mlineView.addNewLine(line);
                    //mlineView.addNewLine(line2);
                }

                node = node.getNext();
            }

            //iterate tail node points
            for (int i = 0; i < node.getPath().getPoints().size() - 1; i++) {

                Point pCurrent = node.getPath().getPoints().get(i);
                Point pNext = node.getPath().getPoints().get(i + 1);

                PointF pointA = new PointF(pCurrent.getX(), pCurrent.getY());
                PointF pointB = new PointF(pNext.getX(), pNext.getY());
                Line line = new Line(pointA, pointB);

                mlineView.addNewLine(line);


            }
        }
    }
}
