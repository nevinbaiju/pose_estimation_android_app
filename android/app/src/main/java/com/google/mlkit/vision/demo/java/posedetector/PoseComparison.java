package com.google.mlkit.vision.demo.java.posedetector;

import java.lang.Math;

public class PoseComparison {
    
    int numPoses;
    PoseComparison(){
        this.numPoses = 1;
    }

    /**
    * Function for comparing the pose.
    *
    * @param angleList Specify the angles of the various body parts in the
    *                   following order: left hand, right hand, left elbow, 
    *                   right elbow, left leg, right leg, left knee, right knee,
    *                   left ankle, right ankle. 
    * @return The errors between the given angles and the most similar angle of
    *         the defined poses.
    */
    public float[] comparePose(float angleList[]){
        float angleErrors[][], referenceAngles[][], 
               error,difference;
        int i, j, classifiedPoseIndex=0, totalError, minError=0;
        angleErrors = new float[this.numPoses][10];
        referenceAngles = getReferenceeAngles();
        for(i=0; i<referenceAngles.length; i++){
            totalError = 0;
            for(j=0; j<10; j++){
                difference = 1 - (Math.abs(angleList[j] - referenceAngles[i][j])/referenceAngles[i][j]);
                error = (float) (100/(1 +Math.pow(Math.E,((-Math.E)*difference+(Math.pow(Math.E,2))))));
                angleErrors[i][j] = error;
                totalError += error;
            }
            if((totalError < minError) | (minError == 0)){
                minError = totalError;
                classifiedPoseIndex = i;
            }
        }
        return angleErrors[classifiedPoseIndex];
    }
    /**
    * INCOMPLETE FUNCTION. Function for getting the angles of defined poses.
    */
    private float[][] getReferenceeAngles(){
        float referenceAngles[][] = {{90.0f, 90.0f, 175.0f, 175.0f, 108.0f,
                                        131.0f, 118.0f, 180.0f, 120.0f, 175.0f}};
        
        return referenceAngles;                                        
    }

}
