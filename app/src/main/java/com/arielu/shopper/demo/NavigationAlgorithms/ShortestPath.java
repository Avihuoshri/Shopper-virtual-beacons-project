package com.arielu.shopper.demo.NavigationAlgorithms;


import com.arielu.shopper.demo.NavigationElements.PathTracker;
import com.arielu.shopper.demo.NavigationElements.PathNode;
import com.arielu.shopper.demo.NavigationElements.PathParser;


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



}
