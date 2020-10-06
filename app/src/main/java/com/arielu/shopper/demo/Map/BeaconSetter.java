package com.arielu.shopper.demo.Map;

import com.arielu.shopper.demo.NavigationElements.Point;
import com.arielu.shopper.demo.externalHardware.Beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeaconSetter {

    public ArrayList<Beacon> beacons;
    private Map<Triple,Point> beacon_entries;
    private float conversionRatio;

public void init()
{
    initBeacons() ;
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
        //pixelDiff = convertPixelsDiff(pixelDiff);
        for (int i = 0; i < quantity; i++) {
            b = new Beacon(x,y);
            beacons.add(b);
            x+=pixelDiff;
        }
    }

    private int convertPixelsDiff(int pixelDiff)
    {
        float temp = pixelDiff/conversionRatio;
        return (int)temp;
    }

    private void initBeacons() {
        beacons = new ArrayList<>();
//        Beacon b1 = new Beacon(290 , 1542);
        Beacon b2 = new Beacon(841 , 1542);
        Beacon b3 = new Beacon(70 , 1342);
//        Beacon b4 = new Beacon(146 , 1342);
//        Beacon b5 = new Beacon(240, 1342);
//        Beacon b6 = new Beacon(326 , 1342);
//        Beacon b7 = new Beacon(402 , 1342);
//        Beacon b8 = new Beacon(479 , 1342);
//        Beacon b9 = new Beacon(567 , 1342);
//        Beacon b10 = new Beacon(644 , 1342);
//        Beacon b11 = new Beacon(724 , 1342);
//        Beacon b12 = new Beacon(815 , 1342);
        Beacon b13 = new Beacon(70  , 1201);
        Beacon b14 = new Beacon(146 , 1201);
//        Beacon b15 = new Beacon(240 , 1201);
//        Beacon b16 = new Beacon(815 , 1224);
//        Beacon b17 = new Beacon(986 ,1224);
//        Beacon b18 = new Beacon(70 , 1042);
//        Beacon b19 = new Beacon(146 , 1042);
//        Beacon b20 = new Beacon(240 , 1042);
//        Beacon b21 = new Beacon(815 , 1089);
//        Beacon b22 = new Beacon(986 , 1089);
//        Beacon b23 = new Beacon(70  , 906);
//        Beacon b24 = new Beacon(146 , 906);
//        Beacon b25 = new Beacon(240 , 906);
//        Beacon b26 = new Beacon(815 , 974 );
//        Beacon b27 = new Beacon(986 , 974);
//        Beacon b28 = new Beacon(70  , 768);
//        Beacon b29 = new Beacon(240 , 824);
//        Beacon b30 = new Beacon(326 , 824);
//        Beacon b31 = new Beacon(402 , 824);
//        Beacon b32 = new Beacon(479 , 824);
//        Beacon b33 = new Beacon(567 , 824);
//        Beacon b34 = new Beacon(644 , 824);
//        Beacon b35 = new Beacon(724 , 824);
//        Beacon b36 = new Beacon(815 , 824);
//        Beacon b37 = new Beacon(986 , 824);
//        Beacon b38 = new Beacon(70 , 636);
//        Beacon b39 = new Beacon(170 , 636);
//        Beacon b40 = new Beacon(255, 636);
//        Beacon b41 = new Beacon(815 , 700);
//        Beacon b42 = new Beacon(897 , 700);
//        Beacon b43 = new Beacon(986 , 700);
//        Beacon b44 = new Beacon(70 , 491);
//        Beacon b45 = new Beacon(170 , 491);
//        Beacon b46 = new Beacon(255 , 491);
//        Beacon b47 = new Beacon(815 , 591);
//        Beacon b48 = new Beacon(897 , 591);
//        Beacon b49 = new Beacon(986 , 591);
//        Beacon b50 = new Beacon(70  , 353);
//        Beacon b51 = new Beacon(235 , 303);
//        Beacon b52 = new Beacon(326, 303);
//        Beacon b53 = new Beacon(402, 303);
//        Beacon b54 = new Beacon(479, 303);
//        Beacon b55 = new Beacon(567, 303);
//        Beacon b56 = new Beacon(644, 303);
//        Beacon b57 = new Beacon(724, 303);
//        Beacon b58 = new Beacon(815 , 303);
//        Beacon b59 = new Beacon(894 , 341);
//        Beacon b60 = new Beacon(986 , 341);
//        Beacon b61 = new Beacon(117 , 118);
//        Beacon b62 = new Beacon(229, 118);
//        Beacon b63 = new Beacon(326 , 118);
//        Beacon b64 = new Beacon(402 , 118);
//        Beacon b65 = new Beacon(479, 118);
//        Beacon b66 = new Beacon(606 , 118);
//        Beacon b67 = new Beacon(697 , 118);
//        Beacon b68 = new Beacon(797 , 118);
//        Beacon b69 = new Beacon(986 , 118);
//        Beacon b70 = new Beacon(986 , 232);
//        Beacon b71 = new Beacon(797 , 232 );
//        Beacon b72 = new Beacon(170 , 303 );

//        beacons.add(b1);
//        beacons.add(b2);
        beacons.add(b3);
//        beacons.add(b4);
//        beacons.add(b5);
//        beacons.add(b6);
//        beacons.add(b7);
//        beacons.add(b8);
//        beacons.add(b9);
//        beacons.add(b10);
//        beacons.add(b11);
//        beacons.add(b12);
        beacons.add(b13);
        beacons.add(b14);
//        beacons.add(b15);
//        beacons.add(b16);
//        beacons.add(b17);
//        beacons.add(b18);
//        beacons.add(b19);
//        beacons.add(b20);
//        beacons.add(b21);
//        beacons.add(b22);
//        beacons.add(b23);
//        beacons.add(b24);
//        beacons.add(b25);
//        beacons.add(b26);
//        beacons.add(b27);
//        beacons.add(b28);
//        beacons.add(b29);
//        beacons.add(b30);
//        beacons.add(b31);
//        beacons.add(b32);
//        beacons.add(b33);
//        beacons.add(b34);
//        beacons.add(b35);
//        beacons.add(b36);
//        beacons.add(b37);
//        beacons.add(b38);
//        beacons.add(b39);
//        beacons.add(b40);
//        beacons.add(b41);
//        beacons.add(b42);
//        beacons.add(b43);
//        beacons.add(b44);
//        beacons.add(b45);
//        beacons.add(b46);
//        beacons.add(b47);
//        beacons.add(b48);
//        beacons.add(b49);
//        beacons.add(b50);
//        beacons.add(b51);
//        beacons.add(b52);
//        beacons.add(b53);
//        beacons.add(b54);
//        beacons.add(b55);
//        beacons.add(b56);
//        beacons.add(b57);
//        beacons.add(b58);
//        beacons.add(b59);
//        beacons.add(b60);
//        beacons.add(b61);
//        beacons.add(b62);
//        beacons.add(b63);
//        beacons.add(b64);
//        beacons.add(b65);
//        beacons.add(b66);
//        beacons.add(b67);
//        beacons.add(b68);
//        beacons.add(b69);
//        beacons.add(b70);
//        beacons.add(b71);
//        beacons.add(b72);
    }


//    private void initEntries()
//    {
//        Triple[] triples = new Triple[18];
//        triples[0] = new Triple(609,2,75);     // 1,2
//        triples[1] = new Triple(545,10,38);     // 3-12
//        triples[2] = new Triple(475,3,34);     // 13-15
//        triples[3] = new Triple(475,2,59);     // 16,17
//        triples[4] = new Triple(415,3,33);     // 18-20
//        triples[5] = new Triple(415,2,59);     // 21,22
//        triples[6] = new Triple(355,3,33);     // 23-25
//        triples[7] = new Triple(355,2,59);     // 26,27
//        triples[8] = new Triple(325,7,36);     // 29-35
//        triples[9] = new Triple(325,2,59);     // 36,37
//        triples[10] = new Triple(250,3,33);    // 38-40
//        triples[11] = new Triple(280,3,31);    // 41-43
//        triples[12] = new Triple(195,3,33);    // 44-46
//        triples[13] = new Triple(215,3,31);    // 47-49
//        triples[14] = new Triple(130,10,33);    // 51-60
//        triples[15] = new Triple(50,3,33);    // 62-64
//        triples[16] = new Triple(50,2,31);    // 65,66
//        triples[17] = new Triple(90,3,34);    // 68-70
//
//        Point b1_2 = new Point(100,609);
//        Point b3_12 = new Point(35,545);
//        Point b13_15 = new Point(30,475);
//        Point b16_17 = new Point(340,475);
//        Point b18_20 = new Point(30,415);
//        Point b21_22 = new Point(340,415);
////        Point b23_25 = new Point(30,355);
////        Point b26_27 = new Point(340,355);
////        Point b29_35 = new Point(100,325);
////        Point b36_37 = new Point(340,325);
////        Point b38_40 = new Point(30,250);
////        Point b41_43 = new Point(340,280);
////        Point b44_46 = new Point(30,195);
////        Point b47_49 = new Point(340,215);
////        Point b51_60 = new Point(100,130);
////        Point b62_64 = new Point(133,50);
////        Point b65_66 = new Point(275,50);
////        Point b68_70 = new Point(340,90);
//        Point[] points = new Point[]{b1_2,b3_12};//,b13_15,b16_17,b18_20,b21_22
////                b23_25,b26_27,b29_35,b36_37,b38_40,b41_43,b44_46,b47_49,b51_60,b62_64,
////                                    b65_66,b68_70};
//        for (int i = 0; i < points.length; i++) {
//            beacon_entries.put(triples[i],points[i]);
//        }
//        for (Map.Entry<Triple,Point> entry : beacon_entries.entrySet())
//        {
//            addMoreBeacon(entry.getKey().y,entry.getKey().quantity,entry.getKey().pixelDiff,entry.getValue());
//        }
//
////        Point b28 = new Point(30,300);
////        Point b50 = new Point(45,136);
////        Point b61 = new Point(63,50);
////        Point b67 = new Point(400,50);
////        beacons.add(new Beacon(b28));
////        beacons.add(new Beacon(b50));
////        beacons.add(new Beacon(b61));
////        beacons.add(new Beacon(b67));
//    }

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
