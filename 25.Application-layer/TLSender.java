import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class TLSender {

   static int seqNo = 1;

   public static void main(String[] args) throws IOException, ClassNotFoundException {
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

      int L5Port = received.getPort();

      System.out.println("\n//// Transport Layer Sender");
      System.out.println("--------------------------------------------------");
      System.out.println("Received From Application Layer Sender");
      System.out.println("Input Data : " + data.getData());

      seqNo = (seqNo + 1) % 2;
      data.setAckNo(seqNo);

      System.out.println("\n--------------------------------------------------");
      System.out.println("Send To Network Layer Sender");
      System.out.println("Output Data : " + data.getData());

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(outputStream);
      os.writeObject(data);
      byte[] sendData = outputStream.toByteArray();

      DatagramPacket output = new DatagramPacket(sendData, sendData.length, address, 3333);
      sender.send(output);

      ///////////////////////////////////////////////////////////////////////////

      received = new DatagramPacket(receivedData, receivedData.length);
      serverSocket.receive(received);

      getData = received.getData();
      in = new ByteArrayInputStream(getData);
      is = new ObjectInputStream(in);
      data = (Data) is.readObject();

      System.out.println("\n--------------------------------------------------");
      System.out.println("Received From Network Layer Sender");
      System.out.println("Input Data : " + data.getData());

      // check ACK

      System.out.println("\n--------------------------------------------------");
      System.out.println("Send To Application Layer Sender");
      System.out.println("Output Data : " + data.getData());

      outputStream = new ByteArrayOutputStream();
      os = new ObjectOutputStream(outputStream);
      os.writeObject(data);
      sendData = outputStream.toByteArray();

      output = new DatagramPacket(sendData, sendData.length, address, L5Port);
      sender.send(output);
   }
}