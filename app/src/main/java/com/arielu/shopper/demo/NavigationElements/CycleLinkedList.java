package com.arielu.shopper.demo.NavigationElements;

import java.io.Serializable;

public class CycleLinkedList implements Serializable {// Double Cycle Linked List

    public PathNode head, tail;
    private int size;

    // Constructor,  constructs an empty list
    public CycleLinkedList(){
        head = tail = null;
        size = 0;
    }

    // Appends the specified element to the end of this list.
    public void add(Path path){
        PathNode prev= null,next = null;
        if (head == null)
        {
            head = new PathNode(path, prev, next);
            tail = head;
            head.next = head.prev = tail;

        }
        else
        {
            PathNode n = new PathNode(path, tail, head);
            tail.next = n;
            tail = n;
            head.prev = tail;
        }
        size++;

    }

    public PathNode getHead(){
        return head;
    }

    public PathNode getNext(PathNode n){
        return n.next;
    }

    public String toString(){
        String s = "[";
        if (head != null){
            s = s + head.toString() + ", ";
            for (PathNode n = head.next; n != head; n=n.next){
                s = s + n.toString() + ", ";
            }
            s = s.substring(0, s.length()-2);
        }
        return s+"]";
    }


    public String sourceDestToString() {
        String s = "[";
        if (head != null){
            s = s + head.getSource() + " -> " + head.getDestination() + ", ";
            for (PathNode n = head.next; n != head; n=n.next){
                int routeSize = n.getPath().getPoints().size();
                Point sourcePoint = n.getPath().getPoints().get(0);
                Point destPoint = n.getPath().getPoints().get(routeSize - 1);
                s = s + n.getSource() + " " + sourcePoint.toString() + " -> " + n.getDestination() + destPoint.toString() + ", ";
            }
            s = s.substring(0, s.length()-2);
        }
        return s+"]";
    }

}
