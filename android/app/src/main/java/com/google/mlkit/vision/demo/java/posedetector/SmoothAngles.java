package com.google.mlkit.vision.demo.java.posedetector;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.ArrayList;
import java.util.List;

//Smoothing coords and calculating angles
public class SmoothAngles {

    private int windowSize;
    private String smoothingMethod;

    SmoothAngles(){
        this.windowSize = 4;
        this.smoothingMethod = "mean";
    }

    private List<Float> averageCoords(){
        // Please call this out if averaging could be done more efficiently :)
        // I'm a noob in Java.
        List<Float> currentCoords;
        List<Float> averagedCoords = new ArrayList<Float>();
        for (int k=0; k < CoordsArray.coordsArray.get(0).size(); k++){
            averagedCoords.add((float)0);
        }
        for (int i=0; i < CoordsArray.coordsArray.size(); i++){
            currentCoords = CoordsArray.coordsArray.get(i);
            for (int j=0; j < currentCoords.size(); j++){
                averagedCoords.set(j, averagedCoords.get(j) + currentCoords.get(j));
            }
        }
        for (int i=0; i < averagedCoords.size(); i++){
            averagedCoords.set(i, (averagedCoords.get(i)/CoordsArray.coordsArray.size()));
        }
        if (CoordsArray.coordsArray.size() == this.windowSize){
            CoordsArray.pop();
        }
        CoordsArray.push(averagedCoords);
        return averagedCoords;
    }

    public List<Float> smoothenCoords(List<Float> pose_coords){
        if (CoordsArray.coordsArray.size() == this.windowSize){
            CoordsArray.pop();
        }
        List<Float> smoothCoords = new ArrayList<Float>();
        CoordsArray.push(pose_coords);
        if (this.smoothingMethod == "mean"){
            smoothCoords = this.averageCoords();
        }

        return smoothCoords;
    }

    static float getAngle(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
        float result =
                (float) Math.toDegrees(
                        Math.atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                                lastPoint.getPosition().x - midPoint.getPosition().x)
                                - Math.atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                                firstPoint.getPosition().x - midPoint.getPosition().x));
        result = Math.abs(result); // Angle should never be negative
        if (result > 180) {
            result = (float) (360.0 - result); // Always get the acute representation of the angle
        }
        return result;
    }


}

