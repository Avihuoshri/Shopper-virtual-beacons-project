package com.arielu.shopper.demo;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.arielu.shopper.demo.Map.Line;
import com.arielu.shopper.demo.Map.LineView;
import com.arielu.shopper.demo.NavigationAlgorithms.DiGraph;
import com.arielu.shopper.demo.NavigationAlgorithms.ShortestPath;
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
    private float width,height;
    ShortestPath shortestPathGenerator;
    DiGraph<Point> graph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        tracker = (PathTracker) intent.getSerializableExtra("tracker");
        sortedTracker = sortListforDrawingLines(tracker);
        mlineView = new LineView(this);
        mlineView.setBackgroundResource(R.drawable.map);
        setContentView(mlineView);
        width = (float) mlineView.getBackground().getIntrinsicWidth();
        height = (float) mlineView.getBackground().getIntrinsicHeight();
        mlineView.setFixWidth(width);
        mlineView.setFixHeight(height);
        fixPaths(sortedTracker);
        shortestPathGenerator = new ShortestPath();
        graph = shortestPathGenerator.buildFullGraph(mlineView.fixWidth);

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
                    Line line = new Line(pointA,pointB);
                    mlineView.addNewLine(line);
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
            //TODO - chenge this part to draw only shopping list path and not the all beacons graph
            for(int i = 0; i < graph.edges.size(); i++) {
                Point from = graph.edges.get(i).from.getValue();
                Point to = graph.edges.get(i).to.getValue();
                PointF beaconA = new PointF(from.getX(), from.getY());
                PointF beaconB = new PointF(to.getX(), to.getY());
                Line beaconLine = new Line(beaconA, beaconB);
                mlineView.addNewBeaconLine(beaconLine);
            }
        }
    }

}
