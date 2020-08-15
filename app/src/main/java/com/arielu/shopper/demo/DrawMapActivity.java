package com.arielu.shopper.demo;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.arielu.shopper.demo.Map.Line;
import com.arielu.shopper.demo.Map.LineView;
import com.arielu.shopper.demo.NavigationElements.Point;
import com.arielu.shopper.demo.NavigationElements.PathTracker;
import com.arielu.shopper.demo.NavigationElements.PathNode;
import com.arielu.shopper.demo.R;

public class DrawMapActivity extends AppCompatActivity {
    private PathTracker tracker;

    private Line line;
    private LineView mlineView;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_draw_map);
        intent = getIntent();
        tracker = (PathTracker) intent.getSerializableExtra("tracker");

        mlineView = new LineView(this);
        mlineView.setBackgroundResource(R.drawable.map);
        setContentView(mlineView);
        //mlineView = findViewById(R.id.LineView) ;

        addLines();

        mlineView.draw();

    }

    private void addLines() {
        PathNode node = tracker.list.head;
        if (!node.hasNext()) {
            //TODO: print in dialog message
            Log.d("error", "no products in the list");
        } else {
            //iterate the nodes in the list
            //node = node.getNext();
            while (node != tracker.list.tail) {//iterate path points
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
                    Line line = new Line(pointA, pointB);
                    mlineView.addNewLine(line);
                }

                node = node.getNext();
            }

            //iterate tail node points
            if (node != tracker.list.tail)
                Log.d("error: ", "not the tail");
            Log.d("tail path: ", node.toString());

            for (int i = 0; i < node.getPath().getPoints().size() - 1; i++) {

                Point pCurrent = node.getPath().getPoints().get(i);
                Point pNext = node.getPath().getPoints().get(i + 1);

                //create new line from two points in the path
                PointF pointA = new PointF(pCurrent.getX(), pCurrent.getY());
                PointF pointB = new PointF(pNext.getX(), pNext.getY());
                Line line = new Line(pointA, pointB);

                mlineView.addNewLine(line);


            }
        }
    }
}
