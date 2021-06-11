import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class TLSender {

   public static int seqNo = 1;
   public static int tmp;

   // For ideal case
   // Probability of ack corrupted
   public static final double CORRUPTED = -1;
   // Probability of packet loss
   public static final double LOSS = -1;

   public static final int TIMER = 2000;

   public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
      DatagramSocket serverSocket = new DatagramSocket(4444);

      DatagramSocket sender = new DatagramSocket();
      InetAddress address = InetAddress.getByName("localhost");

      byte[] receivedData = new byte[1024];

      DatagramPacket received = new DatagramPacket(receivedData, receivedData.length);
      serverSocket.receive(received);

      byte[] getData = received.getData();
      ByteArrayInputStream in = new ByteArrayInputStream(getData);
      ObjectInputStream is = new ObjectInputStream(in);
      Data data = (Data) is.readObject();

      System.out.println("\n//// Transport Layer Sender");

      System.out.println("\nApplication Layer >>> Transport Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Input Data : " + data.getData());

      tmp = seqNo;
      seqNo = (seqNo + 1) % 2;
      data.setSeqNo(seqNo);

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(outputStream);
      os.writeObject(data);
      byte[] sendData = outputStream.toByteArray();

      DatagramPacket output = new DatagramPacket(sendData, sendData.length, address, 3333);

      if (Math.random() > LOSS) {
         System.out.println("\n\nTransport Layer >>> Network Layer");
         System.out.println("--------------------------------------------------");
         System.out.println("Output Data : " + data.getData());
         System.out.println("Output SeqNo : " + data.getSeqNo());

         sender.send(output);
      } else {
         System.out.println("\n\nLost Packet");
      }

      ///////////////////////////////////////////////////////////////////////////
      System.out.println("\n\n///////////////////////////////////////////////////////////");

      try {
         // For ideal case
         // sender.setSoTimeout(TIMER);

         received = new DatagramPacket(receivedData, receivedData.length);
         serverSocket.receive(received);

         getData = received.getData();
         in = new ByteArrayInputStream(getData);
         is = new ObjectInputStream(in);
         data = (Data) is.readObject();

         System.out.println("\n\nNetwork Layer >>> Transport Layer");
         System.out.println("--------------------------------------------------");
         System.out.println("Input AckNo : " + data.getAckNo());
         System.out.println("Input Ack Data : " + data.getAck());

         // Check Ack
         if (checkAck(data)) {
            System.out.println("\n\nTransport Layer >>> Application Layer");
            System.out.println("--------------------------------------------------");
            System.out.println("Sending Received Check Message\n\n");

            outputStream = new ByteArrayOutputStream();
            os = new ObjectOutputStream(outputStream);
            os.writeObject(data);
            sendData = outputStream.toByteArray();

            output = new DatagramPacket(sendData, sendData.length, address, 55555);
            sender.send(output);
         }

      } catch (SocketTimeoutException e) {
         System.out.println("\n\nTime-out");
         System.out.println("Ready for Resending Packet\n\n");
         seqNo = tmp;
      }

      serverSocket.close();
      sender.close();
   }

   public static boolean checkAck(Data data) {
      int ackNo = data.getAckNo();
      String ack = data.getAck();
      String ackCheck = "100000110000111001011";

      if (Math.random() > CORRUPTED) {
         if (ackNo == (seqNo + 1) % 2 && ack.equals(ackCheck)) {
            System.out.println("\n\nError-free Ack Arrived");
            return true;
         } else if (ackNo != (seqNo + 1) % 2) {
            System.out.println(
                  "\n\nError-free Ack Not Related to the Only Outstanding Packet Arrived, Discard the Ack\n\n");
            seqNo = tmp;
            return false;
         } else if (!ack.equals(ackCheck)) {
            System.out.println("\n\nCorrupted Ack Arrived, Discard the Ack\n\n");
            seqNo = tmp;
            return false;
         }
      } else {
         System.out.println("\n\nCorrupted Ack Arrived, Discard the Ack\n\n");
         seqNo = tmp;
         return false;
      }
      return true;
   }
}