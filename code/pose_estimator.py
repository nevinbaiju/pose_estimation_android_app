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
    
    def __init__(self, window_size=8, smoothing_function=None):
        """
        Window Size to specify how much frames to be considered for smoothing
        """
        if(smoothing_function == 'savgol') and ((window_size % 2) == 0):
            print('Is Here')
            print(window_size)
            self.window_size = window_size - 1
            print(self.window_size)
        else:
            self.window_size = window_size
        self.smoothing_function = smoothing_function
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
            
            left_wrist_x = get_pose[lm.LEFT_WRIST].x*image_width
            left_wrist_y = get_pose[lm.LEFT_WRIST].y*image_height
            left_elbow_x = get_pose[lm.LEFT_ELBOW].x*image_width
            left_elbow_y = get_pose[lm.LEFT_ELBOW].y*image_height
            left_shoulder_x = get_pose[lm.LEFT_SHOULDER].x*image_width
            left_shoulder_y = get_pose[lm.LEFT_SHOULDER].y*image_height
            left_hip_x = get_pose[lm.LEFT_HIP].x*image_width
            left_hip_y = get_pose[lm.LEFT_HIP].y*image_height
            left_knee_x = get_pose[lm.LEFT_KNEE].x*image_width
            left_knee_y = get_pose[lm.LEFT_KNEE].y*image_height
            left_ankle_x = get_pose[lm.LEFT_ANKLE].x*image_width
            left_ankle_y = get_pose[lm.LEFT_ANKLE].y*image_height

            right_wrist_x = get_pose[lm.RIGHT_WRIST].x*image_width
            right_wrist_y = get_pose[lm.RIGHT_WRIST].y*image_height
            right_elbow_x = get_pose[lm.RIGHT_ELBOW].x*image_width
            right_elbow_y = get_pose[lm.RIGHT_ELBOW].y*image_height
            right_shoulder_x = get_pose[lm.RIGHT_SHOULDER].x*image_width
            right_shoulder_y = get_pose[lm.RIGHT_SHOULDER].y*image_height
            right_hip_x = get_pose[lm.RIGHT_HIP].x*image_width
            right_hip_y = get_pose[lm.RIGHT_HIP].y*image_height
            right_knee_x = get_pose[lm.RIGHT_KNEE].x*image_width
            right_knee_y = get_pose[lm.RIGHT_KNEE].y*image_height
            right_ankle_x = get_pose[lm.RIGHT_ANKLE].x*image_width
            right_ankle_y = get_pose[lm.RIGHT_ANKLE].y*image_height

            nose_x = get_pose[lm.NOSE].x*image_width
            nose_y = get_pose[lm.NOSE].y*image_height

            return (left_wrist_x, left_wrist_y, left_elbow_x, left_elbow_y, left_shoulder_x, left_shoulder_y, left_hip_x, left_hip_y, left_knee_x, left_knee_y, left_ankle_x, left_ankle_y,
                    right_wrist_x, right_wrist_y, right_elbow_x, right_elbow_y, right_shoulder_x, right_shoulder_y, right_hip_x, right_hip_y, right_knee_x, right_knee_y, right_ankle_x, right_ankle_y,
                    nose_x,nose_y)

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
        if self.smoothing_function == 'mean':
            smoothened_coords = np.array(self.coords_array).mean(axis=0)
        elif self.smoothing_function == 'savgol':
            try:
                savgol = lambda arr: savgol_filter(arr, self.window_size, 1)[-1]
                coords_np_arr = np.array(self.coords_array)
                smoothened_coords = np.apply_along_axis(savgol, 0, 
                                                        coords_np_arr)
                self.coords_array.pop()
                self.coords_array.append(smoothened_coords)
            except ValueError as ve:
                print(ve)
                return pose_coords
        else:
            return pose_coords
        
        return tuple(smoothened_coords)
        
    def get_annotated_image(self, image, pose_coords):
        """
        Function to draw and visualize the coordinates in the image.
        """
        left_wrist_x, left_wrist_y, left_elbow_x, left_elbow_y, left_shoulder_x, left_shoulder_y, left_hip_x, left_hip_y, left_knee_x, left_knee_y, left_ankle_x, left_ankle_y, right_wrist_x, right_wrist_y, right_elbow_x, right_elbow_y, right_shoulder_x, right_shoulder_y, right_hip_x, right_hip_y, right_knee_x, right_knee_y, right_ankle_x, right_ankle_y, nose_x, nose_y = pose_coords
        
        annotated_image = image.copy()
        
        ##Drawing Cirlces
        #Nose
        cv2.circle(annotated_image,
                   (int(nose_x), int(nose_y)),
                   10,(0,0,255),-1)
        #Shoulders
        cv2.circle(annotated_image,
                   (int(left_shoulder_x), int(left_shoulder_y)),
                   10,(0,0,255),-1)
        cv2.circle(annotated_image,
                   (int(right_shoulder_x), int(right_shoulder_y)),
                   10,(0,0,255),-1)
        #Elbows
        cv2.circle(annotated_image,
                   (int(left_elbow_x), int(left_elbow_y)),
                   10,(0,0,255),-1)
        cv2.circle(annotated_image,
                   (int(right_elbow_x), int(right_elbow_y)),
                   10,(0,0,255),-1)
        #Wrists
        cv2.circle(annotated_image,
                   (int(left_wrist_x), int(left_wrist_y)), 
                   10,(0,0,255),-1)
        cv2.circle(annotated_image,
                   (int(right_wrist_x), int(right_wrist_y)), 
                   10,(0,0,255),-1)
        #Hips
        cv2.circle(annotated_image,
                   (int(left_hip_x), int(left_hip_y)), 
                   10,(0,0,255),-1)
        cv2.circle(annotated_image,
                   (int(right_hip_x), int(right_hip_y)), 
                   10,(0,0,255),-1)
        #Knees
        cv2.circle(annotated_image,
                   (int(left_knee_x), int(left_knee_y)), 
                   10,(0,0,255),-1)
        cv2.circle(annotated_image,
                   (int(right_knee_x), int(right_knee_y)), 
                   10,(0,0,255),-1)
        #Ankles
        cv2.circle(annotated_image,
                   (int(left_ankle_x), int(left_ankle_y)), 
                   10,(0,0,255),-1)
        cv2.circle(annotated_image,
                   (int(right_ankle_x), int(right_ankle_y)), 
                   10,(0,0,255),-1)
    
        ##Drawing Lines
        #Nose-Shoulder
        cv2.line(annotated_image,
                 (int(nose_x), int(nose_y)),
                 (int((left_shoulder_x+right_shoulder_x)/2), int((left_shoulder_y+right_shoulder_y)/2)),
                 (0,0,255),3)
        #Shoulder
        cv2.line(annotated_image,
                 (int(left_shoulder_x), int(left_shoulder_y)),
                 (int(right_shoulder_x), int(right_shoulder_y)),
                 (0,0,255),3)
        #Shoulder-Elbow
        cv2.line(annotated_image,
                 (int(left_shoulder_x), int(left_shoulder_y)),
                 (int(left_elbow_x), int(left_elbow_y)),
                 (0,0,255),3)
        cv2.line(annotated_image,
                 (int(right_shoulder_x), int(right_shoulder_y)),
                 (int(right_elbow_x), int(right_elbow_y)),
                 (0,0,255),3)
        #Elbow-Wrist
        cv2.line(annotated_image,
                 (int(left_elbow_x), int(left_elbow_y)),
                 (int(left_wrist_x), int(left_wrist_y)),
                 (0,0,255),3)
        cv2.line(annotated_image,
                 (int(right_elbow_x), int(right_elbow_y)),
                 (int(right_wrist_x), int(right_wrist_y)),
                 (0,0,255),3)                     
        #Shoulder-Hip
        cv2.line(annotated_image,
                 (int(left_shoulder_x), int(left_shoulder_y)),
                 (int(left_hip_x), int(left_hip_y)),
                 (0,0,255),3)   
        cv2.line(annotated_image,
                 (int(right_shoulder_x), int(right_shoulder_y)),
                 (int(right_hip_x), int(right_hip_y)),
                 (0,0,255),3)
        #Hip
        cv2.line(annotated_image,
                 (int(left_hip_x), int(left_hip_y)),
                 (int(right_hip_x), int(right_hip_y)),
                 (0,0,255),3)   
        #Hip-Knee
        cv2.line(annotated_image,
                 (int(left_hip_x), int(left_hip_y)),
                 (int(left_knee_x), int(left_knee_y)),
                 (0,0,255),3)   
        cv2.line(annotated_image,
                 (int(right_hip_x), int(right_hip_y)),
                 (int(right_knee_x), int(right_knee_y)),
                 (0,0,255),3)
        #Knee-Ankle
        cv2.line(annotated_image,
                 (int(left_knee_x), int(left_knee_y)),
                 (int(left_ankle_x), int(left_ankle_y)),
                 (0,0,255),3)   
        cv2.line(annotated_image,
                 (int(right_knee_x), int(right_knee_y)),
                 (int(right_ankle_x), int(right_ankle_y)),
                 (0,0,255),3)  
        
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
            # Read a frame
            ret, image = capture.read(0)
            if ret:
                try:
                    # Get the pose coordinates in a tuple
                    pose_coords = self.get_pose_coords(image)
                    if pose_coords:
                        # If poses are detected then apply the smoothing filter
                        # And annotate the image
                        pose_coords = self.smoothen_coords(pose_coords)
                        annotated_image = self.get_annotated_image(image, pose_coords)
                    else:
                        # If no poses are detected, then just display the frame
                        pose_coords = None
                        self.write_image(image)
                        continue
                    # Write the annotated image
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
