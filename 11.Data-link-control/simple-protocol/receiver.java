import java.util.*;
import java.util.regex.*;
import java.util.Scanner;

 class Receiver
 {   
   private String physicalData;
   private String bitStuffData = "";
   private String bitUnStuffData;
 
   public Receiver()
   {
      getInput ();
   }
 
   public void getInput ()
   {             
      Scanner obj = new Scanner(System.in);
      physicalData = obj.nextLine();
   }

   public void receive(){
      unMlt();
      unStuff();
      showOutput();
   }

   public void unMlt(){
      char previous = '0';

      // At the beginning
      if (physicalData.charAt(0) == '+')
      {
        bitStuffData += "1";
        previous = '+';
      }
      else if (physicalData.charAt(0) == '0')
      {
        bitStuffData += "0";
        previous = '0';
      }

      // On running
      for (int i = 1; i < physicalData.length(); i++)
      {
        // "+"
        if (previous == '+' && physicalData.charAt(i) == '0')
        {
          bitStuffData += "1";
          previous = '0';
        }
        else if (previous == '+' && physicalData.charAt(i) == '+')
        {
          bitStuffData += "0";
          previous = '+';
        }
        // "0"
        else if (previous == '0' && physicalData.charAt(i) == '0')
        {
          bitStuffData += "0";
          previous = '0';
        }
        else if (previous == '0' && physicalData.charAt(i) == '-')
        {
          bitStuffData += "1";
          previous = '-';
        }
        else if (previous == '0' && physicalData.charAt(i) == '+')
        {
          bitStuffData += "1";
          previous = '+';
        }
        // "-"
        else if (previous == '-' && physicalData.charAt(i) == '0')
        {
          bitStuffData += "1";
          previous = '0';
        }
        else if (previous == '-' && physicalData.charAt(i) == '-')
        {
          bitStuffData += "0";
          previous = '-';
        }
      }
      System.out.println("Stuffing Data : " + bitStuffData);
   }

   public void unStuff ()
   {
      bitUnStuffData = bitStuffData.replaceAll ("111110", "11111");
   }

   public void showOutput ()
   {
      System.out.println(bitUnStuffData);
   }
 
   public static void main (String [] args)
   {
      Receiver receiver  = new Receiver ();
      receiver.receive();
   }
 }
