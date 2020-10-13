package com.arielu.shopper.demo.Map;

import android.widget.Toast;

import com.arielu.shopper.demo.NavigationElements.Point;

public class SaleUpdates
{
    public String checkForUpdates(Point pixelPoint)
        {
            String saleUpdate = "" ;
            if(pixelPoint.getX() > 117 && pixelPoint.getX() < 255 && pixelPoint.getY() > 118 && pixelPoint.getY() < 303)
            {
                 saleUpdate = "3 Salmon in 30$" ;
            }
            if(pixelPoint.getX() > 604 && pixelPoint.getX() < 808 && pixelPoint.getY() > 113 && pixelPoint.getY() < 303)
            {
                 saleUpdate = "Buy 2 milk and get cheese for 1$" ;
            }

            return saleUpdate ;
        }
}
