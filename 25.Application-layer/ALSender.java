import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class ALSender {
   public static void main(String[] args) throws IOException, ClassNotFoundException {
      DatagramSocket serverSocket = new DatagramSocket(55555);

      DatagramSocket sender = new DatagramSocket();
      InetAddress address = InetAddress.getByName("localhost");

      byte[] receivedData = new byte[1024];

      Scanner scanner = new Scanner(System.in);
      System.out.println("--------------------------------------------------");
      System.out.print("Enter Sending Data : ");
      String sendMsg = scanner.nextLine();

      Data data = new Data();
      data.setData(sendMsg);

      System.out.println("\n//// Application Layer Sender");

      System.out.println("\nApplication Layer >>> Transport Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Output Data : " + data.getData());

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(outputStream);
      os.writeObject(data);
      byte[] sendData = outputStream.toByteArray();

      DatagramPacket output = new DatagramPacket(sendData, sendData.length, address, 4444);
      sender.send(output);

      //////////////////////////////////////////////////////////////////////////////
      System.out.println("\n\n///////////////////////////////////////////////////////////");

      DatagramPacket received = new DatagramPacket(receivedData, receivedData.length);
      serverSocket.receive(received);

      byte[] getData = received.getData();
      ByteArrayInputStream in = new ByteArrayInputStream(getData);
      ObjectInputStream is = new ObjectInputStream(in);
      data = (Data) is.readObject();

      System.out.println("\n\nTransport Layer >>> Application Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Received Check Complete\n\n");

      serverSocket.close();
      sender.close();
   }
}
