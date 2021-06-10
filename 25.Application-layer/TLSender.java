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

      System.out.println("\n//// Transport Layer Sender");

      System.out.println("\nApplication Layer >>> Transport Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Input Data : " + data.getData());

      seqNo = (seqNo + 1) % 2;
      data.setSeqNo(seqNo);

      System.out.println("\n\nTransport Layer >>> Network Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Output Data : " + data.getData());
      System.out.println("Output SeqNo : " + data.getSeqNo());

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

      System.out.println("\n\nNetwork Layer >>> Transport Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Input Data : " + data.getData());
      System.out.println("Input AckNo : " + data.getAckNo());
      System.out.println("Input Ack : " + data.getAck());

      // check ACK
      System.out.println("\n\nReceived Ack Complete");

      System.out.println("\n\nTransport Layer >>> Application Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Output Data : " + data.getData() + "\n");

      outputStream = new ByteArrayOutputStream();
      os = new ObjectOutputStream(outputStream);
      os.writeObject(data);
      sendData = outputStream.toByteArray();

      output = new DatagramPacket(sendData, sendData.length, address, 55555);
      sender.send(output);

      serverSocket.close();
      sender.close();
   }
}