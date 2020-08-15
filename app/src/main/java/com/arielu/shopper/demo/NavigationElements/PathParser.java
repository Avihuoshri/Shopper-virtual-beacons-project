package com.arielu.shopper.demo.NavigationElements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathParser {

    public PathNode parse(String DBPath) {

        PathNode path = new PathNode();

        //**********************************************************************************************************************
        String[] key1 = "2 , starting at : enter, going to : 1st:".split(":");
        String[] values1 = "[ (50,50),  (51,51),  (52,52),  (53,53),  (54,54),  (55,55),  (56,56)]".split(" ");
       //***********************************************************************************************************************
        String[] key = DBPath.split(":");
        String[] values = DBPath.split(" ");

        String from = key[1].split(",")[0];
        String to = key[2];

        path.setSource(from);
        path.setDestination(to);

        Pattern p = Pattern.compile("\\((.*?)\\)");
        System.out.println(" ###############  parse path ######");
        for (int i = 0; i < values.length; i++) {
            String s = values[i];
            Matcher m = p.matcher(s);
            while (m.find()) {
                Point point = new Point(m.group(1));
                path.getPath().add(point);
            }
        }
        return path;
    }

}


