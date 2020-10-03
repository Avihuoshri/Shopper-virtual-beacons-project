package com.arielu.shopper.demo.Map;

import com.arielu.shopper.demo.NavigationElements.Point;
import com.arielu.shopper.demo.externalHardware.Beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeaconSetter {

    public ArrayList<Beacon> beacons;
    private Map<Triple,Point> beacon_entries;


    public void initBeacons()
    {
        beacons = new ArrayList<>();
        beacon_entries = new HashMap<>();
        initEntries();
    }

    /**
     * method can add beacons in a row starting from left to right
     * @param y marks the height (in pixels) of a beacon
     * @param quantity marks the amount of Beacons to add
     * @param pixelDiff marks how many pixels to shift to the right
     * @param locationOfReqBeacon is the location of the first
     */
    private void addMoreBeacon(int y, int quantity, int pixelDiff, Point locationOfReqBeacon)
    {
        //beacons.add(new Beacon(locationOfReqBeacon));
        Beacon b;
        int x = locationOfReqBeacon.getX();
        for (int i = 0; i < quantity; i++) {
            b = new Beacon(x,y);
            x+=pixelDiff;
            beacons.add(b);
        }
    }
    private void initEntries()
    {
        Triple[] triples = new Triple[18];
        triples[0] = new Triple(619,2,135);     // 1,2
        triples[1] = new Triple(545,10,55);     // 3-12
        triples[2] = new Triple(475,3,57);     // 13-15
        triples[3] = new Triple(475,2,85);     // 16,17
        triples[4] = new Triple(415,3,57);     // 18-20
        triples[5] = new Triple(415,2,85);     // 21,22
        triples[6] = new Triple(355,3,57);     // 23-25
        triples[7] = new Triple(355,2,85);     // 26,27
        triples[8] = new Triple(325,7,51);     // 29-35
        triples[9] = new Triple(325,2,85);     // 36,37
        triples[10] = new Triple(250,3,65);    // 38-40
        triples[11] = new Triple(280,3,65);    // 41-43
        triples[12] = new Triple(195,3,65);    // 44-46
        triples[13] = new Triple(215,3,57);    // 47-49
        triples[14] = new Triple(130,10,50);    // 51-60
        triples[15] = new Triple(50,3,50);    // 62-64
        triples[16] = new Triple(50,2,85);    // 65,66
        triples[17] = new Triple(90,3,53);    // 68-70

        Point b1_2 = new Point(213,611);
        Point b3_12 = new Point(50,545);
        Point b13_15 = new Point(45,475);
        Point b16_17 = new Point(545,475);
        Point b18_20 = new Point(45,415);
        Point b21_22 = new Point(545,415);
        Point b23_25 = new Point(45,355);
        Point b26_27 = new Point(545,355);
        Point b29_35 = new Point(160,325);
        Point b36_37 = new Point(545,325);
        Point b38_40 = new Point(45,250);
        Point b41_43 = new Point(545,280);
        Point b44_46 = new Point(45,195);
        Point b47_49 = new Point(545,215);
        Point b51_60 = new Point(165,130);
        Point b62_64 = new Point(215,50);
        Point b65_66 = new Point(460,50);
        Point b68_70 = new Point(545,90);
        Point[] points = new Point[]{b1_2,b3_12,b13_15,b16_17,b18_20,b21_22,b23_25,b26_27,b29_35,b36_37,b38_40,b41_43,b44_46,b47_49,b51_60,b62_64,
                                    b65_66,b68_70};
        for (int i = 0; i < points.length; i++) {
            beacon_entries.put(triples[i],points[i]);
        }
        for (Map.Entry<Triple,Point> entry : beacon_entries.entrySet())
        {
            addMoreBeacon(entry.getKey().y,entry.getKey().quantity,entry.getKey().pixelDiff,entry.getValue());
        }

        Point b28 = new Point(45,300);
        Point b50 = new Point(80,130);
        Point b61 = new Point(80,50);
        Point b67 = new Point(630,50);
        beacons.add(new Beacon(b28));
        beacons.add(new Beacon(b50));
        beacons.add(new Beacon(b61));
        beacons.add(new Beacon(b67));
    }

    class Triple
    {
        int y;
        int quantity;
        int pixelDiff;

        public Triple(int y, int quantity, int pixelDiff) {
            this.y = y;
            this.quantity = quantity;
            this.pixelDiff = pixelDiff;
        }
    }





}
