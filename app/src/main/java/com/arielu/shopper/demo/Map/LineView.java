package com.arielu.shopper.demo.Map;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

import com.arielu.shopper.demo.NavigationAlgorithms.ShortestPath;

import com.arielu.shopper.demo.externalHardware.Beacon;

import java.util.ArrayList;

public class LineView extends View {

    private Paint paintLines = new Paint();
    private Paint paintPoints = new Paint();
    private Paint paintBeacons = new Paint();
    private Paint paintBeaconsLines = new Paint();
    private Paint paintDepartments = new Paint();
    private Paint paintDepartmentsLines = new Paint();

    public float fixWidth;
    public float fixHeight;

    private ArrayList<Line> lines  = new ArrayList<>() ;
    private ArrayList<Line> beaconLines  = new ArrayList<>() ;
    private ArrayList<Line> departmentBlockLines  = new ArrayList<>() ;

    public LineView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ShortestPath shortestPath = new ShortestPath() ;
        shortestPath.initDepartments();
        paintLines.setColor(Color.BLUE);
        paintLines.setStrokeWidth(7);

        paintPoints.setColor(Color.RED);
        paintPoints.setStrokeWidth(15);

        paintBeacons.setColor(Color.MAGENTA);

        paintBeaconsLines.setColor(Color.BLACK);
        paintBeacons.setStrokeWidth(7);

        paintDepartments.setColor(Color.BLACK);
        paintDepartments.setStrokeWidth(13);
        paintDepartmentsLines.setColor(Color.RED);
        paintDepartmentsLines.setStrokeWidth(7);


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
            canvas.drawPoint(pointA.x, pointA.y, paintPoints);
            canvas.drawPoint(pointB.x, pointB.y, paintPoints);
        }

        for (int i = 1; i < beaconLines.size() ; i++) {
            PointF pointA = new PointF();
            PointF pointB = new PointF();

            pointA.x = beaconLines.get(i).pointA.x ;
            pointA.y = beaconLines.get(i).pointA.y;
            pointB.x = beaconLines.get(i).pointB.x;
            pointB.y = beaconLines.get(i).pointB.y ;

            canvas.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, paintBeaconsLines);
            canvas.drawPoint(pointA.x, pointA.y, paintBeacons);
            canvas.drawPoint(pointB.x, pointB.y, paintBeacons);
        }

        BeaconSetter beaconSetter = new BeaconSetter();
        beaconSetter.init();
        // draw beacons
        drawBeacons(canvas,beaconSetter);
        //draw points

        drawDepatments(shortestPath.getDepartments() , canvas) ;
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
//            float x_adjust = ((float) (beacon.getX()) / 700);
//            float y_adjust = ((float) (beacon.getY()) / 688); // Todo  -> validate!
            //float fixW = fixWidth/700;
            //float fixH = fixHeight/688;
//            int fixed_x = (int) (x_adjust * (fixWidth - 106));
//            int fixed_y = (int) (y_adjust * (fixHeight - 79));
            canvas.drawPoint(beacon_draw.x  ,beacon_draw.y ,paintBeacons);
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

    public void drawDepatments(ArrayList<DepartmentBlock> departments , Canvas canvas) // Add new line to the array list
    {
        for(DepartmentBlock department : departments)
        {
            PointF leftDown = new PointF(department.getLeftDownCorner().getX() , department.getLeftDownCorner().getY());
            PointF leftUp = new PointF(department.getLeftUpCorner().getX() , department.getLeftUpCorner().getY());
            PointF rightDown = new PointF(department.getRightDownCorner().getX() , department.getRightDownCorner().getY());
            PointF rightUp = new PointF(department.getRightUpCorner().getX() , department.getRightUpCorner().getY());

            canvas.drawPoint(leftDown.x  ,leftDown.y ,paintDepartments);
            canvas.drawPoint(leftUp.x  ,leftUp.y ,paintDepartments);
            canvas.drawPoint(rightDown.x  ,rightDown.y ,paintDepartments);
            canvas.drawPoint(rightUp.x  ,rightUp.y ,paintDepartments);


            canvas.drawLine(leftDown.x  ,leftDown.y , leftUp.x  ,leftUp.y ,paintDepartmentsLines);
            canvas.drawLine(rightDown.x  ,rightDown.y , rightUp.x  ,rightUp.y ,paintDepartmentsLines);
            canvas.drawLine(leftDown.x  ,leftDown.y , rightDown.x  ,rightDown.y ,paintDepartmentsLines);
            canvas.drawLine(rightUp.x  ,rightUp.y , leftUp.x  ,leftUp.y ,paintDepartmentsLines);
        }
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

