package com.arielu.shopper.demo.Map;

import com.arielu.shopper.demo.NavigationElements.Point;

public class DepartmentBlock {

    private Point leftDownCorner;
    private Point leftUpCorner;
    private Point rightDownCorner;
    private Point rightUpCorner;

    public void setLeftDownCorner(Point leftDownCorner) {
        this.leftDownCorner = leftDownCorner;
    }

    public void setRightUpCorner(Point rightUpCorner) {
        this.rightUpCorner = rightUpCorner;
    }

    public void setRightDownleftUpCorners() {
        try {
            if ((leftDownCorner == null) || (rightUpCorner == null)) {
                throw new Exception("must initaialize left down corner and right up corner");
            }
            this.leftUpCorner = new Point(leftDownCorner.getX(), rightUpCorner.getY());
            this.rightDownCorner = new Point(rightUpCorner.getX(), leftDownCorner.getY());
        }

        catch (Exception e){
            e.printStackTrace();
        }


    }


    public Point getLeftUpCorner() {
        return leftUpCorner;
    }

    public Point getRightDownCorner() {
        return rightDownCorner;
    }

    public Point getRightUpCorner() {
        return rightUpCorner;
    }

    public Point getLeftDownCorner() {
        return leftDownCorner;
    }

}
