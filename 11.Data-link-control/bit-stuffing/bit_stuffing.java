import java.util.*;
import java.util.regex.*;
import java.util.Scanner;

 class BitStuff
 {   
    private String inputData;
    private String outputData;
 
   public BitStuff()
   {
      getInput ();
   }
 
   public void getInput ()
   {             
      System.out.print("Input Data : ");
      Scanner obj = new Scanner(System.in);
      inputData = obj.nextLine();
   }
       
   public void stuff ()
   {
      outputData = inputData.replaceAll ("11111", "111110");
      showOutput ();
   }
         
   public void showOutput ()
   {
      System.out.println("After Bit-Stuffing : " + outputData);
   }
 
   public static void main (String [] args)
   {
      BitStuff byteStuff  = new BitStuff ();
      byteStuff.stuff();
   }
 }
