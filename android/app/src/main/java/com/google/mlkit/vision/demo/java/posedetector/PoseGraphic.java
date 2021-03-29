/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.vision.demo.java.posedetector;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import androidx.annotation.Nullable;

import com.google.mlkit.vision.demo.GraphicOverlay;
import com.google.mlkit.vision.demo.GraphicOverlay.Graphic;
import com.google.mlkit.vision.demo.java.Data;
import com.google.mlkit.vision.demo.java.Util;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.text.DecimalFormat;
import java.util.List;
import java.util.*;


/**
 * Draw the detected pose in preview.
 */
public class PoseGraphic extends Graphic {

    private static final float DOT_RADIUS = 15;
    private static final float IN_FRAME_LIKELIHOOD_TEXT_SIZE = 30.0f;
    private static float threshold = 0f;

    private final Pose pose;
    private boolean showInFrameLikelihood;
    private float dynamicPaintThreshold = 6.5f;
    private float angleError;
    private final Paint linePaint;
    private final Paint linePaintGreen;
    private final Paint dynamicLinePaint;
    private final Paint dotPaint;
    private final Paint textPaint;

    PoseGraphic(GraphicOverlay overlay, Pose pose, boolean showInFrameLikelihood) {
        super(overlay);


        this.pose = pose;
        this.showInFrameLikelihood = true;
        this.angleError = 0;

        dotPaint = new Paint();
        dotPaint.setColor(Color.WHITE);
        dotPaint.setTextSize(IN_FRAME_LIKELIHOOD_TEXT_SIZE);

        linePaint = new Paint();
        linePaint.setStrokeWidth(17);
        linePaint.setColor(Color.CYAN);

        linePaintGreen = new Paint();
        linePaintGreen.setStrokeWidth(17);
        linePaintGreen.setColor(Color.GREEN);

        dynamicLinePaint = new Paint();
        dynamicLinePaint.setStrokeWidth(17.5f);

        textPaint = new Paint();
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(50);

    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private int getRed(Float value){
        return (int)((1-value)*255);
//        return 0;
    }

    private int getGreen(Float value){
        return (int)((value)*255);
//        return 0;
    }

    @Override
    public void draw(Canvas canvas) {
        List<PoseLandmark> landmarks = pose.getAllPoseLandmarks();
        if (landmarks.isEmpty()) {
            return;
        }

        // Getting all the landmarks
        PoseLandmark nose = pose.getPoseLandmark(PoseLandmark.NOSE);
        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
        PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
        PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
        PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
        PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
        PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
        PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
        PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);
        PoseLandmark leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY);
        PoseLandmark rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY);
        PoseLandmark leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
        PoseLandmark rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);
        PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB);
        PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB);
        PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
        PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);
        PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX);
        PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX);


        // Frame Visibility Check
        showInFrameLikelihood = true;
        if(nose.getInFrameLikelihood()<threshold){
            showInFrameLikelihood = false;
        }
        if(leftShoulder.getInFrameLikelihood()<threshold){
            showInFrameLikelihood = false;
        }
        if(rightShoulder.getInFrameLikelihood()<threshold){
            showInFrameLikelihood = false;
        }
        if(leftElbow.getInFrameLikelihood()<threshold){
            showInFrameLikelihood = false;
        }
        if(rightElbow.getInFrameLikelihood()<threshold){
            showInFrameLikelihood = false;
        }
        if(leftWrist.getInFrameLikelihood()<threshold){
            showInFrameLikelihood = false;
        }
        if(rightWrist.getInFrameLikelihood()<threshold){
            showInFrameLikelihood = false;
        }
        if(leftHip.getInFrameLikelihood()<threshold){
            showInFrameLikelihood = false;
        }
        if(rightHip.getInFrameLikelihood()<threshold){
            showInFrameLikelihood = false;
        }
        if(leftKnee.getInFrameLikelihood()<threshold){
            showInFrameLikelihood = false;
        }
        if(rightKnee.getInFrameLikelihood()<threshold){
            showInFrameLikelihood = false;
        }
        if(leftAnkle.getInFrameLikelihood()<threshold){
            showInFrameLikelihood = false;
        }
        if(rightAnkle.getInFrameLikelihood()<threshold){
            showInFrameLikelihood = false;
        }

        if (showInFrameLikelihood==true){

            PoseAlignment poseAlignment = new PoseAlignment();
            Boolean shoulder_alignment = new Boolean(true);
            shoulder_alignment = poseAlignment.validatePoseAlignment(leftShoulder.getPosition().x,rightShoulder.getPosition().x,"shoulder");
            if (shoulder_alignment){
                //Angles
                SmoothAngles smoothAngles = new SmoothAngles();
                Float leftShoulder_angle = smoothAngles.getAngle(leftElbow, leftShoulder, leftHip);
                Float rightShoulder_angle = smoothAngles.getAngle(rightElbow, rightShoulder, rightHip);
                Float leftElbow_angle = smoothAngles.getAngle(leftWrist, leftElbow, leftShoulder);
                Float rightElbow_angle = smoothAngles.getAngle(rightWrist, rightElbow, rightShoulder);
                Float leftHip_angle = smoothAngles.getAngle(leftKnee, leftHip, leftShoulder);
                Float rightHip_angle = smoothAngles.getAngle(rightKnee, rightHip, rightShoulder);
                Float leftKnee_angle = smoothAngles.getAngle(leftAnkle, leftKnee, leftHip);
                Float rightKnee_angle = smoothAngles.getAngle(rightAnkle, rightKnee, rightHip);
                Float leftAnkle_angle = smoothAngles.getAngle(leftFootIndex, leftAnkle, leftKnee);
                Float rightAnkle_angle = smoothAngles.getAngle(rightFootIndex, rightAnkle, rightKnee);

                float[] angleArray = new float[10];
                angleArray[0] = leftShoulder_angle;
                angleArray[1] = rightShoulder_angle;
                angleArray[2] = leftElbow_angle;
                angleArray[3] = rightElbow_angle;
                angleArray[4] = leftHip_angle;
                angleArray[5] = rightHip_angle;
                angleArray[6] = leftKnee_angle;
                angleArray[7] = rightKnee_angle;
                angleArray[8] = leftAnkle_angle;
                angleArray[9] = rightAnkle_angle;

                float[] angleErrors = new float[10];
                PoseComparison poseCompare = new PoseComparison();
                angleErrors = poseCompare.comparePose(angleArray);


                //Creating array for smoothing
                List<Float> inputArray = new ArrayList<Float>();
                inputArray.add((float)leftShoulder.getPosition().x);
                inputArray.add((float)leftShoulder.getPosition().y);
                inputArray.add((float)rightShoulder.getPosition().x);
                inputArray.add((float)rightShoulder.getPosition().y);
                inputArray.add((float)leftElbow.getPosition().x);
                inputArray.add((float)leftElbow.getPosition().y);
                inputArray.add((float)rightElbow.getPosition().x);
                inputArray.add((float)rightElbow.getPosition().y);
                inputArray.add((float)leftWrist.getPosition().x);
                inputArray.add((float)leftWrist.getPosition().y);
                inputArray.add((float)rightWrist.getPosition().x);
                inputArray.add((float)rightWrist.getPosition().y);
                inputArray.add((float)leftHip.getPosition().x);
                inputArray.add((float)leftHip.getPosition().y);
                inputArray.add((float)rightHip.getPosition().x);
                inputArray.add((float)rightHip.getPosition().y);
                inputArray.add((float)leftKnee.getPosition().x);
                inputArray.add((float)leftKnee.getPosition().y);
                inputArray.add((float)rightKnee.getPosition().x);
                inputArray.add((float)rightKnee.getPosition().y);
                inputArray.add((float)leftAnkle.getPosition().x);
                inputArray.add((float)leftAnkle.getPosition().y);
                inputArray.add((float)rightAnkle.getPosition().x);
                inputArray.add((float)rightAnkle.getPosition().y);
                inputArray.add((float)leftPinky.getPosition().x);
                inputArray.add((float)leftPinky.getPosition().y);
                inputArray.add((float)rightPinky.getPosition().x);
                inputArray.add((float)rightPinky.getPosition().y);
                inputArray.add((float)leftIndex.getPosition().x);
                inputArray.add((float)leftIndex.getPosition().y);
                inputArray.add((float)rightIndex.getPosition().x);
                inputArray.add((float)rightIndex.getPosition().y);
                inputArray.add((float)leftThumb.getPosition().x);
                inputArray.add((float)leftThumb.getPosition().y);
                inputArray.add((float)rightThumb.getPosition().x);
                inputArray.add((float)rightThumb.getPosition().y);
                inputArray.add((float)leftHeel.getPosition().x);
                inputArray.add((float)leftHeel.getPosition().y);
                inputArray.add((float)rightHeel.getPosition().x);
                inputArray.add((float)rightHeel.getPosition().y);
                inputArray.add((float)leftFootIndex.getPosition().x);
                inputArray.add((float)leftFootIndex.getPosition().y);
                inputArray.add((float)rightFootIndex.getPosition().x);
                inputArray.add((float)rightFootIndex.getPosition().y);
                inputArray.add((float)nose.getPosition().x);
                inputArray.add((float)nose.getPosition().y);

                //Calling the smoothing function
                CoordsArray.currentCoords = smoothAngles.smoothenCoords(inputArray);

                PointF new_leftShoulder = new PointF(CoordsArray.currentCoords.get(0), CoordsArray.currentCoords.get(1));
                PointF new_rightShoulder = new PointF(CoordsArray.currentCoords.get(2), CoordsArray.currentCoords.get(3));
                PointF new_shoulderMid = new PointF((CoordsArray.currentCoords.get(0)+CoordsArray.currentCoords.get(2))/2,(CoordsArray.currentCoords.get(1)+CoordsArray.currentCoords.get(3))/2);
                PointF new_leftElbow = new PointF(CoordsArray.currentCoords.get(4), CoordsArray.currentCoords.get(5));
                PointF new_rightElbow = new PointF(CoordsArray.currentCoords.get(6), CoordsArray.currentCoords.get(7));
                PointF new_leftWrist = new PointF(CoordsArray.currentCoords.get(8), CoordsArray.currentCoords.get(9));
                PointF new_rightWrist = new PointF(CoordsArray.currentCoords.get(10), CoordsArray.currentCoords.get(11));
                PointF new_leftHip = new PointF(CoordsArray.currentCoords.get(12), CoordsArray.currentCoords.get(13));
                PointF new_rightHip = new PointF(CoordsArray.currentCoords.get(14), CoordsArray.currentCoords.get(15));
                PointF new_leftKnee = new PointF(CoordsArray.currentCoords.get(16), CoordsArray.currentCoords.get(17));
                PointF new_rightKnee = new PointF(CoordsArray.currentCoords.get(18), CoordsArray.currentCoords.get(19));
                PointF new_leftAnkle = new PointF(CoordsArray.currentCoords.get(20), CoordsArray.currentCoords.get(21));
                PointF new_rightAnkle = new PointF(CoordsArray.currentCoords.get(22), CoordsArray.currentCoords.get(23));
                PointF new_leftPinky = new PointF(CoordsArray.currentCoords.get(24), CoordsArray.currentCoords.get(25));
                PointF new_rightPinky = new PointF(CoordsArray.currentCoords.get(26), CoordsArray.currentCoords.get(27));
                PointF new_leftIndex = new PointF(CoordsArray.currentCoords.get(28), CoordsArray.currentCoords.get(29));
                PointF new_rightIndex = new PointF(CoordsArray.currentCoords.get(30), CoordsArray.currentCoords.get(31));
                PointF new_leftThumb = new PointF(CoordsArray.currentCoords.get(32), CoordsArray.currentCoords.get(33));
                PointF new_rightThumb = new PointF(CoordsArray.currentCoords.get(34), CoordsArray.currentCoords.get(35));
                PointF new_leftHeel = new PointF(CoordsArray.currentCoords.get(36), CoordsArray.currentCoords.get(37));
                PointF new_rightHeel = new PointF(CoordsArray.currentCoords.get(38), CoordsArray.currentCoords.get(39));
                PointF new_leftFootIndex = new PointF(CoordsArray.currentCoords.get(40), CoordsArray.currentCoords.get(41));
                PointF new_rightFootIndex = new PointF(CoordsArray.currentCoords.get(42), CoordsArray.currentCoords.get(43));
                PointF new_nose = new PointF(CoordsArray.currentCoords.get(44), CoordsArray.currentCoords.get(45));


                //Displaying angles
                drawAngle(canvas, leftShoulder_angle, new_leftShoulder, textPaint);
                drawAngle(canvas, rightShoulder_angle, new_rightShoulder, textPaint);
                drawAngle(canvas, leftElbow_angle, new_leftElbow, textPaint);
                drawAngle(canvas, rightElbow_angle, new_rightElbow, textPaint);
                drawAngle(canvas, leftHip_angle, new_leftHip, textPaint);
                drawAngle(canvas, rightHip_angle, new_rightHip, textPaint);
                drawAngle(canvas, leftKnee_angle, new_leftKnee, textPaint);
                drawAngle(canvas, rightKnee_angle, new_rightKnee, textPaint);
                drawAngle(canvas, leftAnkle_angle, new_leftAnkle, textPaint);
                drawAngle(canvas, rightAnkle_angle, new_rightAnkle, textPaint);

                //Drawing Face points
                drawPoint(canvas, new_nose, dotPaint);

                //Drawing joint points
                drawPoint(canvas, new_leftShoulder, dotPaint);
                drawPoint(canvas, new_rightShoulder, dotPaint);
                drawPoint(canvas, new_leftElbow, dotPaint);
                drawPoint(canvas, new_rightElbow, dotPaint);
                drawPoint(canvas, new_leftWrist, dotPaint);
                drawPoint(canvas, new_rightWrist, dotPaint);
                drawPoint(canvas, new_leftThumb, dotPaint);
                drawPoint(canvas, new_leftPinky, dotPaint);
                drawPoint(canvas, new_leftIndex, dotPaint);
                drawPoint(canvas, new_rightThumb, dotPaint);
                drawPoint(canvas, new_rightPinky, dotPaint);
                drawPoint(canvas, new_rightIndex, dotPaint);
                drawPoint(canvas, new_leftHip, dotPaint);
                drawPoint(canvas, new_rightHip, dotPaint);
                drawPoint(canvas, new_leftKnee, dotPaint);
                drawPoint(canvas, new_rightKnee, dotPaint);
                drawPoint(canvas, new_leftAnkle, dotPaint);
                drawPoint(canvas, new_rightAnkle, dotPaint);
                drawPoint(canvas, new_leftHeel, dotPaint);
                drawPoint(canvas, new_rightHeel, dotPaint);
                drawPoint(canvas, new_rightFootIndex, dotPaint);
                drawPoint(canvas, new_leftFootIndex, dotPaint);


                //Drawing lines
                for (int i=0;i<10;i++){
                    angleError += angleErrors[i];
                }
                if (angleError > dynamicPaintThreshold) {
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[0]),getGreen(angleErrors[0]),0));
                    drawLine(canvas, new_leftShoulder, new_leftElbow, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[1]),getGreen(angleErrors[1]),0));
                    drawLine(canvas, new_rightShoulder, new_rightElbow, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[2]),getGreen(angleErrors[2]),0));
                    drawLine(canvas, new_leftElbow, new_leftWrist, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[3]),getGreen(angleErrors[3]),0));
                    drawLine(canvas, new_rightElbow, new_rightWrist, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[4]),getGreen(angleErrors[4]),0));
                    drawLine(canvas, new_leftHip, new_leftKnee, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[5]),getGreen(angleErrors[5]),0));
                    drawLine(canvas, new_rightHip, new_rightKnee, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[6]),getGreen(angleErrors[6]),0));
                    drawLine(canvas, new_leftKnee, new_leftAnkle, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[7]),getGreen(angleErrors[7]),0));
                    drawLine(canvas, new_rightKnee, new_rightAnkle, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[8]),getGreen(angleErrors[8]),0));
                    drawLine(canvas, new_leftAnkle, new_leftFootIndex, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[9]),getGreen(angleErrors[9]),0));
                    drawLine(canvas, new_rightAnkle, new_rightFootIndex, dynamicLinePaint);

                    drawLine(canvas, new_leftShoulder, new_rightShoulder, linePaintGreen);
                    drawLine(canvas, new_leftHip, new_rightHip, linePaintGreen);
                    drawLine(canvas, new_shoulderMid,new_nose,linePaintGreen);
                    //Left body
                    drawLine(canvas, new_leftShoulder, new_leftHip, linePaintGreen);
                    drawLine(canvas, new_leftWrist, new_leftThumb, linePaintGreen);
                    drawLine(canvas, new_leftWrist, new_leftPinky, linePaintGreen);
                    drawLine(canvas, new_leftWrist, new_leftIndex, linePaintGreen);
                    drawLine(canvas, new_leftAnkle, new_leftHeel, linePaintGreen);
                    drawLine(canvas, new_leftHeel, new_leftFootIndex, linePaintGreen);

                    //Right body
                    drawLine(canvas, new_rightShoulder, new_rightHip, linePaintGreen);
                    drawLine(canvas, new_rightWrist, new_rightThumb, linePaintGreen);
                    drawLine(canvas, new_rightWrist, new_rightPinky, linePaintGreen);
                    drawLine(canvas, new_rightWrist, new_rightIndex, linePaintGreen);
                    drawLine(canvas, new_rightAnkle, new_rightHeel, linePaintGreen);
                    drawLine(canvas, new_rightHeel, new_rightFootIndex, linePaintGreen);

                }
                else{
                    drawLine(canvas, new_leftShoulder, new_leftElbow, linePaint);
                    drawLine(canvas, new_rightShoulder, new_rightElbow, linePaint);
                    drawLine(canvas, new_leftElbow, new_leftWrist, linePaint);
                    drawLine(canvas, new_rightElbow, new_rightWrist, linePaint);
                    drawLine(canvas, new_leftHip, new_leftKnee, linePaint);
                    drawLine(canvas, new_rightHip, new_rightKnee, linePaint);
                    drawLine(canvas, new_leftKnee, new_leftAnkle, linePaint);
                    drawLine(canvas, new_rightKnee, new_rightAnkle, linePaint);
                    drawLine(canvas, new_leftAnkle, new_leftFootIndex, linePaint);
                    drawLine(canvas, new_rightAnkle, new_rightFootIndex, linePaint);

                    drawLine(canvas, new_leftShoulder, new_rightShoulder, linePaint);
                    drawLine(canvas, new_leftHip, new_rightHip, linePaint);
                    drawLine(canvas, new_shoulderMid,new_nose,linePaint);
                    //Left body
                    drawLine(canvas, new_leftShoulder, new_leftHip, linePaint);
                    drawLine(canvas, new_leftWrist, new_leftThumb, linePaint);
                    drawLine(canvas, new_leftWrist, new_leftPinky, linePaint);
                    drawLine(canvas, new_leftWrist, new_leftIndex, linePaint);
                    drawLine(canvas, new_leftAnkle, new_leftHeel, linePaint);
                    drawLine(canvas, new_leftHeel, new_leftFootIndex, linePaint);

                    //Right body
                    drawLine(canvas, new_rightShoulder, new_rightHip, linePaint);
                    drawLine(canvas, new_rightWrist, new_rightThumb, linePaint);
                    drawLine(canvas, new_rightWrist, new_rightPinky, linePaint);
                    drawLine(canvas, new_rightWrist, new_rightIndex, linePaint);
                    drawLine(canvas, new_rightAnkle, new_rightHeel, linePaint);
                    drawLine(canvas, new_rightHeel, new_rightFootIndex, linePaint);

                }
            }
            else {
                SmoothAngles smoothAngles = new SmoothAngles();
                Float leftShoulder_angle = smoothAngles.getAngle(leftElbow, leftShoulder, leftHip);
                Float rightShoulder_angle = smoothAngles.getAngle(rightElbow, rightShoulder, rightHip);
                Float leftElbow_angle = smoothAngles.getAngle(leftWrist, leftElbow, leftShoulder);
                Float rightElbow_angle = smoothAngles.getAngle(rightWrist, rightElbow, rightShoulder);
                Float leftHip_angle = smoothAngles.getAngle(leftKnee, leftHip, leftShoulder);
                Float rightHip_angle = smoothAngles.getAngle(rightKnee, rightHip, rightShoulder);
                Float leftKnee_angle = smoothAngles.getAngle(leftAnkle, leftKnee, leftHip);
                Float rightKnee_angle = smoothAngles.getAngle(rightAnkle, rightKnee, rightHip);
                Float leftAnkle_angle = smoothAngles.getAngle(leftFootIndex, leftAnkle, leftKnee);
                Float rightAnkle_angle = smoothAngles.getAngle(rightFootIndex, rightAnkle, rightKnee);

                float[] angleArray = new float[10];
                angleArray[0] = leftShoulder_angle;
                angleArray[1] = rightShoulder_angle;
                angleArray[2] = leftElbow_angle;
                angleArray[3] = rightElbow_angle;
                angleArray[4] = leftHip_angle;
                angleArray[5] = rightHip_angle;
                angleArray[6] = leftKnee_angle;
                angleArray[7] = rightKnee_angle;
                angleArray[8] = leftAnkle_angle;
                angleArray[9] = rightAnkle_angle;

                float[] angleErrors = new float[10];
                PoseComparison poseCompare = new PoseComparison();
                angleErrors = poseCompare.comparePose(angleArray);

                PointF new_leftShoulder = new PointF(CoordsArray.currentCoords.get(0), CoordsArray.currentCoords.get(1));
                PointF new_rightShoulder = new PointF(CoordsArray.currentCoords.get(2), CoordsArray.currentCoords.get(3));
                PointF new_shoulderMid = new PointF((CoordsArray.currentCoords.get(0) + CoordsArray.currentCoords.get(2)) / 2, (CoordsArray.currentCoords.get(1) + CoordsArray.currentCoords.get(3)) / 2);
                PointF new_leftElbow = new PointF(CoordsArray.currentCoords.get(4), CoordsArray.currentCoords.get(5));
                PointF new_rightElbow = new PointF(CoordsArray.currentCoords.get(6), CoordsArray.currentCoords.get(7));
                PointF new_leftWrist = new PointF(CoordsArray.currentCoords.get(8), CoordsArray.currentCoords.get(9));
                PointF new_rightWrist = new PointF(CoordsArray.currentCoords.get(10), CoordsArray.currentCoords.get(11));
                PointF new_leftHip = new PointF(CoordsArray.currentCoords.get(12), CoordsArray.currentCoords.get(13));
                PointF new_rightHip = new PointF(CoordsArray.currentCoords.get(14), CoordsArray.currentCoords.get(15));
                PointF new_leftKnee = new PointF(CoordsArray.currentCoords.get(16), CoordsArray.currentCoords.get(17));
                PointF new_rightKnee = new PointF(CoordsArray.currentCoords.get(18), CoordsArray.currentCoords.get(19));
                PointF new_leftAnkle = new PointF(CoordsArray.currentCoords.get(20), CoordsArray.currentCoords.get(21));
                PointF new_rightAnkle = new PointF(CoordsArray.currentCoords.get(22), CoordsArray.currentCoords.get(23));
                PointF new_leftPinky = new PointF(CoordsArray.currentCoords.get(24), CoordsArray.currentCoords.get(25));
                PointF new_rightPinky = new PointF(CoordsArray.currentCoords.get(26), CoordsArray.currentCoords.get(27));
                PointF new_leftIndex = new PointF(CoordsArray.currentCoords.get(28), CoordsArray.currentCoords.get(29));
                PointF new_rightIndex = new PointF(CoordsArray.currentCoords.get(30), CoordsArray.currentCoords.get(31));
                PointF new_leftThumb = new PointF(CoordsArray.currentCoords.get(32), CoordsArray.currentCoords.get(33));
                PointF new_rightThumb = new PointF(CoordsArray.currentCoords.get(34), CoordsArray.currentCoords.get(35));
                PointF new_leftHeel = new PointF(CoordsArray.currentCoords.get(36), CoordsArray.currentCoords.get(37));
                PointF new_rightHeel = new PointF(CoordsArray.currentCoords.get(38), CoordsArray.currentCoords.get(39));
                PointF new_leftFootIndex = new PointF(CoordsArray.currentCoords.get(40), CoordsArray.currentCoords.get(41));
                PointF new_rightFootIndex = new PointF(CoordsArray.currentCoords.get(42), CoordsArray.currentCoords.get(43));
                PointF new_nose = new PointF(CoordsArray.currentCoords.get(44), CoordsArray.currentCoords.get(45));

                //Displaying angles
                drawAngle(canvas, leftShoulder_angle, new_leftShoulder, textPaint);
                drawAngle(canvas, rightShoulder_angle, new_rightShoulder, textPaint);
                drawAngle(canvas, leftElbow_angle, new_leftElbow, textPaint);
                drawAngle(canvas, rightElbow_angle, new_rightElbow, textPaint);
                drawAngle(canvas, leftHip_angle, new_leftHip, textPaint);
                drawAngle(canvas, rightHip_angle, new_rightHip, textPaint);
                drawAngle(canvas, leftKnee_angle, new_leftKnee, textPaint);
                drawAngle(canvas, rightKnee_angle, new_rightKnee, textPaint);
                drawAngle(canvas, leftAnkle_angle, new_leftAnkle, textPaint);
                drawAngle(canvas, rightAnkle_angle, new_rightAnkle, textPaint);

                //Drawing Face points
                drawPoint(canvas, new_nose, dotPaint);

                //Drawing joint points
                drawPoint(canvas, new_leftShoulder, dotPaint);
                drawPoint(canvas, new_rightShoulder, dotPaint);
                drawPoint(canvas, new_leftElbow, dotPaint);
                drawPoint(canvas, new_rightElbow, dotPaint);
                drawPoint(canvas, new_leftWrist, dotPaint);
                drawPoint(canvas, new_rightWrist, dotPaint);
                drawPoint(canvas, new_leftThumb, dotPaint);
                drawPoint(canvas, new_leftPinky, dotPaint);
                drawPoint(canvas, new_leftIndex, dotPaint);
                drawPoint(canvas, new_rightThumb, dotPaint);
                drawPoint(canvas, new_rightPinky, dotPaint);
                drawPoint(canvas, new_rightIndex, dotPaint);
                drawPoint(canvas, new_leftHip, dotPaint);
                drawPoint(canvas, new_rightHip, dotPaint);
                drawPoint(canvas, new_leftKnee, dotPaint);
                drawPoint(canvas, new_rightKnee, dotPaint);
                drawPoint(canvas, new_leftAnkle, dotPaint);
                drawPoint(canvas, new_rightAnkle, dotPaint);
                drawPoint(canvas, new_leftHeel, dotPaint);
                drawPoint(canvas, new_rightHeel, dotPaint);
                drawPoint(canvas, new_rightFootIndex, dotPaint);
                drawPoint(canvas, new_leftFootIndex, dotPaint);


                //Drawing lines
                for (int i = 0; i < 10; i++) {
                    angleError += angleErrors[i];
                }
                if (angleError > dynamicPaintThreshold) {
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[0]), getGreen(angleErrors[0]), 0));
                    drawLine(canvas, new_leftShoulder, new_leftElbow, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[1]), getGreen(angleErrors[1]), 0));
                    drawLine(canvas, new_rightShoulder, new_rightElbow, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[2]), getGreen(angleErrors[2]), 0));
                    drawLine(canvas, new_leftElbow, new_leftWrist, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[3]), getGreen(angleErrors[3]), 0));
                    drawLine(canvas, new_rightElbow, new_rightWrist, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[4]), getGreen(angleErrors[4]), 0));
                    drawLine(canvas, new_leftHip, new_leftKnee, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[5]), getGreen(angleErrors[5]), 0));
                    drawLine(canvas, new_rightHip, new_rightKnee, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[6]), getGreen(angleErrors[6]), 0));
                    drawLine(canvas, new_leftKnee, new_leftAnkle, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[7]), getGreen(angleErrors[7]), 0));
                    drawLine(canvas, new_rightKnee, new_rightAnkle, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[8]), getGreen(angleErrors[8]), 0));
                    drawLine(canvas, new_leftAnkle, new_leftFootIndex, dynamicLinePaint);
                    dynamicLinePaint.setColor(Color.rgb(getRed(angleErrors[9]), getGreen(angleErrors[9]), 0));
                    drawLine(canvas, new_rightAnkle, new_rightFootIndex, dynamicLinePaint);

                    drawLine(canvas, new_leftShoulder, new_rightShoulder, linePaintGreen);
                    drawLine(canvas, new_leftHip, new_rightHip, linePaintGreen);
                    drawLine(canvas, new_shoulderMid, new_nose, linePaintGreen);
                    //Left body
                    drawLine(canvas, new_leftShoulder, new_leftHip, linePaintGreen);
                    drawLine(canvas, new_leftWrist, new_leftThumb, linePaintGreen);
                    drawLine(canvas, new_leftWrist, new_leftPinky, linePaintGreen);
                    drawLine(canvas, new_leftWrist, new_leftIndex, linePaintGreen);
                    drawLine(canvas, new_leftAnkle, new_leftHeel, linePaintGreen);
                    drawLine(canvas, new_leftHeel, new_leftFootIndex, linePaintGreen);

                    //Right body
                    drawLine(canvas, new_rightShoulder, new_rightHip, linePaintGreen);
                    drawLine(canvas, new_rightWrist, new_rightThumb, linePaintGreen);
                    drawLine(canvas, new_rightWrist, new_rightPinky, linePaintGreen);
                    drawLine(canvas, new_rightWrist, new_rightIndex, linePaintGreen);
                    drawLine(canvas, new_rightAnkle, new_rightHeel, linePaintGreen);
                    drawLine(canvas, new_rightHeel, new_rightFootIndex, linePaintGreen);

                } else {
                    drawLine(canvas, new_leftShoulder, new_leftElbow, linePaint);
                    drawLine(canvas, new_rightShoulder, new_rightElbow, linePaint);
                    drawLine(canvas, new_leftElbow, new_leftWrist, linePaint);
                    drawLine(canvas, new_rightElbow, new_rightWrist, linePaint);
                    drawLine(canvas, new_leftHip, new_leftKnee, linePaint);
                    drawLine(canvas, new_rightHip, new_rightKnee, linePaint);
                    drawLine(canvas, new_leftKnee, new_leftAnkle, linePaint);
                    drawLine(canvas, new_rightKnee, new_rightAnkle, linePaint);
                    drawLine(canvas, new_leftAnkle, new_leftFootIndex, linePaint);
                    drawLine(canvas, new_rightAnkle, new_rightFootIndex, linePaint);

                    drawLine(canvas, new_leftShoulder, new_rightShoulder, linePaint);
                    drawLine(canvas, new_leftHip, new_rightHip, linePaint);
                    drawLine(canvas, new_shoulderMid, new_nose, linePaint);
                    //Left body
                    drawLine(canvas, new_leftShoulder, new_leftHip, linePaint);
                    drawLine(canvas, new_leftWrist, new_leftThumb, linePaint);
                    drawLine(canvas, new_leftWrist, new_leftPinky, linePaint);
                    drawLine(canvas, new_leftWrist, new_leftIndex, linePaint);
                    drawLine(canvas, new_leftAnkle, new_leftHeel, linePaint);
                    drawLine(canvas, new_leftHeel, new_leftFootIndex, linePaint);

                    //Right body
                    drawLine(canvas, new_rightShoulder, new_rightHip, linePaint);
                    drawLine(canvas, new_rightWrist, new_rightThumb, linePaint);
                    drawLine(canvas, new_rightWrist, new_rightPinky, linePaint);
                    drawLine(canvas, new_rightWrist, new_rightIndex, linePaint);
                    drawLine(canvas, new_rightAnkle, new_rightHeel, linePaint);
                    drawLine(canvas, new_rightHeel, new_rightFootIndex, linePaint);

                }
            }
        }
    }

    void drawAngle(Canvas canvas, float angle, @Nullable PointF point, Paint paint){
        if(point == null) {
            return;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        angle = Float.valueOf(df.format(angle));
        canvas.drawText(String.valueOf(angle),translateX(point.x), translateY(point.y), paint);
    }

    void drawPoint(Canvas canvas, @Nullable PointF point, Paint paint) {
        if (point == null) {
            return;
        }
        canvas.drawCircle(translateX(point.x), translateY(point.y), DOT_RADIUS, paint);
    }

    void drawLine(Canvas canvas, @Nullable PointF start, @Nullable PointF end, Paint paint) {
        if (start == null || end == null) {
            return;
        }
        canvas.drawLine(
                translateX(start.x), translateY(start.y), translateX(end.x), translateY(end.y), paint);
    }
}
