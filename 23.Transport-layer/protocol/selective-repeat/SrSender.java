import java.io.*;
import java.net.*;
import java.util.*;

public class SrSender {

   public static int TIMER = 3000;

   public static final double PROBABILITY = 0.15;

   public static final double CORRUPTED = 0.15;

   public static final int PORT = 6666;

   public static void main(String[] args) {

      Scanner sc = new Scanner(System.in);

      System.out.println("///////// Enter Info");
      System.out.println("///////// Input Case : 6 , 3 , 4 , 2000 , 2");

      System.out.print("\n//// Enter Number of Packets : ");
      int numPackets = sc.nextInt();

      System.out.print("//// Enter Number of Sequence bits : ");
      int seqNumBits = sc.nextInt();

      System.out.print("//// Enter Size of Window : ");
      int windowSize = sc.nextInt();

      System.out.print("//// Enter Size of Time out : ");
      TIMER = sc.nextInt();

      System.out.print("//// Enter Size of Segment : ");
      int sizeSegment = sc.nextInt();

      System.out.println("\n\n//////////////////////////////////////////////////////////////////////////////////");
      System.out.println("///// Number of Seq bits : " + seqNumBits + ", Window Size : " + windowSize + ", Timeout : "
            + TIMER + ", Segment Size : " + sizeSegment);
      System.out.println("//////////////////////////////////////////////////////////////////////////////////\n");

      try {
         sendData(PORT, numPackets, seqNumBits, windowSize, sizeSegment);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private static void sendData(int portNumber, int numPackets, int seqNumBits, int windowSize, long sizeSegment)
         throws IOException, ClassNotFoundException, InterruptedException {

      ArrayList<Packet> sent = new ArrayList<>();

      int lastSent = 0;

      int waitingForAck = 0;

      String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      int N = alphabet.length();
      int M = (int) Math.pow(2, seqNumBits);

      byte[] dataIn = new byte[1024];
      InitInfo initInfo = new InitInfo();
      initInfo.setNumPackets(numPackets);
      initInfo.setPacketSize(sizeSegment);
      initInfo.setWindowSize(windowSize);
      initInfo.setSeqNumBits(seqNumBits);

      HashSet<Integer> notOrdered = new HashSet<>();

      DatagramSocket Socket = new DatagramSocket();
      InetAddress IPAddress = InetAddress.getByName("localhost");

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(outputStream);
      os.writeObject(initInfo);
      byte[] initData = outputStream.toByteArray();

      DatagramPacket initPacket = new DatagramPacket(initData, initData.length, IPAddress, portNumber);
      System.out.println("\n///////////// Sending Initial Data\n");
      Socket.send(initPacket);

      DatagramPacket initAck = new DatagramPacket(dataIn, dataIn.length);
      Socket.receive(initAck);

      byte[] dataCon = initAck.getData();
      ByteArrayInputStream inReturn = new ByteArrayInputStream(dataCon);
      ObjectInputStream isReturn = new ObjectInputStream(inReturn);
      InitInfo initAckData = (InitInfo) isReturn.readObject();

      while (true) {

         while (lastSent - waitingForAck < windowSize && lastSent < numPackets) {

            if (lastSent - waitingForAck == 0) {
               System.out.println("\n-----------------------------------");
               System.out.println("Timer Start for Packet : " + lastSent % M);
            } else {
               System.out.println("\n-----------------------------------");
               System.out.println("Timer Already Running for Packet : " + waitingForAck % M);
            }

            Random r = new Random();
            char ch = alphabet.charAt(r.nextInt(N));

            Packet packet = new Packet();
            packet.setData(ch);
            packet.setSeqNum(lastSent);

            if (lastSent == numPackets - 1) {
               packet.setLast(true);
            }

            if (Math.random() <= CORRUPTED) {
               packet.setData(alphabet.charAt(r.nextInt(N)));
            }

            outputStream = new ByteArrayOutputStream();
            os = new ObjectOutputStream(outputStream);
            os.writeObject(packet);

            byte[] data = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, portNumber);

            System.out.println("//////////////// Sending Packet (seqNo : " + packet.getSeqNum() % M + ")\n");
            sent.add(packet);
            Socket.send(sendPacket);

            lastSent++;
            Thread.sleep(3000);
         }

         DatagramPacket received = new DatagramPacket(dataIn, dataIn.length);

         try {
            Socket.setSoTimeout(TIMER);
            Socket.receive(received);

            byte[] data = received.getData();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            AckData ackData = (AckData) is.readObject();

            if (Math.random() > PROBABILITY) {
               System.out.println("\n-----------------------------------");
               System.out.println("//////// Received Ack for Packet : " + (ackData.getAckNo() - 1) % M + " (ackNo : "
                     + (ackData.getAckNo() - 1) % M + ")");

               if ((ackData.getAckNo() - waitingForAck) == 1) {
                  waitingForAck = waitingForAck + 1;
                  System.out.println("//////// Slide the Window");

                  if (notOrdered.size() > 0) {
                     for (int i = waitingForAck; i <= lastSent; i++) {

                        if (notOrdered.contains(i)) {
                           notOrdered.remove(i);
                           waitingForAck++;
                        } else {
                           break;
                        }
                     }
                  }

                  if (waitingForAck == lastSent) {
                     System.out.println("Timer Stop");
                  }

               } else {
                  System.out.println("Timer Already Running for Packet : " + waitingForAck % M);
                  notOrdered.add((ackData.getAckNo() - 1));
               }

            } else {
               System.out.println("\n-----------------------------------");
               System.out.println("//////// Lost Ack for Packet : " + (ackData.getAckNo() - 1) % M + " (ackNo : "
                     + (ackData.getAckNo() - 1) % M + ")");
            }

            if (waitingForAck == numPackets && notOrdered.size() == 0) {
               // To send exit call to finish program
               Packet packet = new Packet();
               packet.setSeqNum(-1);

               outputStream = new ByteArrayOutputStream();
               os = new ObjectOutputStream(outputStream);
               os.writeObject(packet);

               byte[] lastData = outputStream.toByteArray();
               DatagramPacket sendPacket = new DatagramPacket(lastData, lastData.length, IPAddress, PORT);

               Socket.send(sendPacket);
               break;

            } else {
               System.out.println("Timer Restart for Packet : " + waitingForAck % M + "\n");
            }

         } catch (SocketTimeoutException e) {
            System.out.println("\n-----------------------------------");
            System.out.println("Time Out (Packet : " + waitingForAck % M + ")");

            for (int i = waitingForAck; i < lastSent; i++) {

               Packet packet = sent.get(i);

               if (!(notOrdered.contains(packet.getSeqNum()))) {

                  char ch = packet.getData();

                  if (Math.random() <= CORRUPTED) {
                     Random r = new Random();
                     packet.setData(alphabet.charAt(r.nextInt(N)));
                  }

                  outputStream = new ByteArrayOutputStream();
                  os = new ObjectOutputStream(outputStream);
                  os.writeObject(packet);

                  byte[] data = outputStream.toByteArray();
                  DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, portNumber);

                  System.out.println("//////////////// Resending Packet (seqNo : " + packet.getSeqNum() % M + ")\n");

                  Socket.send(sendPacket);
                  Thread.sleep(3000);
               }
            }
         }
      }
   }
}
