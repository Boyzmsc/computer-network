import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class PLSender {

   public static void main(String[] args) throws IOException, ClassNotFoundException {
      DatagramSocket serverSocket = new DatagramSocket(1111);

      DatagramSocket sender = new DatagramSocket();
      InetAddress address = InetAddress.getByName("localhost");

      byte[] receivedData = new byte[1024];

      DatagramPacket received = new DatagramPacket(receivedData, receivedData.length);
      serverSocket.receive(received);

      byte[] getData = received.getData();
      ByteArrayInputStream in = new ByteArrayInputStream(getData);
      ObjectInputStream is = new ObjectInputStream(in);
      Data data = (Data) is.readObject();

      System.out.println("\n//// Physical Layer Sender");

      System.out.println("\nDatalink Layer >>> Physical Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Input Data : " + data.getData());

      // MLT-3
      String afterMlt = mlt(data.getData());
      data.setData(afterMlt);

      System.out.println("\n\nPhysical Layer >>> Physical Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Output Data : " + data.getData());

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(outputStream);
      os.writeObject(data);
      byte[] sendData = outputStream.toByteArray();

      DatagramPacket output = new DatagramPacket(sendData, sendData.length, address, 5555);
      sender.send(output);

      ///////////////////////////////////////////////////////////////////////////

      received = new DatagramPacket(receivedData, receivedData.length);
      serverSocket.receive(received);

      getData = received.getData();
      in = new ByteArrayInputStream(getData);
      is = new ObjectInputStream(in);
      data = (Data) is.readObject();

      System.out.println("\n\nPhysical Layer >>> Physical Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Input Data : " + data.getData());

      // Un MLT-3
      String afterUnMlt = unMlt(data.getData());
      data.setData(afterUnMlt);

      System.out.println("\n\nPhysical Layer >>> Datalink Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Output Data : " + data.getData() + "\n");

      outputStream = new ByteArrayOutputStream();
      os = new ObjectOutputStream(outputStream);
      os.writeObject(data);
      sendData = outputStream.toByteArray();

      output = new DatagramPacket(sendData, sendData.length, address, 2222);
      sender.send(output);

      serverSocket.close();
      sender.close();
   }

   // MLT-3
   public static String mlt(String inputData) {
      String physicalData = "";
      int level = 0;
      Boolean isUp = true;
      String[] output = { "-", "0", "+" };

      // At the beginning
      if (inputData.charAt(0) == '0') {
         physicalData += "0";
         level = 0;
      } else if (inputData.charAt(0) == '1') {
         physicalData += "+";
         level = 1;
      }

      // On running
      for (int i = 1; i < inputData.length(); i++) {
         if (inputData.charAt(i) == '0') {
            physicalData += output[level + 1];
         } else if (inputData.charAt(i) == '1') {
            if (level != 0) {
               isUp = !isUp;
               physicalData += output[1];
               level = 0;
            } else {
               if (isUp) {
                  physicalData += output[2];
                  level = 1;
               } else {
                  physicalData += output[0];
                  level = -1;
               }
            }
         }
      }
      return physicalData;
   }

   // Un MLT-3
   public static String unMlt(String physicalData) {
      String rlt = "";
      char previous = '0';

      // At the beginning
      if (physicalData.charAt(0) == '+') {
         rlt += "1";
         previous = '+';
      } else if (physicalData.charAt(0) == '0') {
         rlt += "0";
         previous = '0';
      }

      // On running
      for (int i = 1; i < physicalData.length(); i++) {
         // "+"
         if (previous == '+' && physicalData.charAt(i) == '0') {
            rlt += "1";
            previous = '0';
         } else if (previous == '+' && physicalData.charAt(i) == '+') {
            rlt += "0";
            previous = '+';
         }
         // "0"
         else if (previous == '0' && physicalData.charAt(i) == '0') {
            rlt += "0";
            previous = '0';
         } else if (previous == '0' && physicalData.charAt(i) == '-') {
            rlt += "1";
            previous = '-';
         } else if (previous == '0' && physicalData.charAt(i) == '+') {
            rlt += "1";
            previous = '+';
         }
         // "-"
         else if (previous == '-' && physicalData.charAt(i) == '0') {
            rlt += "1";
            previous = '0';
         } else if (previous == '-' && physicalData.charAt(i) == '-') {
            rlt += "0";
            previous = '-';
         }
      }
      return rlt;
   }
}