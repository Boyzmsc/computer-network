import java.net.*;
import java.io.*;
import java.nio.*;
import java.util.*;

public class SimpleSender {

   public static final double LOSS = 0.1;

   public static void main(String[] args) throws IOException, InterruptedException {
      DatagramSocket sender = new DatagramSocket();
      InetAddress address = InetAddress.getByName("localhost");

      byte[] sendData = new byte[1024];

      Scanner scanner = new Scanner(System.in);
      System.out.print("Enter Number of Packets : ");
      int counter_max = scanner.nextInt();

      for (int counter = 0; counter < counter_max; counter++) {
         System.out.println("\n\n////// Request came from Application...");

         // dummy data
         int data = (int) (Math.random() * 100) + 1;
         sendData = ByteBuffer.allocate(4).putInt(data).array();

         System.out.println("Sending Packet (Data : " + data + ")");

         DatagramPacket output = new DatagramPacket(sendData, sendData.length, address, 1111);

         if (Math.random() > LOSS) {
            sender.send(output);
         } else {
            System.out.println("Packet (Data : " + data + ") Lost");
         }

         Thread.sleep(2000);
      }

      sendData = ByteBuffer.allocate(4).putInt(-9999).array();
      DatagramPacket end_output = new DatagramPacket(sendData, sendData.length, address, 1111);
      sender.send(end_output);
      sender.close();
   }
}