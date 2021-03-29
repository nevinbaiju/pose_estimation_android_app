# Pose Estimation android app

## Introduction

This project is developed from the [ML Kit Quickstart app](https://github.com/googlesamples/mlkit.git). It uses the pose estimation module of the android app to predict and to compare to a predefined pose. We can add poses predefined and let the app compare it based on the angles at the joints. 

## Releases
The apk file can be found at the [Releases](https://github.com/nevinbaiju/pose_estimation_android_app/releases) page. 

## Feature List

Features that are included in this Quickstart app:
* [Pose Detection](https://developers.google.com/ml-kit/vision/pose-detection/android) - Detect the position of the human body in real time.

## Getting Started

* [Android](https://github.com/nevinbaiju/pose_estimation/tree/main/android) folder contains the app code
* [Code](https://github.com/nevinbaiju/pose_estimation/tree/main/code) folder contains the feature modules and test notebooks.

* Run the sample code on your Android device or emulator
* Try extending the code to add new features and functionality

### Live Camera scenario
It uses the camera preview as input and contains these API workflows: Object detection & tracking, Face Detection, Text Recognition, Barcode Scanning, and Image Labeling. There's also a settings page that allows you to configure several options:
* Camera
    * Preview Size - Specify the preview size of rear camera manually (Default size is chose appropriately based on screen size)
    * Enable live viewport - Prevent the live camera preview from being blocked by API rendering speed
* Pose Detection
    * Performance Mode -- Allows you to switch between "Fast" and "Accurate" operation mode
    * Show In-Frame Likelihood -- Displays InFrameLikelihood confidence within the app

## App Features
The additional functionalities added will be explained in detail here.
* Coordinate Smoothing
    * The output coordinates from the model are stored in an array of a specific size and the mean value is taken to lower the stuttering movements of the coordinates and produce a smoother movement. Things to note is, a minor lag is introduced to the output as the consequence of taking the mean value, but as long as the array size is small it's negligible.
* Pose Comparison
    * This module compares angles at the specific joints with the predefined angles and returns an error value based on how close the angles from the output as to the predefined ones. This enables us to see if the current output pose is similar to the one we defined. As of now, the poses are defined in an array inside the function, future implementations can improve this.
* Dynamic drwaing fucntion
    * Inorder to improve the user experience the drawing only starts after all the joints are on camera.
    * The output of the pose comparison is used to dynamically draw the line colors in such a way that, as the output coordinates are close to the predefined pose the lines get greener in color and when they are not the lines get red. This is to give visual feedback on the pose comparison. The dynamic coloring module only kicks in once a certain threshold has reached. the threshold is calculated based on the error values returned from the comparison module.
* Alignment Validation
    * The model have a tendency to wrongly classify the left and right shoulders when the person in the image is too close to the camera. This module checks with the pose that's defined and based on that, the wrong classified coordinates are dumped. 


## Support

* [Documentation](https://developers.google.com/ml-kit/guides)
* [API Reference](https://developers.google.com/ml-kit/reference/android)
* [Stack Overflow](https://stackoverflow.com/questions/tagged/google-mlkit)

## License

Copyright 2020 Google, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.