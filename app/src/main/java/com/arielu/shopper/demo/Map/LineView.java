package com.arielu.shopper.demo.Map;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

import java.util.ArrayList;

public class LineView extends View {

    private Paint paintLines = new Paint();
    private Paint paintPoints = new Paint();
    private ArrayList<Line> lines  = new ArrayList<>() ;

    public LineView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paintLines.setColor(Color.BLUE);
        paintPoints.setColor(Color.RED);
        paintLines.setStrokeWidth(7);
        paintPoints.setStrokeWidth(15);

        //draw lines
        for (int i = 1; i < lines.size() ; i++) {
            PointF pointA = new PointF();
            PointF pointB = new PointF();

            pointA.x = lines.get(i).pointA.x ;
            pointA.y = lines.get(i).pointA.y ;
            pointB.x = lines.get(i).pointB.x ;
            pointB.y = lines.get(i).pointB.y ;

            canvas.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, paintLines);
            canvas.drawPoint(pointA.x, pointA.y, paintLines);
            canvas.drawPoint(pointB.x, pointB.y, paintPoints);
        }

        //draw points

        super.onDraw(canvas);
    }

    public void addNewLine(Line line) // Add new line to the array list
    {
        lines.add(line) ;
    }

    public void draw()
    {
        invalidate();
        requestLayout();
    }
}

