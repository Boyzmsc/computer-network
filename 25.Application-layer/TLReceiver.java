import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TLReceiver {

   // For ideal case
   // Probability of packet corrupted
   public static final double CORRUPTED = -1;
   // Probability of ack loss
   public static final double LOSS = -1;

   public static int R = 0;

   public static void main(String[] args) throws IOException, ClassNotFoundException {
      DatagramSocket serverSocket = new DatagramSocket(8888);

      DatagramSocket sender = new DatagramSocket();
      InetAddress address = InetAddress.getByName("localhost");

      byte[] receivedData = new byte[1024];

      DatagramPacket received = new DatagramPacket(receivedData, receivedData.length);
      serverSocket.receive(received);

      byte[] getData = received.getData();
      ByteArrayInputStream in = new ByteArrayInputStream(getData);
      ObjectInputStream is = new ObjectInputStream(in);
      Data data = (Data) is.readObject();

      System.out.println("\n//// Transport Layer Receiver");

      System.out.println("\nNetwork Layer >>> Transport Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Input Data : " + data.getData());
      System.out.println("Input SeqNo : " + data.getSeqNo());

      // Check Packet
      if (checkPacket(data)) {
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         ObjectOutputStream os = new ObjectOutputStream(outputStream);
         os.writeObject(data);
         byte[] sendData = outputStream.toByteArray();

         DatagramPacket output = new DatagramPacket(sendData, sendData.length, address, 9999);

         System.out.println("\n\nTransport Layer >>> Application Layer");
         System.out.println("--------------------------------------------------");
         System.out.println("Output Data : " + data.getData());

         sender.send(output);

         ///////////////////////////////////////////////////////////////////////////
         System.out.println("\n\n///////////////////////////////////////////////////////////");

         // Convert to ASCII Code
         String ack = "ACK";
         byte[] bytes = ack.getBytes(StandardCharsets.US_ASCII);

         // Set ASCII to Binary String
         data.setAck(
               Integer.toBinaryString(bytes[0]) + Integer.toBinaryString(bytes[1]) + Integer.toBinaryString(bytes[2]));
         data.setAckNo(R);

         outputStream = new ByteArrayOutputStream();
         os = new ObjectOutputStream(outputStream);
         os.writeObject(data);
         sendData = outputStream.toByteArray();

         output = new DatagramPacket(sendData, sendData.length, address, 7777);

         if (Math.random() > LOSS) {
            System.out.println("\n\nTransport Layer >>> Network Layer");
            System.out.println("--------------------------------------------------");
            System.out.println("Output AckNo : " + data.getAckNo());
            System.out.println("Output Ack Data : " + data.getAck() + "\n\n");

            sender.send(output);
         } else {
            System.out.println("\n\nLost Ack\n\n");
         }
      }

      serverSocket.close();
      sender.close();
   }

   public static boolean checkPacket(Data data) {
      if (Math.random() > CORRUPTED) {
         if (data.getSeqNo() != R) {
            System.out.println("\n\nDiscard thd Packet (= It is duplicate)");
         } else {
            System.out.println("\n\nError-free Packet Arrived");
            R = (R + 1) % 2;
         }
         return true;
      } else {
         System.out.println("\n\nCorrupted Packet Arrived, Discard the Packet\n\n");
         return false;
      }
   }
}