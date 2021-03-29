import java.util.Map;
import java.util.HashMap;
import java.io.*;

public class PoseAlignment {
    Map<String, Boolean> pose_alignments = new HashMap<String, Boolean>();

    /**
    * Constructor for reading the defined pose alignments and storing them in a hashmap.
    */
    PoseAlignment(){
        this.pose_alignments = getPoseAlignment();
    }

    /**
    * Function reading the defined poses from a file.
    * Currently statically written, can be modified in the future.
    * @return the hashmap of the alignments for various poses.
    */
    private Map<String, Boolean> getPoseAlignment(){
        Map<String, Boolean> pose_alignments = new HashMap<String, Boolean>();
        pose_alignments.put("demo_pose_x", true);
        
        return pose_alignments;
    }

    /**
    * Function for classifying the alignment (Front Facing or Back facing).
    *
    * @param left_shoulder The coordinate of the left shoulder
    * @param right_shoulder The coordinate of the right shoulder
    * @return True if front facing, False if back facing.
    */
    private boolean classifyPoseAlignment(double left_shoulder, double right_shoulder){
        if ((left_shoulder - right_shoulder) > 0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
    * Function for checking if the pose is aligned as expected.
    *
    * @param left_shoulder The coordinate of the left shoulder.
    * @param right_shoulder The coordinate of the right shoulder.
    * @param pose_name The name of the pose suffixed by the x or the y coodinate being checked.
    * @return True if pose is aligned as expected False otherwise. 
    * Will return true if the pose alignment is not defined.
    */
    public boolean validatePoseAlignment(double left_shoulder, double right_shoulder, String pose_name){
        boolean current_pose_alignment, actual_pose_alignment;
        current_pose_alignment = classifyPoseAlignment(left_shoulder, right_shoulder);
        try {
            actual_pose_alignment = this.pose_alignments.get(pose_name);   
        } catch (NullPointerException e) {
            System.out.println("Alignment for "+pose_name+" is not defined");
            return true;
        }

        if (current_pose_alignment == actual_pose_alignment){
            return true;
        }
        else{
            return false;
        }
    }

    public static void main(String args[]) {
        PoseAlignment alignment_checker = new PoseAlignment();
        System.out.println(alignment_checker.validatePoseAlignment(10.0, 12.0, "demo_pose_x"));
        System.out.println(alignment_checker.validatePoseAlignment(0.0, 0.2, "demo_pose"));
        System.out.println(alignment_checker.validatePoseAlignment(10.0, 8.0, "demo_pose"));
    }
}
