import java.lang.Math;

public class PoseComparison {
    
    int num_poses;
    PoseComparison(){
        this.num_poses = 1;
    }

    /**
    * Function for comparing the pose.
    *
    * @param angle_list Specify the angles of the various body parts in the
    *                   following order: left hand, right hand, left elbow, 
    *                   right elbow, left leg, right leg, left knee, right knee,
    *                   left ankle, right ankle. 
    * @return The errors between the given angles and the most similar angle of
    *         the defined poses.
    */
    public double[] comparePose(double angle_list[]){
        double angle_errors[][], reference_angles[][], 
               error;
        int i, j, classified_pose_index=0, total_error, min_error=0;
        angle_errors = new double[this.num_poses][10];
        reference_angles = getReferenceeAngles();
        for(i=0; i<reference_angles.length; i++){
            total_error = 0;
            for(j=0; j<10; j++){
                error = Math.abs(angle_list[j] - reference_angles[i][j])/reference_angles[i][j];
                angle_errors[i][j] = error;
                total_error += error;
            }
            if((total_error < min_error) | (min_error == 0)){
                min_error = total_error;
                classified_pose_index = i;
            }
        }
        System.out.println("The identified pose is :"+classified_pose_index);
        return angle_errors[classified_pose_index];
    }
    /**
    * INCOMPLETE FUNCTION. Function for getting the angles of defined poses.
    */
    private double[][] getReferenceeAngles(){
        double reference_angles[][] = {{90.0, 90.0, 175.0, 175.0, 108.0, 
                                        131.0, 118.0, 180.0, 120.0, 175.0}};
        
        return reference_angles;                                        
    }

    public static void printList(double list[]){
        for(double value: list){
            System.out.print(value+", ");
        }
        System.out.println();
    }

    public static void main(String args[]){
        PoseComparison pose_comparer = new PoseComparison();
        double angles[] = {90.0, 90.0, 175.0, 175.0, 108.0, 
            131.0, 118.0, 180.0, 120.0, 175.0};
        double comparison_result[];
        comparison_result = new double[10];                        
        comparison_result = pose_comparer.comparePose(angles);

        printList(comparison_result);
    }
}
