package com.arielu.shopper.demo.NavigationAlgorithms;


import com.arielu.shopper.demo.Map.BeaconSetter;
import com.arielu.shopper.demo.Map.DepartmentBlock;
import com.arielu.shopper.demo.NavigationElements.PathTracker;
import com.arielu.shopper.demo.NavigationElements.Point;
import com.arielu.shopper.demo.externalHardware.Beacon;


import java.util.ArrayList;
import java.util.List;

public class ShortestPath
{
    private ArrayList<DepartmentBlock> departments;
    public ShortestPath()
    {

    }

//    //This function gets ArrayList of String and make it a Pathnodes ArrayList.
//    private ArrayList<PathNode> stringPathsToPathNodes(ArrayList<String> stringPaths){
//        ArrayList<PathNode> paths = new ArrayList<>();
//        PathParser parser = new PathParser();
//        for(String stringPath : stringPaths){
//            PathNode path = parser.parse(stringPath);
//            paths.add(path);
//        }
//        return paths;
//    }
//
//    //This function compare the client's current path to the DB saved path between each 2 nodes
//    //and update the DB path if the client walked in a shorter path
//    public ArrayList<PathNode> updateDbPaths(PathTracker clientPath  , ArrayList<PathNode> paths)
//    {
//        for (PathNode node : paths )
//        {
//            PathNode currentNode = clientPath.list.head ;
//            while(currentNode.hasNext())
//            {
//                String cnSrc = currentNode.getSource() ;      //cnSrc = current node source
//                String cnDst = currentNode.getDestination() ; //cnDst = current node destination
//                String alnSrc = node.getSource() ;            //alnSrc = array list node's source
//                String alnDst = node.getDestination() ;       //alnDst = array list node's sourdestinationce
//
//
//                if(cnSrc.equals(alnSrc) && cnDst.equals(alnDst))
//                {
//                    int clientPathSize = currentNode.getPath().size() ;
//                    int nodePathSize = node.getPath().size() ;
//                    if(clientPathSize < nodePathSize)
//                    {
//                        node.setPath(currentNode.getPath());
//                    }
//                }
//            }
//
//        }
//        return paths ;
//    }

    // Given three colinear points p, q, r, the function checks if
// point q lies on line segment 'pr'
    static boolean onSegment(Point p, Point q, Point r)
    {
        if (q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) &&
                q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY()))
            return true;

        return false;
    }

    // To find orientation of ordered triplet (p, q, r).
// The function returns following values
// 0 --> p, q and r are colinear
// 1 --> Clockwise
// 2 --> Counterclockwise
    static int orientation(Point p, Point q, Point r)
    {
        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
        // for details of below formula.
        int val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0) return 0; // colinear

        return (val > 0)? 1: 2; // clock or counterclock wise
    }

    // The main function that returns true if line segment 'p1q1'
// and 'p2q2' intersect.
    static boolean doIntersect(Point p1, Point q1, Point p2, Point q2)
    {
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Doesn't fall in any of the above cases
    }

    // build graph node from current products list and beacons points
    static ArrayList<Point> addGraphNodes(float fixWidth){

        BeaconSetter beaconSetter = new BeaconSetter();
        beaconSetter.initBeacons(fixWidth / 700);
        ArrayList<Point> graphNodes = new ArrayList<>();

        for(Beacon beacon : beaconSetter.beacons) {
            Point beaconPoint = new Point(beacon.getX(), beacon.getY());
            graphNodes.add(beaconPoint);
        }

        return graphNodes;
    }

    //use doIntersect function to build only the right edges
    //use DiGraph.add to build full graph with edges
    public DiGraph<Point> buildFullGraph(float fixWidth){
        DiGraph<Point> graph = new DiGraph<>();
        ArrayList<Point> graphNodes = addGraphNodes(fixWidth);
        initDepartments();
        for(int a = 0; a < graphNodes.size(); a++){
            for(int b = 0; b < graphNodes.size(); b++){
                for (DepartmentBlock department : departments){
                    if(a < b) {
                        Point from = graphNodes.get(a);
                        Point to = graphNodes.get(b);

                        Boolean rectangleEdgeOne = (doIntersect(from, to, department.getLeftDownCorner(), department.getLeftUpCorner()));
                        Boolean rectangleEdgeTwo = (doIntersect(from, to, department.getLeftUpCorner(), department.getRightUpCorner()));
                        Boolean rectangleEdgeThree = (doIntersect(from, to, department.getRightUpCorner(), department.getRightDownCorner()));
                        Boolean rectangleEdgeFour = (doIntersect(from, to, department.getRightDownCorner(), department.getLeftDownCorner()));

                        if (rectangleEdgeOne || rectangleEdgeTwo || rectangleEdgeThree || rectangleEdgeFour) {
                            continue;
                        }
                        else{
                            graph.add(from, to, from.compareTo(to));
                        }
                    }
                }
            }
        }
        return graph;
    }

    //use DiGraph.getPath to get shortestPath
    public List<String> getShortestPath(Point from, Point to,float fixWidth){
        DiGraph<Point> graph = buildFullGraph(fixWidth);
        return graph.getPath(from, to);
    }


    public void initDepartments(){
        departments = new ArrayList<>();


        //florist Department
        DepartmentBlock florist = new DepartmentBlock();
        Point leftDown = new Point(60,590);
        Point rightUp = new Point(110,570);
        florist.setLeftDownCorner(leftDown);
        florist.setRightUpCorner(rightUp);
        florist.setRightDownleftUpCorners();

        departments.add(florist);



        //PRODUCE Department
        DepartmentBlock p1 = new DepartmentBlock();
        DepartmentBlock p2 = new DepartmentBlock();
        DepartmentBlock p3 = new DepartmentBlock();
        DepartmentBlock p4 = new DepartmentBlock();
        DepartmentBlock p5 = new DepartmentBlock();
        DepartmentBlock p6 = new DepartmentBlock();
        DepartmentBlock p7 = new DepartmentBlock();
        //DepartmentBlock p8 = new DepartmentBlock();

        p1.setLeftDownCorner(new Point(62,508));
        p1.setRightUpCorner(new Point(84 ,491));
        p1.setRightDownleftUpCorners();

        p2.setLeftDownCorner(new Point(120,508));
        p2.setRightUpCorner(new Point(141,490));
        p2.setRightDownleftUpCorners();

        p3.setLeftDownCorner(new Point(62,458));
        p3.setRightUpCorner(new Point(84,438));
        p3.setRightDownleftUpCorners();

        p4.setLeftDownCorner(new Point(120,456));
        p4.setRightUpCorner(new Point(141,438));
        p4.setRightDownleftUpCorners();

        p5.setLeftDownCorner(new Point(62,398));
        p5.setRightUpCorner(new Point(84,385));
        p5.setRightDownleftUpCorners();

        p6.setLeftDownCorner(new Point(120,390));
        p6.setRightUpCorner(new Point(141,373));
        p6.setRightDownleftUpCorners();

        p7.setLeftDownCorner(new Point(65,340));
        p7.setRightUpCorner(new Point(140,321));
        p7.setRightDownleftUpCorners();

        p7.setLeftDownCorner(new Point(65,286));
        p7.setRightUpCorner(new Point(139,265));
        p7.setRightDownleftUpCorners();

        departments.add(p1);
        departments.add(p2);
        departments.add(p3);
        departments.add(p4);
        departments.add(p5);
        departments.add(p6);
        departments.add(p7);
        //departments.add(p8);


        //BULK Department
        DepartmentBlock b1 = new DepartmentBlock();
        DepartmentBlock b2 = new DepartmentBlock();
        DepartmentBlock b3 = new DepartmentBlock();
        DepartmentBlock b4 = new DepartmentBlock();

        b1.setLeftDownCorner(new Point(65,225));
        b1.setRightUpCorner(new Point(108,210));
        b1.setRightDownleftUpCorners();

        b2.setLeftDownCorner(new Point(136 ,235));
        b2.setRightUpCorner(new Point(155,204));
        b2.setRightDownleftUpCorners();

        b3.setLeftDownCorner(new Point(64,170));
        b3.setRightUpCorner(new Point(107,157));
        b3.setRightDownleftUpCorners();

        b4.setLeftDownCorner(new Point(136,183));
        b4.setRightUpCorner(new Point(155,150));
        b4.setRightDownleftUpCorners();

        departments.add(b1);
        departments.add(b2);
        departments.add(b3);
        departments.add(b4);

        //GROCERY Department
        DepartmentBlock g1 = new DepartmentBlock();
        DepartmentBlock g2 = new DepartmentBlock();
        DepartmentBlock g3 = new DepartmentBlock();
        DepartmentBlock g4 = new DepartmentBlock();
        DepartmentBlock g5 = new DepartmentBlock();
        DepartmentBlock g6 = new DepartmentBlock();
        DepartmentBlock g7 = new DepartmentBlock();
        DepartmentBlock g8 = new DepartmentBlock();
        DepartmentBlock g9 = new DepartmentBlock();
        DepartmentBlock g10 = new DepartmentBlock();
        DepartmentBlock g11 = new DepartmentBlock();

        g1.setLeftDownCorner(new Point(180,510));
        g1.setRightUpCorner(new Point(200,350));
        g1.setRightDownleftUpCorners();

        g2.setLeftDownCorner(new Point(233,510));
        g2.setRightUpCorner(new Point(250,350));
        g2.setRightDownleftUpCorners();

        g3.setLeftDownCorner(new Point(280,510));
        g3.setRightUpCorner(new Point(300,350));
        g3.setRightDownleftUpCorners();

        g4.setLeftDownCorner(new Point(330,510));
        g4.setRightUpCorner(new Point(350,350));
        g4.setRightDownleftUpCorners();

        g5.setLeftDownCorner(new Point(180,300));
        g5.setRightUpCorner(new Point(200,140));
        g5.setRightDownleftUpCorners();

        g6.setLeftDownCorner(new Point(230,300));
        g6.setRightUpCorner(new Point(250,140));
        g6.setRightDownleftUpCorners();

        g7.setLeftDownCorner(new Point(280,300));
        g7.setRightUpCorner(new Point(300,140));
        g7.setRightDownleftUpCorners();

        g8.setLeftDownCorner(new Point(330,300));
        g8.setRightUpCorner(new Point(350,140));
        g8.setRightDownleftUpCorners();

        g9.setLeftDownCorner(new Point(380,300));
        g9.setRightUpCorner(new Point(400,140));
        g9.setRightDownleftUpCorners();

        g10.setLeftDownCorner(new Point(430,300));
        g10.setRightUpCorner(new Point(450,140));
        g10.setRightDownleftUpCorners();

        g11.setLeftDownCorner(new Point(480,300));
        g11.setRightUpCorner(new Point(500,140));
        g11.setRightDownleftUpCorners();

        departments.add(g1);
        departments.add(g2);
        departments.add(g3);
        departments.add(g4);
        departments.add(g5);
        departments.add(g6);
        departments.add(g7);
        departments.add(g8);
        departments.add(g9);
        departments.add(g10);
        departments.add(g11);

        //SEAFOOD Department
        DepartmentBlock s1 = new DepartmentBlock();

        s1.setLeftDownCorner(new Point(102,106));
        s1.setRightUpCorner(new Point(138,62));
        s1.setRightDownleftUpCorners();

        departments.add(s1);

        //MEAT Department
        DepartmentBlock m1 = new DepartmentBlock();
        DepartmentBlock m2 = new DepartmentBlock();
        DepartmentBlock m3 = new DepartmentBlock();
        DepartmentBlock m4 = new DepartmentBlock();

        m1.setLeftDownCorner(new Point(175,105));
        m1.setRightUpCorner(new Point(195,60));
        m1.setRightDownleftUpCorners();

        m2.setLeftDownCorner(new Point(225,105));
        m2.setRightUpCorner(new Point(245,60));
        m2.setRightDownleftUpCorners();

        m3.setLeftDownCorner(new Point(275,105));
        m3.setRightUpCorner(new Point(195,60));
        m3.setRightDownleftUpCorners();

        m4.setLeftDownCorner(new Point(325,105));
        m4.setRightUpCorner(new Point(345,60));
        m4.setRightDownleftUpCorners();

        departments.add(m1);
        departments.add(m2);
        departments.add(m3);
        departments.add(m4);


        //DAIRY EGGS & CHEASE Department
        DepartmentBlock d1 = new DepartmentBlock();
        DepartmentBlock d2 = new DepartmentBlock();


        d1.setLeftDownCorner(new Point(421,105));
        d1.setRightUpCorner(new Point(440,60));
        d1.setRightDownleftUpCorners();

        d2.setLeftDownCorner(new Point(470,105));
        d2.setRightUpCorner(new Point(490,60));
        d2.setRightDownleftUpCorners();

        departments.add(d1);
        departments.add(d2);

        //WINE & SPIRITS Department
        DepartmentBlock w1 = new DepartmentBlock();
        DepartmentBlock w2 = new DepartmentBlock();
        DepartmentBlock w3 = new DepartmentBlock();


        w1.setLeftDownCorner(new Point(550,80));
        w1.setRightUpCorner(new Point(615,60));
        w1.setRightDownleftUpCorners();

        w2.setLeftDownCorner(new Point(550,122));
        w2.setRightUpCorner(new Point(615,102));
        w2.setRightDownleftUpCorners();

        w3.setLeftDownCorner(new Point(550,165));
        w3.setRightUpCorner(new Point(615,145));
        w3.setRightDownleftUpCorners();

        departments.add(w1);
        departments.add(w2);
        departments.add(w3);

        //BAKERY Department
        DepartmentBlock bakery1 = new DepartmentBlock();
        DepartmentBlock bakery2 = new DepartmentBlock();
        DepartmentBlock bakery3 = new DepartmentBlock();
        DepartmentBlock bakery4 = new DepartmentBlock();
        DepartmentBlock bakery5 = new DepartmentBlock();
        DepartmentBlock bakery6 = new DepartmentBlock();


        bakery1.setLeftDownCorner(new Point(550,310));
        bakery1.setRightUpCorner(new Point(570,290));
        bakery1.setRightDownleftUpCorners();

        bakery2.setLeftDownCorner(new Point(595,310));
        bakery2.setRightUpCorner(new Point(615,290));
        bakery2.setRightDownleftUpCorners();

        bakery3.setLeftDownCorner(new Point(550,261));
        bakery3.setRightUpCorner(new Point(570,240));
        bakery3.setRightDownleftUpCorners();

        bakery4.setLeftDownCorner(new Point(595,260));
        bakery4.setRightUpCorner(new Point(615,242));
        bakery4.setRightDownleftUpCorners();

        bakery5.setLeftDownCorner(new Point(550,216));
        bakery5.setRightUpCorner(new Point(570,195));
        bakery5.setRightDownleftUpCorners();

        bakery6.setLeftDownCorner(new Point(595,214));
        bakery6.setRightUpCorner(new Point(615,195));
        bakery6.setRightDownleftUpCorners();

        departments.add(bakery1);
        departments.add(bakery2);
        departments.add(bakery3);
        departments.add(bakery4);
        departments.add(bakery5);
        departments.add(bakery6);


        //DELI & PREPARED FOOD Department
        DepartmentBlock deli1 = new DepartmentBlock();
        DepartmentBlock deli2 = new DepartmentBlock();
        DepartmentBlock deli3 = new DepartmentBlock();

        deli1.setLeftDownCorner(new Point(550,370));
        deli1.setRightUpCorner(new Point(612,350));
        deli1.setRightDownleftUpCorners();

        deli2.setLeftDownCorner(new Point(550,416));
        deli2.setRightUpCorner(new Point(612,400));
        deli2.setRightDownleftUpCorners();

        deli3.setLeftDownCorner(new Point(550,465));
        deli3.setRightUpCorner(new Point(612,450));
        deli3.setRightDownleftUpCorners();

        departments.add(deli1);
        departments.add(deli2);
        departments.add(deli3);

        //FROZEN Department
        DepartmentBlock f1 = new DepartmentBlock();
        DepartmentBlock f2 = new DepartmentBlock();
        DepartmentBlock f3 = new DepartmentBlock();

        f1.setLeftDownCorner(new Point(380 ,510));
        f1.setRightUpCorner(new Point(400,350));
        f1.setRightDownleftUpCorners();

        f2.setLeftDownCorner(new Point(430,510));
        f2.setRightUpCorner(new Point(450,350));
        f2.setRightDownleftUpCorners();

        f3.setLeftDownCorner(new Point(480,510));
        f3.setRightUpCorner(new Point(500,350));
        f3.setRightDownleftUpCorners();

        departments.add(f1);
        departments.add(f2);
        departments.add(f3);

        //CHAIRS Department
        DepartmentBlock cheir1 = new DepartmentBlock();
        DepartmentBlock cheir2 = new DepartmentBlock();
        DepartmentBlock cheir3 = new DepartmentBlock();
        DepartmentBlock cheir4 = new DepartmentBlock();
        DepartmentBlock cheir5 = new DepartmentBlock();

        cheir1.setLeftDownCorner(new Point(580,530));
        cheir1.setRightUpCorner(new Point(610,500));
        cheir1.setRightDownleftUpCorners();

        cheir2.setLeftDownCorner(new Point(570,570));
        cheir2.setRightUpCorner(new Point(600,545));
        cheir2.setRightDownleftUpCorners();

        cheir3.setLeftDownCorner(new Point(620,555));
        cheir3.setRightUpCorner(new Point(645,530));
        cheir3.setRightDownleftUpCorners();

        cheir4.setLeftDownCorner(new Point(607,597));
        cheir4.setRightUpCorner(new Point(633,570));
        cheir4.setRightDownleftUpCorners();

        cheir5.setLeftDownCorner(new Point(650,595));
        cheir5.setRightUpCorner(new Point(680,565));
        cheir5.setRightDownleftUpCorners();

        departments.add(cheir1);
        departments.add(cheir2);
        departments.add(cheir3);
        departments.add(cheir4);
        departments.add(cheir5);

        //CASHIER Department
        DepartmentBlock cashier1 = new DepartmentBlock();
        DepartmentBlock cashier2 = new DepartmentBlock();
        DepartmentBlock cashier3= new DepartmentBlock();
        DepartmentBlock cashier4 = new DepartmentBlock();
        DepartmentBlock cashier5 = new DepartmentBlock();
        DepartmentBlock cashier6 = new DepartmentBlock();
        DepartmentBlock cashier7 = new DepartmentBlock();

        cashier1.setLeftDownCorner(new Point(273,597));
        cashier1.setRightUpCorner(new Point(280,560));
        cashier1.setRightDownleftUpCorners();

        cashier2.setLeftDownCorner(new Point(323,597));
        cashier2.setRightUpCorner(new Point(317,560));
        cashier2.setRightDownleftUpCorners();

        cashier3.setLeftDownCorner(new Point(373,597));
        cashier3.setRightUpCorner(new Point(352,560));
        cashier3.setRightDownleftUpCorners();

        cashier4.setLeftDownCorner(new Point(423,597));
        cashier4.setRightUpCorner(new Point(388,560));
        cashier4.setRightDownleftUpCorners();

        cashier5.setLeftDownCorner(new Point(473,597));
        cashier5.setRightUpCorner(new Point(422,560));
        cashier5.setRightDownleftUpCorners();

        cashier6.setLeftDownCorner(new Point(449,597));
        cashier6.setRightUpCorner(new Point(458,560));
        cashier6.setRightDownleftUpCorners();

        cashier7.setLeftDownCorner(new Point(484,597));
        cashier7.setRightUpCorner(new Point(493,560));
        cashier7.setRightDownleftUpCorners();

        departments.add(cashier1);
        departments.add(cashier2);
        departments.add(cashier3);
        departments.add(cashier4);
        departments.add(cashier5);
        departments.add(cashier6);
        departments.add(cashier7);

    }

    public ArrayList<DepartmentBlock> getDepartments() {
        return departments;
    }

}
