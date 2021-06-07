import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;

class StopwaitSender {

   // probability of packet loss
   public static final double LOSS = 0.1;
   // probability of ack corrupted
   public static final double CORRUPTED = 0.1;

   public static final int TIMER = 2000;

   public static void main(String args[]) throws IOException, InterruptedException {
      DatagramSocket sender = new DatagramSocket();
      InetAddress address = InetAddress.getByName("localhost");

      byte[] receivedData = new byte[1024];
      byte[] sendData = new byte[1024];

      int seqNo = 1;

      Scanner scanner = new Scanner(System.in);
      System.out.print("Enter Number of Packets : ");
      int counter_max = scanner.nextInt();

      for (int counter = 0; counter < counter_max; counter++) {
         boolean isBlocked = true;
         System.out.println("\n////// Request came from Application...");

         int tmp = seqNo;

         while (isBlocked) {
            seqNo = (seqNo + 1) % 2;

            sendData = ByteBuffer.allocate(4).putInt(seqNo).array();

            System.out.println("\nSending Packet (seqNo : " + seqNo + ")");
            System.out.println("Start the timer");

            try {
               sender.setSoTimeout(TIMER);

               // create packet
               DatagramPacket output = new DatagramPacket(sendData, sendData.length, address, 8000);

               if (Math.random() > LOSS) {
                  sender.send(output);
               } else {
                  System.out.println("Lost Packet (seqNo : " + seqNo + ")");
               }

               DatagramPacket input = new DatagramPacket(receivedData, receivedData.length);
               sender.receive(input);

               int in = ByteBuffer.wrap(input.getData()).getInt();

               System.out.println("// From Receiver,");

               if (Math.random() > CORRUPTED) {
                  if (in == (seqNo + 1) % 2) {
                     isBlocked = false;
                     System.out.println("Error-free Ack Arrived (ackNo : " + in + ")");
                     System.out.println("Stop the Timer\n");
                  } else {
                     System.out.println("Error-free Ack (ackNo : " + in
                           + ") Not Related to the only Outstanding Packet Arrived, Discard the Ack\n");
                     seqNo = tmp;
                  }
               } else {
                  System.out.println("Corrupted Ack Arrived (ackNo : " + in + "), Discard the Ack\n");
                  seqNo = tmp;
               }

            } catch (SocketTimeoutException exception) {
               System.out.println("Time-out");
               System.out.println("Ready for Resending Packet (seqNo : " + seqNo + ")\n");
               seqNo = tmp;
            }
         }
         Thread.sleep(5000);
      }

      sendData = ByteBuffer.allocate(4).putInt(-9999).array();
      DatagramPacket end_output = new DatagramPacket(sendData, sendData.length, address, 8000);
      sender.send(end_output);
      sender.close();
   }
}