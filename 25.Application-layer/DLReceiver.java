import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class DLReceiver {

   private static Random random = new Random();
   private static int k, tbSetting;
   private static final int csmaSetting = 1;
   private static final int tfrSetting = 1;
   private static boolean isTransDone;
   private static boolean isColliDetected;
   private static boolean success;

   public static void main(String[] args) throws IOException, ClassNotFoundException {
      DatagramSocket serverSocket = new DatagramSocket(6666);

      DatagramSocket sender = new DatagramSocket();
      InetAddress address = InetAddress.getByName("localhost");

      byte[] receivedData = new byte[1024];

      DatagramPacket received = new DatagramPacket(receivedData, receivedData.length);
      serverSocket.receive(received);

      byte[] getData = received.getData();
      ByteArrayInputStream in = new ByteArrayInputStream(getData);
      ObjectInputStream is = new ObjectInputStream(in);
      Data data = (Data) is.readObject();

      System.out.println("\n//// Datalink Layer Receiver");

      System.out.println("\nPhysical Layer >>> Datalink Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Input Data : " + data.getData());

      // Bit-Unstuffing
      String bitUnstuff = unStuff(data.getData());
      data.setData(bitUnstuff);

      System.out.println("\n\nDatalink Layer >>> Network Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Output Data : " + data.getData());

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(outputStream);
      os.writeObject(data);
      byte[] sendData = outputStream.toByteArray();

      DatagramPacket output = new DatagramPacket(sendData, sendData.length, address, 7777);
      sender.send(output);

      ///////////////////////////////////////////////////////////////////////////
      System.out.println("\n\n///////////////////////////////////////////////////////////");

      received = new DatagramPacket(receivedData, receivedData.length);
      serverSocket.receive(received);

      getData = received.getData();
      in = new ByteArrayInputStream(getData);
      is = new ObjectInputStream(in);
      data = (Data) is.readObject();

      System.out.println("\n\nNetwork Layer >>> Datalink Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Input Ack Data : " + data.getAck());

      // Bit-Stuffing
      String bitStuff = stuff(data.getAck());
      data.setAck(bitStuff);

      // CSMA-CD
      csmacd();

      System.out.println("\n\nDatalink Layer >>> Physical Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Output Ack Data : " + data.getAck() + "\n\n");

      outputStream = new ByteArrayOutputStream();
      os = new ObjectOutputStream(outputStream);
      os.writeObject(data);
      sendData = outputStream.toByteArray();

      output = new DatagramPacket(sendData, sendData.length, address, 5555);
      sender.send(output);

      serverSocket.close();
      sender.close();
   }

   // Bit-Stuffing, Bit-Unstuffing
   public static String stuff(String inputData) {
      String outputData = inputData.replaceAll("11111", "111110");
      return outputData;
   }

   public static String unStuff(String inputData) {
      String outputData = inputData.replaceAll("111110", "11111");
      return outputData;
   }

   // CSMACD
   public static void csmacd() {
      k = 0;
      success = false;
      while (!success) {
         int count = k + 1;
         System.out.println("\n\nAttempt " + count + ":");

         csma(csmaSetting);

         isTransDone = isTransDone();
         isColliDetected = isColliDetected();
         if (!isTransDone && !isColliDetected) {
            // Transmit and Receive
            while (!isTransDone && !isColliDetected) {
               isTransDone = isTransDone();
               isColliDetected = isColliDetected();
            }
         }

         if (isTransDone) {
            System.out.println("Transmission done ...");
         } else {
            System.out.println("Transmission not done ...");
         }

         if (isColliDetected) {
            System.out.println("Collision detected, sending jamming signal ...");
            success = false;
         } else {
            System.out.println("Collision not detected, Success");
            success = true;
         }

         if (!success) {
            // For Ideal Case
            // System.out.println("The whole process was aborted. We need to try another
            // time.");
            k++;

            System.out.println("Waiting the TB timer to expire and to start a new attempt ...");
            // Random number R between 0 and 2^k-1
            // TB = R * Tfr
            tbSetting = tfrSetting * random.nextInt((int) Math.pow(2, k));
            wait(tbSetting);
         }

         // To check event
         wait(3);
      }
   }

   // CSMA : non-Persistent
   // True : Channel busy , False : Channel idle
   public static void csma(int seconds) {
      if (Math.random() > 0.5) {
         System.out.println("Channel is busy, waiting a ramdom amount of time ...");
         wait(seconds);
         while (Math.random() > 0.5) {
            wait(seconds);
         }
      }
      System.out.println("Channel is idle, ready to transmit ...");
   } // End csma

   // Check transmission done
   // True : Transmission done , False : Transmission not done
   public static boolean isTransDone() {
      return (Math.random() > 0.3);
   } // End isTransDone

   // Check collision detected
   // True : Collision detected , False : Collision not detected
   public static boolean isColliDetected() {
      return (Math.random() < 0.3);
   } // End isColliDetected

   public static void wait(int seconds) {
      try {
         Thread.sleep(1000 * seconds);
      } catch (InterruptedException e) {
      }
   } // End wait
}