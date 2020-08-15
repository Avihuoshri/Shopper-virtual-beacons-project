package com.arielu.shopper.demo.NavigationAlgorithms;

import com.arielu.shopper.demo.NavigationElements.Point;
import com.arielu.shopper.demo.NavigationElements.PathNode;

interface StoreLocationConvertor {

    public PathNode Point2PathNode(Point point);
    public Point PathNode2Point(PathNode node);
}
