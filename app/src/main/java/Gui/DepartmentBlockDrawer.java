package Gui;

import android.graphics.PointF;

import com.arielu.shopper.demo.Map.DepartmentBlock;
import com.arielu.shopper.demo.senssors.stepDetector;

import java.util.LinkedList;

public class DepartmentBlockDrawer {


    public LinkedList<PointF> drawAllBlocks()
    {
        stepDetector stepDetector = new stepDetector();
        stepDetector.initDepartments();
        LinkedList<PointF> ans = new LinkedList<>();
        for (DepartmentBlock departmentBlock : stepDetector.getDepartments())
        {
            ans.add(new PointF(departmentBlock.getLeftUpCorner().getX(),departmentBlock.getLeftUpCorner().getY()));
            ans.add(new PointF(departmentBlock.getLeftDownCorner().getX(),departmentBlock.getLeftDownCorner().getY()));
            ans.add(new PointF(departmentBlock.getRightUpCorner().getX(),departmentBlock.getRightUpCorner().getY()));
            ans.add(new PointF(departmentBlock.getRightDownCorner().getX(),departmentBlock.getRightDownCorner().getY()));
        }
        return ans;
    }
}
