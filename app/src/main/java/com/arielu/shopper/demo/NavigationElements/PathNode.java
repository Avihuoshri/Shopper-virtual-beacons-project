package com.arielu.shopper.demo.NavigationElements;

import java.io.Serializable;

public class PathNode implements Serializable {
    public PathNode prev, next;
    private Path path;
    private String source, destination;

    public PathNode(){
        path = new Path();
    }

    public PathNode(Path path, PathNode prev, PathNode next){
        this.path = path;
        this.next = next;
        this.prev = prev;

        //this.source = prev.destination;

    }

    public void setSource(String source) { this.source = source; }

    public void setDestination(String destination) { this.destination = destination; }

    public String getSource() { return source; }

    public String getDestination() { return destination; }

    public void setPath(Path path){ this.path = path; }

    public Path getPath(){ return this.path; }

    public PathNode getNext(){ return this.next;}

    public PathNode getPrev(){ return this.prev; }

    public String toString(){ return " "+this.path.toString() + " "; }

    public boolean hasNext() {
        if(this.next == null)
            return false;
        return true;
    }
}
