import java.util.*;
import java.util.regex.*;
import java.util.Scanner;

 class BitUnStuff
 {   
    private String inputData;
    private String outputData;
 
   public BitUnStuff()
   {
      getInput ();
   }
 
   public void getInput ()
   {
      System.out.print("Input Data : ");             
      Scanner obj = new Scanner(System.in);
      inputData = obj.nextLine();
   }
   
   public void unStuff ()
   {
      outputData = inputData.replaceAll ("111110", "11111");
      showOutput ();
   }
         
   public void showOutput ()
   {
      System.out.println("After Bit-UnStuffing : " + outputData);
   }
 
   public static void main (String [] args)
   {
      BitUnStuff byteStuff  = new BitUnStuff ();
      byteStuff.unStuff();
   }
 }
