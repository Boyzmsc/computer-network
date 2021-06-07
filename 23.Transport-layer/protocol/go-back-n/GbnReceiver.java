import java.io.*;
import java.net.*;
import java.util.*;

public class GbnReceiver {

   public static final double PROBABILITY = 0.15;

   public static int M;

   public static void main(String[] args) {

      DatagramSocket socket = null;
      int portNumber = 5555;

      byte[] received = new byte[1024];

      try {
         socket = new DatagramSocket(portNumber);
         System.out.println("///// Receiver is Ready\n");

         DatagramPacket initPacket = new DatagramPacket(received, received.length);
         socket.receive(initPacket);

         byte[] initData = initPacket.getData();
         ByteArrayInputStream inInit = new ByteArrayInputStream(initData);
         ObjectInputStream isInit = new ObjectInputStream(inInit);
         InitInfo initInfo = (InitInfo) isInit.readObject();

         System.out.println("\n///////////// Received Initial Data");

         M = (int) Math.pow(2, initInfo.getSeqNumBits());

         InetAddress address = initPacket.getAddress();
         int port = initPacket.getPort();

         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         ObjectOutputStream os = new ObjectOutputStream(outputStream);
         os.writeObject(initInfo);

         byte[] ack = outputStream.toByteArray();
         DatagramPacket output = new DatagramPacket(ack, ack.length, address, port);
         socket.send(output);

         goBackN(socket, initInfo);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private static void goBackN(DatagramSocket socket, InitInfo initInfo) throws IOException, ClassNotFoundException {

      ArrayList<Packet> received = new ArrayList<>();

      boolean check = false;
      int waitingFor = 0;

      byte[] input = new byte[1024];

      while (!check) {

         DatagramPacket dataIn = new DatagramPacket(input, input.length);
         socket.receive(dataIn);

         byte[] data = dataIn.getData();
         ByteArrayInputStream in = new ByteArrayInputStream(data);
         ObjectInputStream is = new ObjectInputStream(in);

         Packet packet = (Packet) is.readObject();

         // To receive exit call
         if (packet.getSeqNum() == -1) {
            break;
         }

         System.out.println("\n\nPacket Receiving... (seqNo : " + packet.getSeqNum() % M + ")");

         char ch = packet.getData();

         if (packet.getSeqNum() == waitingFor && packet.isLast()) {
            if (waitingFor < initInfo.getNumPackets()) {
               waitingFor++;
            }
            received.add(packet);
            System.out.println("////////// Last Packet");

         } else if (packet.getSeqNum() == waitingFor) {
            waitingFor++;
            received.add(packet);
         } else {
            System.out.println("//////////////////// Discard Packet (Not in Order)");
            packet.setSeqNum(-9999);
         }

         InetAddress address = dataIn.getAddress();
         int port = dataIn.getPort();

         AckData ackData = new AckData();
         ackData.setAckNo(waitingFor);

         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         ObjectOutputStream os = new ObjectOutputStream(outputStream);
         os.writeObject(ackData);

         byte[] ack = outputStream.toByteArray();
         DatagramPacket output = new DatagramPacket(ack, ack.length, address, port);

         if (Math.random() > PROBABILITY && packet.getSeqNum() != -9999) {
            String msg = "//////////////////// Sending Ack (ackNo : " + ackData.getAckNo() % M + ")";
            System.out.println(msg);
            socket.send(output);
         } else if (packet.getSeqNum() != -9999) {
            int length = received.size();
            System.out.println("//////////////////// Packet Lost (seqNo : " + packet.getSeqNum() % M + ")");
            received.remove(length - 1);
            waitingFor--;

            if (check) {
               check = false;
            }
         } else if (packet.getSeqNum() == -9999) {
            String msg = "//////////////////// Sending Ack (ackNo : " + ackData.getAckNo() % M + ")";
            System.out.println(msg);
            socket.send(output);
         }
      }
   }
}
