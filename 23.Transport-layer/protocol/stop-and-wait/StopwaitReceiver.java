import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class StopwaitReceiver {

   // probability of ack loss
   public static final double LOSS = 0.1;
   // probability of packet corrupted
   public static final double CORRUPTED = 0.1;

   public static void main(String[] args) throws IOException {

      DatagramSocket serverSocket = new DatagramSocket(8000);
      System.out.print("////// Receiver is Ready");

      byte[] receivedData = new byte[1024];
      byte[] sendData = new byte[1024];

      int R = 0;

      while (true) {

         DatagramPacket received = new DatagramPacket(receivedData, receivedData.length);
         serverSocket.receive(received);

         int input = ByteBuffer.wrap(received.getData()).getInt();

         if (input == -9999) {
            break;
         }

         System.out.println("\n\n// From Sender,");

         if (Math.random() > CORRUPTED) {
            System.out.println("Error-free Packet (seqNo : " + input + ") Arrived");

            InetAddress address = received.getAddress();
            int port = received.getPort();

            if (input != R) {
               System.out.println("Discard thd Packet (seqNo : " + input + ") (= It is duplicate)");
            } else {
               System.out.println("Deliver the Message to Application");
               System.out.println("Slide the Receive Window Forward");
               R = (R + 1) % 2;
            }

            System.out.println("Sending Ack (ackNo : " + R + ")");
            sendData = ByteBuffer.allocate(4).putInt(R).array();

            DatagramPacket output = new DatagramPacket(sendData, sendData.length, address, port);

            if (Math.random() > LOSS) {
               serverSocket.send(output);
            } else {
               System.out.println("Ack (ackNo : " + R + ") Lost");
            }
         } else {
            System.out.println("Corrupted Packet (seqNo : " + input + ") Arrived, Discard the Packet");
         }
      }

      serverSocket.close();
   }
}