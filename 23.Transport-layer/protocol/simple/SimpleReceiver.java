import java.net.*;
import java.io.*;
import java.nio.*;
import java.util.*;

public class SimpleReceiver extends Thread {

   public static void main(String[] args) throws IOException {
      DatagramSocket serverSocket = new DatagramSocket(1111);
      System.out.println("////// Receiver is Ready");

      byte[] receivedData = new byte[1024];

      while (true) {

         DatagramPacket received = new DatagramPacket(receivedData, receivedData.length);
         serverSocket.receive(received);

         int input = ByteBuffer.wrap(received.getData()).getInt();

         if (input == -9999) {
            break;
         }

         System.out.println("\n\n// From Sender,");

         System.out.println("Packet (Data : " + input + ") Arrived");
         System.out.println("Deliver it to Process");
      }

      serverSocket.close();
   }
}