import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class NLSender {

   public static void main(String[] args) throws IOException, ClassNotFoundException {
      DatagramSocket serverSocket = new DatagramSocket(3333);

      DatagramSocket sender = new DatagramSocket();
      InetAddress address = InetAddress.getByName("localhost");

      byte[] receivedData = new byte[1024];

      DatagramPacket received = new DatagramPacket(receivedData, receivedData.length);
      serverSocket.receive(received);

      byte[] getData = received.getData();
      ByteArrayInputStream in = new ByteArrayInputStream(getData);
      ObjectInputStream is = new ObjectInputStream(in);
      Data data = (Data) is.readObject();

      System.out.println("\n//// Network Layer Sender");

      System.out.println("\nTransport Layer >>> Network Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Input Data : " + data.getData());

      // By-pass

      System.out.println("\n\nNetwork Layer >>> Datalink Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Output Data : " + data.getData());

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(outputStream);
      os.writeObject(data);
      byte[] sendData = outputStream.toByteArray();

      DatagramPacket output = new DatagramPacket(sendData, sendData.length, address, 2222);
      sender.send(output);

      ///////////////////////////////////////////////////////////////////////////

      received = new DatagramPacket(receivedData, receivedData.length);
      serverSocket.receive(received);

      getData = received.getData();
      in = new ByteArrayInputStream(getData);
      is = new ObjectInputStream(in);
      data = (Data) is.readObject();

      System.out.println("\n\nDatalink Layer >>> Network Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Input Data : " + data.getData());

      // By-pass

      System.out.println("\n\nNetwork Layer >>> Transport Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Output Data : " + data.getData() + "\n");

      outputStream = new ByteArrayOutputStream();
      os = new ObjectOutputStream(outputStream);
      os.writeObject(data);
      sendData = outputStream.toByteArray();

      output = new DatagramPacket(sendData, sendData.length, address, 4444);
      sender.send(output);

      serverSocket.close();
      sender.close();
   }
}