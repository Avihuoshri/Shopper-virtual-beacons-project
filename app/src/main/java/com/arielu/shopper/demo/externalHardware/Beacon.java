package com.arielu.shopper.demo.externalHardware;

import com.arielu.shopper.demo.Map.DepartmentBlock;
import com.arielu.shopper.demo.NavigationElements.Point;

public class Beacon extends Point{

    public Beacon(int x, int y) {
        super(x, y);
    }

    public Beacon(String point) {
        super(point);
    }

    public Beacon(Point point) {
        super(point);
    }


    /*
    public static Point setWidthBeacon(DepartmentBlock departmentBlock1,DepartmentBlock departmentBlock2)
    {
        int x,y;
        boolean further = departmentBlock1.getLeftUpCorner().getY() >= departmentBlock2.getLeftUpCorner().getY();
        y = (departmentBlock1.getLeftUpCorner().getY() + departmentBlock2.getLeftUpCorner().getY() ) /2;
        boolean wider = departmentBlock1.getLeftUpCorner().getX() >= departmentBlock2.getLeftUpCorner().getX();
        x = ( departmentBlock1.getLeftUpCorner().getX() + departmentBlock2.getLeftUpCorner().getX() ) / 2;
        Point point = new Point(x,y);
        return point;
    }
     */
}
