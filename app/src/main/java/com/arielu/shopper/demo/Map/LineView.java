package com.arielu.shopper.demo.Map;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

import com.arielu.shopper.demo.NavigationAlgorithms.ShortestPath;

import com.arielu.shopper.demo.NavigationElements.Point;
import com.arielu.shopper.demo.externalHardware.Beacon;

import java.util.ArrayList;

public class LineView extends View {

    private Paint paintLines = new Paint();
    private Paint paintPoints = new Paint();
    private Paint paintBeacons = new Paint();
    private Paint paintBeaconsLines = new Paint();
    private Paint paintDepartments = new Paint();
    private Paint paintDepartmentsLines = new Paint();
    private Paint circlePaint = new Paint();
    private BeaconSetter beaconSetter = new BeaconSetter();
    public float fixWidth;
    public float fixHeight;

    private ArrayList<Line> lines  = new ArrayList<>() ;
    private ArrayList<Line> beaconLines  = new ArrayList<>() ;
    private ArrayList<Line> departmentBlockLines  = new ArrayList<>() ;
    private int radius = 90 ;
    public LineView(Context context) {
        super(context);
    }
    boolean firstDraw = true ;
    @Override
    protected void onDraw(Canvas canvas) {
        ShortestPath shortestPath = new ShortestPath() ;
        shortestPath.initDepartments();
        paintLines.setColor(Color.BLUE);
        paintPoints.setColor(Color.RED);
        paintBeacons.setColor(Color.BLUE);
        paintBeaconsLines.setColor(Color.BLACK);
        paintDepartments.setColor(Color.BLACK);
        paintDepartmentsLines.setColor(Color.RED);

        paintBeaconsLines.setStrokeWidth(5);
        paintBeacons.setStrokeWidth(12);
        paintPoints.setStrokeWidth(15);
        paintLines.setStrokeWidth(7);
        circlePaint.setStrokeWidth(2);
        paintDepartments.setStrokeWidth(13);
        paintDepartmentsLines.setStrokeWidth(7);

        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.parseColor("#777170"));
       if (firstDraw) {
           beaconSetter.init();
           firstDraw = false ;
       }


        //draw lines
        drawRouts(canvas);

        drawBeaconsRouts(canvas) ;
        // draw beacons
        drawBeacons(canvas,beaconSetter);
        drawBeaconsRanges(canvas) ;

//        drawDepatments(shortestPath.getDepartments() , canvas) ;
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
            if(beacon.getIsInRange() == true)
            {
                paintBeacons.setColor(Color.GREEN);
            }
            canvas.drawPoint(beacon_draw.x  ,beacon_draw.y ,paintBeacons);
            paintBeacons.setColor(Color.BLUE);
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

//    public void drawDepatments(ArrayList<DepartmentBlock> departments , Canvas canvas) // Add new line to the array list
//    {
//        for(DepartmentBlock department : departments)
//        {
//            PointF leftDown = new PointF(department.getLeftDownCorner().getX() , department.getLeftDownCorner().getY());
//            PointF leftUp = new PointF(department.getLeftUpCorner().getX() , department.getLeftUpCorner().getY());
//            PointF rightDown = new PointF(department.getRightDownCorner().getX() , department.getRightDownCorner().getY());
//            PointF rightUp = new PointF(department.getRightUpCorner().getX() , department.getRightUpCorner().getY());
//
//            canvas.drawPoint(leftDown.x  ,leftDown.y ,paintDepartments);
//            canvas.drawPoint(leftUp.x  ,leftUp.y ,paintDepartments);
//            canvas.drawPoint(rightDown.x  ,rightDown.y ,paintDepartments);
//            canvas.drawPoint(rightUp.x  ,rightUp.y ,paintDepartments);
//
//
//            canvas.drawLine(leftDown.x  ,leftDown.y , leftUp.x  ,leftUp.y ,paintDepartmentsLines);
//            canvas.drawLine(rightDown.x  ,rightDown.y , rightUp.x  ,rightUp.y ,paintDepartmentsLines);
//            canvas.drawLine(leftDown.x  ,leftDown.y , rightDown.x  ,rightDown.y ,paintDepartmentsLines);
//            canvas.drawLine(rightUp.x  ,rightUp.y , leftUp.x  ,leftUp.y ,paintDepartmentsLines);
//        }
//    }



private void drawBeaconsRanges(Canvas canvas)
{

    for (Beacon beacon: beaconSetter.beacons) {
        if(beacon.getIsInRange() == true)
        {
            circlePaint.setColor(Color.RED);
            canvas.drawCircle(beacon.getX() , beacon.getY() , radius , circlePaint);
        }

        circlePaint.setColor(Color.parseColor("#777170"));
    }
}

private void drawBeaconsRouts(Canvas canvas)
{
    for (int i = 1; i < beaconLines.size() ; i++) {
        PointF pointA = new PointF();
        PointF pointB = new PointF();

        pointA.x = beaconLines.get(i).pointA.x ;
        pointA.y = beaconLines.get(i).pointA.y;
        pointB.x = beaconLines.get(i).pointB.x;
        pointB.y = beaconLines.get(i).pointB.y ;

        canvas.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, paintBeaconsLines);
//        canvas.drawPoint(pointA.x, pointA.y, paintBeacons);
//        canvas.drawPoint(pointB.x, pointB.y, paintBeacons);
    }
}

private void drawRouts(Canvas canvas)
{
    for (int i = 1; i < lines.size() ; i++) {
        PointF pointA = new PointF();
        PointF pointB = new PointF();

        pointA.x = lines.get(i).pointA.x ;
        pointA.y = lines.get(i).pointA.y ;
        pointB.x = lines.get(i).pointB.x ;
        pointB.y = lines.get(i).pointB.y ;

      //  canvas.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, paintLines);
        canvas.drawPoint(pointA.x, pointA.y, paintPoints);
        canvas.drawPoint(pointB.x, pointB.y, paintPoints);
    }
}



public void customerLocation(Point point , ArrayList<Beacon> beacons)
{
    beaconSetter.setBeaconsArraylist(beacons);
    for (Beacon beacon:beaconSetter.beacons) {
        beacon.changeIfInRange(point , radius);
    }
   draw();
}
    public void draw()
    {
        invalidate();
        requestLayout();
    }

    public void addNewBeaconLine(Line beaconLine) {
        beaconLines.add(beaconLine) ;
    }
}

