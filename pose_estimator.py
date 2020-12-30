import cv2
import mediapipe as mp
import math
from imutils.video import VideoStream
from imutils.video import FileVideoStream
import numpy as np
import matplotlib.pyplot as plt
from scipy.signal import savgol_filter
import collections

class PoseEstimator:
    
    def __init__(self, window_size=8):
        """
        Window Size to specify how much frames to be considered for smoothing
        """
        self.window_size = window_size
        self.mp_drawing = mp.solutions.drawing_utils
        self.mp_pose = mp.solutions.pose
        self.pose = self.mp_pose.Pose(static_image_mode=False, min_detection_confidence=0.1)
        self.writer = None
        self.coords_array = []
        
    def get_pose_coords(self, image):
        """
        Function returns the coordinates of wrist, elbow and shoulder if given an image.
        """
        try:
            image_height, image_width, _ = image.shape
            results = self.pose.process(cv2.cvtColor(image, cv2.COLOR_BGR2RGB))
            if not results.pose_landmarks:
                raise ValueError('No poses detected')
            get_pose = results.pose_landmarks.landmark
            lm = self.mp_pose.PoseLandmark
            wrist_x = get_pose[lm.LEFT_WRIST].x*image_width
            wrist_y = get_pose[lm.LEFT_WRIST].y*image_height
            elbow_x = get_pose[lm.LEFT_ELBOW].x*image_width
            elbow_y = get_pose[lm.LEFT_ELBOW].y*image_height
            shoulder_x = get_pose[lm.LEFT_SHOULDER].x*image_width
            shoulder_y = get_pose[lm.LEFT_SHOULDER].y*image_height

            return (wrist_x, wrist_y, elbow_x, 
                    elbow_y, shoulder_x, shoulder_y)
        except Exception as e:
            print(e)
            return None
    
    def smoothen_coords(self, pose_coords):
        """
        Function to smooth the coordinates of last n coordinates where
        n is the window size.
        Input is a list of tuple of coordinates.
        """
        if len(self.coords_array) == self.window_size:
            self.coords_array.pop(0)
        self.coords_array.append(pose_coords)
        smoothened_coords = np.array(self.coords_array).mean(axis=0)
        
        return tuple(smoothened_coords)
        
    def get_annotated_image(self, image, pose_coords):
        """
        Function to draw and visualize the coordinates in the image.
        """
        wrist_x, wrist_y, elbow_x, elbow_y, shoulder_x, shoulder_y = pose_coords
        
        annotated_image = image.copy()
        
        cv2.circle(annotated_image,
                   (int(wrist_x), int(wrist_y)), 
                   10,(0,0,255),-1)
        cv2.circle(annotated_image,
                   (int(elbow_x), int(elbow_y)),
                   10,(0,0,255),-1)
        cv2.circle(annotated_image,
                   (int(shoulder_x), int(shoulder_y)),
                   10,(0,0,255),-1)

        cv2.line(annotated_image,
                 (int(wrist_x), int(wrist_y)),
                 (int(elbow_x), int(elbow_y)),
                 (0,0,255),3)
        cv2.line(annotated_image,
                 (int(shoulder_x),int(shoulder_y)),
                 (int(elbow_x),int(elbow_y)),
                 (0,255,255),3)
        
        return annotated_image
    def write_image(self, image):
        """
        Function for displaying the image.
        """
        if self.writer is None:
            fourcc = cv2.VideoWriter_fourcc(*"MJPG")
            self.writer = cv2.VideoWriter("test6.mp4", fourcc, 25,
                (image.shape[1], image.shape[0]), True)
        
        self.writer.write(image)
        show = cv2.resize(image, None,
                          fx=1, fy =1)
        show = cv2.flip(image, 1)
        cv2.imshow("Frame", show)
        key = cv2.waitKey(1) & 0xFF
        return key
            
    def run_estimator(self):
        """
        Main Function to run the Pose Estimator.
        """
        
        capture = cv2.VideoCapture(0)
        while (capture.isOpened()):
            ret, image = capture.read(0)
            if ret:
                try:
                    coords = self.get_pose_coords(image)
                    if coords:
                        pose_coords = self.get_pose_coords(image)
                        pose_coords = self.smoothen_coords(pose_coords)
                    else:
                        pose_coords = None
                        self.write_image(image)
                        continue
                    if pose_coords:
                        annotated_image = self.get_annotated_image(image, pose_coords)
                    
                    key = self.write_image(annotated_image)
                except ValueError as ve:
                    print(ve)
                    key = self.write_image(image)
                if key == ord("q"):
                    break
        cv2.destroyAllWindows()
        capture.release()

        if self.writer is not None:
            self.writer.release()
        self.pose.close()
        
s = PoseEstimator(window_size=8)
s.run_estimator()

### To Do

# 1. Add the refactored savgol filter function
# 2. Add the refactored angle function
# 3. Add better documentation
