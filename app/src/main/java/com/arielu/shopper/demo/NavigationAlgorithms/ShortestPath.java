package com.arielu.shopper.demo.NavigationAlgorithms;


import com.arielu.shopper.demo.NavigationElements.PathTracker;
import com.arielu.shopper.demo.NavigationElements.PathNode;
import com.arielu.shopper.demo.NavigationElements.PathParser;
import com.arielu.shopper.demo.NavigationElements.Point;


import java.util.ArrayList;

public class ShortestPath
{
    public ShortestPath()
    {

    }

    //This function gets ArrayList of String and make it a Pathnodes ArrayList.
    private ArrayList<PathNode> stringPathsToPathNodes(ArrayList<String> stringPaths){
        ArrayList<PathNode> paths = new ArrayList<>();
        PathParser parser = new PathParser();
        for(String stringPath : stringPaths){
            PathNode path = parser.parse(stringPath);
            paths.add(path);
        }
        return paths;
    }


    //This function compare the client's current path to the DB saved path between each 2 nodes
    //and update the DB path if the client walked in a shorter path
    public ArrayList<PathNode> updateDbPaths(PathTracker clientPath  , ArrayList<PathNode> paths)
    {
        for (PathNode node : paths )
        {
            PathNode currentNode = clientPath.list.head ;
            while(currentNode.hasNext())
            {
                String cnSrc = currentNode.getSource() ;      //cnSrc = current node source
                String cnDst = currentNode.getDestination() ; //cnDst = current node destination
                String alnSrc = node.getSource() ;            //alnSrc = array list node's source
                String alnDst = node.getDestination() ;       //alnDst = array list node's sourdestinationce


                if(cnSrc.equals(alnSrc) && cnDst.equals(alnDst))
                {
                    int clientPathSize = currentNode.getPath().size() ;
                    int nodePathSize = node.getPath().size() ;
                    if(clientPathSize < nodePathSize)
                    {
                        node.setPath(currentNode.getPath());
                    }
                }
            }

        }
        return paths ;
    }

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

}
