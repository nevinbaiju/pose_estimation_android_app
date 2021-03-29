import java.io.FileReader;  
import java.io.BufferedReader;
import java.util.*;


/**
* This is a program for reading the coordinates from
* a csv file without any headers. It takes parameters, 
* window size and smoothing method. As of now, only mean
* smoothing is used, no error handling is defined if 
* any others are specified. Please note that I am a noob
* in java, so anything to improve the efficiency in handling
* or computing the data could be done. This is just to 
* illustrate the smoothing function that we would be using.
*
* The arguments in the main function are hard coded, please change them before running.
*/
public class CoordsSmoothening {  

    private List<List<Double>> coords_array = new ArrayList<List<Double>>(); 
    private int window_size;
    private String smoothing_method;

    /**
    * Constructor of CoordsSmoothening.
    *
    * @param window_size Specify the window size of frames to be considered for 
    * smoothening
    * @param smoothing_method The smoothing method to be used. Currently only
    * 'mean' smoothing is supported
    */
    CoordsSmoothening(int window_size, String smoothing_method){
        this.window_size = window_size;
        this.smoothing_method = smoothing_method;
    }

    
    /**
    * Function for taking the average across axis 1 of the coords_array list of the class
    *
    * @return The averaged list of coordinates.
    */
    private List<Double> average_coords(){
        // Please call this out if averaging could be done more efficiently :)
        // I'm a noob in Java.
        List<Double> current_coords;
        List<Double> averaged_coords = new ArrayList<Double>();
        for (int k=0; k < this.coords_array.get(0).size(); k++){
            averaged_coords.add(0.0);
        }
        for (int i=0; i < this.coords_array.size(); i++){
            current_coords = this.coords_array.get(i);
            for (int j=0; j < current_coords.size(); j++){
                averaged_coords.set(j, averaged_coords.get(j) + current_coords.get(j));
            }
        }
        for (int i=0; i < averaged_coords.size(); i++){
            averaged_coords.set(i, averaged_coords.get(i)/this.coords_array.size());
        }
        return averaged_coords;
    }
    
    /**
    * Function for applying the smoothening to the coords_array of the class.
    *
    * @param pose_coords Input the new coordinate.
    *
    * @return The smoothened coordinates of the input based on the cached coords of 
    * window size.
    */
    public List<Double> smoothen_coords(List<Double> pose_coords){
        if (pose_coords.size() == this.window_size){
            this.coords_array.remove(0);
        }
        List<Double> smoothened_coords = new ArrayList<Double>();
        this.coords_array.add(pose_coords);
        if (this.smoothing_method == "mean"){
            smoothened_coords = this.average_coords();
        }

        return smoothened_coords;
    }

    /**
    * A helper function for converting array of strings to float.
    *
    * @param arr Array of strings.
    *
    * @return The array containing the data converted to double.
    */
    public static List<Double> arr_to_int(String arr[]){
      int length = arr.length;
      List<Double> doubleArray = new ArrayList<Double>();;

      for(int i=0; i<length; i++){
        doubleArray.add(Double.parseDouble(arr[i]));
      }

      return doubleArray;
    }

    /**
    * A helper function for printing a list.
    *
    * @param list A list of float values.
    */
    public static void print_array(List<Double> list) {
        for (Double coord : list) {
            System.out.print(coord+", "); 
        }
        System.out.println();
    }

    /**
    * The main function. Reads the file, makes the CoordsSmoothening object and feeds in 
    * the converted list into the smoothening algorithm
    *
    * @param args Unused.
    */
    public static void main(String args[])throws Exception{    
      
          FileReader fr=new FileReader("");    
          BufferedReader br=new BufferedReader(fr);
          String line;
          String[] str_arr = {}; 
          List<Double> current_coords = new ArrayList<Double>();

          CoordsSmoothening coord_smoothing = new CoordsSmoothening(3, "mean");
          while((line = br.readLine())!= null){
            str_arr = line.split(",", 0); 
            current_coords = arr_to_int(str_arr);
            System.out.println("Actual Coordinates: ");
            print_array(current_coords);
            current_coords = coord_smoothing.smoothen_coords(current_coords);
            System.out.println("Smoothened coordinates: ");
            print_array(current_coords);
          }
          fr.close();    
    }    
}  