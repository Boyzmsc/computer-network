import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class DLSender {

   private Random random;
   private int k, tbSetting;
   private final double SUCCESS = 0.1;
   private final int csmaSetting = 2;
   private final int tfrSetting = 2;
   private boolean isTransDone;
   private boolean isColliDetected;
   private boolean success;

   public void main(String[] args) throws IOException, ClassNotFoundException {
      DatagramSocket serverSocket = new DatagramSocket(2222);

      DatagramSocket sender = new DatagramSocket();
      InetAddress address = InetAddress.getByName("localhost");

      byte[] receivedData = new byte[1024];

      DatagramPacket received = new DatagramPacket(receivedData, receivedData.length);
      serverSocket.receive(received);

      byte[] getData = received.getData();
      ByteArrayInputStream in = new ByteArrayInputStream(getData);
      ObjectInputStream is = new ObjectInputStream(in);
      Data data = (Data) is.readObject();

      int L3Port = received.getPort();

      System.out.println("\n//// Datalink Layer Sender");
      System.out.println("--------------------------------------------------");
      System.out.println("Received From Network Layer Sender");
      System.out.println("Input Data : " + data.getData());

      // Bit-Stuffing
      String bitStuff = stuff(data.getData());
      data.setData(bitStuff);

      // CSMA-CD
      csmacd();

      System.out.println("\n--------------------------------------------------");
      System.out.println("Send To Physical Layer Sender");
      System.out.println("Output Data : " + data.getData());

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(outputStream);
      os.writeObject(data);
      byte[] sendData = outputStream.toByteArray();

      DatagramPacket output = new DatagramPacket(sendData, sendData.length, address, 1111);
      sender.send(output);

      ///////////////////////////////////////////////////////////////////////////

      received = new DatagramPacket(receivedData, receivedData.length);
      serverSocket.receive(received);

      getData = received.getData();
      in = new ByteArrayInputStream(getData);
      is = new ObjectInputStream(in);
      data = (Data) is.readObject();

      System.out.println("\n--------------------------------------------------");
      System.out.println("Received From Physical Layer Sender");
      System.out.println("Input Data : " + data.getData());

      // Bit-Unstuffing
      String bitUnstuff = unStuff(data.getData());
      data.setData(bitUnstuff);

      System.out.println("\n--------------------------------------------------");
      System.out.println("Send To Network Layer Sender");
      System.out.println("Output Data : " + data.getData());

      outputStream = new ByteArrayOutputStream();
      os = new ObjectOutputStream(outputStream);
      os.writeObject(data);
      sendData = outputStream.toByteArray();

      output = new DatagramPacket(sendData, sendData.length, address, L3Port);
      sender.send(output);
   }

   // Bit-Stuffing, Bit-Unstuffing
   public String stuff(String inputData) {
      String outputData = inputData.replaceAll("11111", "111110");
      return outputData;
   }

   public String unStuff(String inputData) {
      String outputData = inputData.replaceAll("111110", "11111");
      return outputData;
   }

   // CSMACD
   public void csmacd() {
      k = 0;
      success = false;
      while (!success) {
         System.out.println("\nAttempt " + (k + 1) + ":");

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

   // CSMA : non-Persistent
   // True : Channel busy , False : Channel idle
   public void csma(int seconds) {
      int waitSeconds;
      if (Math.random() > SUCCESS) {
         System.out.println("Channel is busy, waiting a ramdom amount of time ...");
         waitSeconds = random.nextInt(seconds);
         wait(waitSeconds);
         while (Math.random() > SUCCESS) {
            waitSeconds = random.nextInt(seconds);
            wait(waitSeconds);
         }
      }
      System.out.println("Channel is idle, ready to transmit ...");
   } // End csma

   // Check transmission done
   // True : Transmission done , False : Transmission not done
   public boolean isTransDone() {
      return (Math.random() > SUCCESS);
   } // End isTransDone

   // Check collision detected
   // True : Collision detected , False : Collision not detected
   public boolean isColliDetected() {
      return (Math.random() > SUCCESS);
   } // End isColliDetected

   public void wait(int seconds) {
      try {
         Thread.sleep(1000 * seconds);
      } catch (InterruptedException e) {
      }
   } // End wait
}