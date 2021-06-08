import java.io.*;
import java.net.*;
import java.util.*;

public class SrReceiver {

   public static final double PROBABILITY = 0.15;

   public static int M;

   public static void main(String[] args) {

      DatagramSocket socket = null;
      int portNumber = 6666;

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

         sR(socket, initInfo);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private static void sR(DatagramSocket socket, InitInfo initInfo) throws IOException, ClassNotFoundException {

      ArrayList<Packet> received = new ArrayList<>();
      ArrayList<Packet> bufferData = new ArrayList<>();

      boolean check = false;
      int waitingFor = 0;

      byte[] input = new byte[1024];

      while (!check) {

         DatagramPacket dataIn = new DatagramPacket(input, input.length);
         socket.receive(dataIn);

         InetAddress address = dataIn.getAddress();
         int port = dataIn.getPort();

         byte[] data = dataIn.getData();
         ByteArrayInputStream in = new ByteArrayInputStream(data);
         ObjectInputStream is = new ObjectInputStream(in);

         Packet packet = (Packet) is.readObject();

         // To receive exit call
         if (packet.getSeqNum() == -1) {
            break;
         }

         char ch = packet.getData();

         if (packet.getSeqNum() == waitingFor && packet.isLast()) {
            if (waitingFor < initInfo.getNumPackets()) {
               waitingFor++;
            }
            received.add(packet);

            int value = sendData(packet, waitingFor, socket, address, port, false);

            if (value < waitingFor) {
               waitingFor = value;
               int len = received.size();
               System.out.println("\n\n//////// Packet Lost (seqNo : " + (waitingFor) % M + ")");
               received.remove(len - 1);
               check = false;

            } else {
               System.out.println("//////// Last Packet");
               System.out.println("//////// Packet (seqNo : " + packet.getSeqNum() % M + ") delivered to Application");
               // check = true;
            }

         }

         else if (packet.getSeqNum() == waitingFor && bufferData.size() > 0) {
            received.add(packet);
            if (waitingFor < initInfo.getNumPackets()) {
               waitingFor++;
            }

            int value = sendData(packet, waitingFor, socket, address, port, false);

            if (value < waitingFor) {
               waitingFor = value;
               int len = received.size();

               System.out.println("\n\n//////// Packet Lost (seqNo : " + (waitingFor) % M + ")");

               received.remove(len - 1);

            } else {
               ArrayList<Packet> tmp = new ArrayList<>();

               System.out.println("//////// Packet (seqNo : " + packet.getSeqNum() % M + ") delivered to Application");

               tmp.addAll(bufferData);

               int count = 0;

               for (int i = 0; i < tmp.size(); i++) {
                  if (!(waitingFor == tmp.get(i).getSeqNum())) {
                     break;
                  } else {
                     if (waitingFor < initInfo.getNumPackets()) {
                        waitingFor++;
                     }
                     count++;

                     System.out.println("//////// Packet (seqNo : " + bufferData.get(i).getSeqNum() % M
                           + ") delivered to Application");
                  }
               }

               bufferData = new ArrayList<>();

               for (int j = 0; j < tmp.size(); j++) {
                  if (j < count) {
                     continue;
                  }
                  bufferData.add(tmp.get(j));
               }

               // if (waitingFor == initInfo.getNumPackets()) {
               // check = true;
               // }
            }
         }

         else if (packet.getSeqNum() == waitingFor && bufferData.size() == 0) {
            received.add(packet);
            if (waitingFor < initInfo.getNumPackets()) {
               waitingFor++;
            }

            int value = sendData(packet, waitingFor, socket, address, port, false);

            if (value < waitingFor) {
               waitingFor = value;
               int len = received.size();

               System.out.println("\n\n//////// Packet Lost (seqNo : " + (waitingFor) % M + ")");

               received.remove(len - 1);
            } else {
               System.out.println("//////// Packet (seqNo : " + packet.getSeqNum() % M + ") delivered to Application");
            }
         }

         else if (packet.getSeqNum() > waitingFor) {

            sendData(packet, waitingFor, socket, address, port, true);
            System.out.println("//////// Packet Stored in Buffer (seqNo : " + packet.getSeqNum() % M + ")");
            bufferData.add(packet);

            Collections.sort(bufferData);

         }

         else if (packet.getSeqNum() < waitingFor) {

            sendData(packet, waitingFor, socket, address, port, true);
            System.out.println("//////// Packet Already Delivered, Sending Ack");
         }

         else {
            System.out.println("//////// Discard Packet (seqNo : " + packet.getSeqNum() % M + ")");
            packet.setSeqNum(-9999);
         }
      }
   }

   public static int sendData(Packet packet, int waitingFor, DatagramSocket socket, InetAddress address, int port,
         boolean b) throws IOException {

      AckData ackData = new AckData();
      ackData.setAckNo(packet.getSeqNum() + 1);

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(outputStream);
      os.writeObject(ackData);

      byte[] replyData = outputStream.toByteArray();
      DatagramPacket sendPacket = new DatagramPacket(replyData, replyData.length, address, port);

      if ((Math.random() > PROBABILITY | b) && packet.getSeqNum() != -9999) {
         System.out.println("\n\nPacket Receiving... (seqNo : " + (ackData.getAckNo() - 1) % M + ")");

         String msg = "//////////////////// Sending Ack for Packet : " + (ackData.getAckNo() - 1) % M + " (ackNo : "
               + (ackData.getAckNo() - 1) % M + ")";
         System.out.println(msg);
         socket.send(sendPacket);

      } else if (packet.getSeqNum() != -9999 && !b) {
         waitingFor--;
      }

      return waitingFor;
   }
}
