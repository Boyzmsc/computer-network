import java.util.*;
import java.util.regex.*;
import java.util.Scanner;

 class BitUnstuff
 {   
    private String inputData;
    private String outputData;
 
   public BitUnstuff()
   {
      getInput ();
   }
 
   public void getInput ()
   {             
      Scanner obj = new Scanner(System.in);
      inputData = obj.nextLine();
   }
   
   public void unstuff ()
   {
      outputData = inputData.replaceAll ("111110", "11111");
      showOutput ();
   }
         
   public void showOutput ()
   {
      System.out.println(outputData);
   }
 
   public static void main (String [] args)
   {
      BitUnstuff bytestuff  = new BitUnstuff ();
      bytestuff.unstuff();
   }
 }
