import java.util.*;
import java.util.regex.*;
import java.util.Scanner;

 class Sender
 {   
   private String dataLinkData;
   private String bitStuffData;
   private String physicalData = "";
 
   public Sender()
   {
      getInput ();
   }
 
   public void getInput ()
   {             
      Scanner obj = new Scanner(System.in);
      dataLinkData = obj.nextLine();
   }

   public void send(){
     stuff();
     mlt();
     showOutput();
   }
       
   public void stuff ()
   {
      bitStuffData = dataLinkData.replaceAll ("11111", "111110");
      System.out.println("Stuffing Data : " + bitStuffData);
   }

   public void mlt(){
      int level = 0;
      Boolean isUp = true;
      String[] output = {"-", "0", "+"};

      // At the beginning
      if (bitStuffData.charAt(0) == '0')
      {
        physicalData += "0";
        level = 0;
      }
      else if (bitStuffData.charAt(0) == '1')
      {
        physicalData += "+";
        level = 1;
      }

      // On running
      for (int i = 1; i < bitStuffData.length(); i++)
      {
        if (bitStuffData.charAt(i) == '0')
        {
          physicalData += output[level + 1];
        }
        else if (bitStuffData.charAt(i) == '1')
        {
          if (level != 0)
          {
            isUp = !isUp;
            physicalData += output[1];
            level = 0;
          }
          else
          {
            if (isUp)
            {
              physicalData += output[2];
              level = 1;
            }
            else
            {
              physicalData += output[0];
              level = -1;
            }
          }
        }
      }
   }

   public void showOutput ()
   {
      System.out.println(physicalData);
   }
 
   public static void main (String [] args)
   {
      Sender sender  = new Sender ();
      sender.send();
   }
 }
