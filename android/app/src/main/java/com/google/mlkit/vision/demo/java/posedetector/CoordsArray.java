package com.google.mlkit.vision.demo.java.posedetector;

import java.util.ArrayList;
import java.util.List;

public class CoordsArray {
    public static List<List<Float>> coordsArray = new ArrayList<List<Float>>();

    public static void push(List<Float> inArray){
        CoordsArray.coordsArray.add(inArray);
    }

    public static void pop() {
        CoordsArray.coordsArray.remove(0);
    }

    public static  List<Float> currentCoords = new ArrayList<Float>();
}
