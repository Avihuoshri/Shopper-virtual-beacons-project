package com.arielu.shopper.demo.Map;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.arielu.shopper.demo.externalHardware.Beacon;

import java.util.ArrayList;
import java.util.LinkedList;

import Gui.DepartmentBlockDrawer;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LineView extends View {

    private Paint paintLines = new Paint();
    private Paint paintPoints = new Paint();
    private Paint paintBeacons = new Paint();
    public float fixWidth;
    public float fixHeight;

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
        paintBeacons.setColor(Color.GREEN);


        paintBeacons.setStrokeWidth(20);
        fixWidth = (float) this.getBackground().getIntrinsicWidth();
        fixHeight = (float) this.getBackground().getIntrinsicHeight();

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
        BeaconSetter beaconSetter = new BeaconSetter();
        beaconSetter.initBeacons(fixWidth/700);
        // draw beacons
        drawBeacons(canvas,beaconSetter);
        //draw points


        super.onDraw(canvas);
    }
    private void drawBeacons(Canvas canvas,BeaconSetter beaconSetter)
    {
        PointF beacon_draw;
        for(Beacon beacon : beaconSetter.beacons)
        {
            beacon_draw = new PointF();
            beacon_draw.x = beacon.getX() ;//* (420/160) - 160;
            beacon_draw.y = beacon.getY() ;//* (420/160) + 320;
            float x_adjust = ((float) (beacon.getX()) / 700);
            float y_adjust = ((float) (beacon.getY()) / 688); // Todo  -> validate!
            //float fixW = fixWidth/700;
            //float fixH = fixHeight/688;
            int fixed_x = (int) (x_adjust * (fixWidth - 106));
            int fixed_y = (int) (y_adjust * (fixHeight - 79));
            canvas.drawPoint(fixed_x  ,fixed_y ,paintBeacons);
        }
    }

    public void setFixWidth(float fixWidth) {
        this.fixWidth = fixWidth;
    }

    public void setFixHeight(float fixHeight) {
        this.fixHeight = fixHeight;
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

