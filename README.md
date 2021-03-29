# Pose Estimation android app

## Introduction

This project is developed from the [ML Kit Quickstart app](https://github.com/googlesamples/mlkit.git). It uses the pose estimation module of the android app to predict and to compare to a predefined pose. We can add poses predefined and let the app compare it based on the angles at the joints. 

## Feature List

Features that are included in this Quickstart app:
* [Pose Detection](https://developers.google.com/ml-kit/vision/pose-detection/android) - Detect the position of the human body in real time.

## Getting Started

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