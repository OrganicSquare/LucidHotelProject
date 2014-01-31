package communication;

public class Util {
 public static Boolean DEBUG_MODE = false;

  /**
     * If Debug mode is activated this will log the reports from program
     * @param message The message returned if debug mode is active
     */
 public static void logC(String content){
  if(DEBUG_MODE){
   System.out.println(content);
  }
 }
}