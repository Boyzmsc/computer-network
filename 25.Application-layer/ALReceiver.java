import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class ALReceiver {

   public static void main(String[] args) throws IOException, ClassNotFoundException {
      DatagramSocket serverSocket = new DatagramSocket(9999);

      byte[] receivedData = new byte[1024];

      DatagramPacket received = new DatagramPacket(receivedData, receivedData.length);
      serverSocket.receive(received);

      byte[] getData = received.getData();
      ByteArrayInputStream in = new ByteArrayInputStream(getData);
      ObjectInputStream is = new ObjectInputStream(in);
      Data data = (Data) is.readObject();

      System.out.println("\n//// Application Layer Receiver");

      System.out.println("\nTransport Layer >>> Application Layer");
      System.out.println("--------------------------------------------------");
      System.out.println("Input Data : " + data.getData());

      System.out.println("\n\nReceived Complete\n\n");

      serverSocket.close();
   }
}