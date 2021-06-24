# Function Block Description

## How To Run

1. Run 10 Terminals (or PowerShells)

2. Execution Order
   * java ALReceiver
   * java TLReceiver
   * java NLReceiver
   * java DLReceiver
   * java PLReceiver
   * java PLSender
   * java DLSender
   * java NLSender
   * java TLSender
   * java ALSender
3. Input Binary Data Randomly



## Code Blocks

### Data.java

```java
// Data Structure and Using for Processing Ack
public class Data implements Serializable {
    // Get Ack (Ascii Binary)
    public String getAck() {
    	// ...
    }
    
    // Set Ack (Ascii Binary)
    public void setAck(String a) {
    	// ...
    }
    
    // Get SeqNo
    public int getSeqNo() {
    	// ...
    }
    
    // Set SeqNo
    public void setSeqNo(int n) {
    	// ...
    }
    
    // Get AckNo
    public int getAckNo() {
    	// ...
    }
    
    // Set AckNo
    public void setAckNo(int n) {
    	// ...
    }
    
    // Get Data
    public String getData() {
    	// ...
    }
    
    // Set Data
    public void setData(String d) {
    	// ...
    }
}
```



### ALSender.java

```java
// Application Layer Sender
public class ALSender {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Enter Sending Data (Input)
      
        // Application Layer >>> Transport Layer
      
        //////////////////////////////////////////////////////
      
        // Transport Layer >>> Application Layer
      
        // Check Received Data
    }
}
```



### TLSender.java

```java
// Transport Layer Sender
public class TLSender {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Application Layer >>> Transport Layer
      
        // Set seqNo to Data
      
        // Transport Layer >>> Network Layer
      
        //////////////////////////////////////////////////////
      
        // Network Layer >>> Transport Layer
      
        // Check Ack
      
        // Transport Layer >>> Application Layer
    }
    
    // Check Ack
    public static boolean checkAck(Data data) {
    	// ...
    }
}
```



### NLSender.java

```java
// Network Layer Sender
public class NLSender {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Transport Layer >>> Network Layer

        // By-pass

        // Network Layer >>> Datalink Layer

        //////////////////////////////////////////////////////

        // Datalink Layer >>> Network Layer

        // By-pass

        // Network Layer >>> Transport Layer
    }
}
```



### DLSender.java

```java
// Datalink Layer Sender
public class DLSender {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Network Layer >>> Datalink Layer
      
        // Bit-Stuffing (Simple Protocol)
      
        // CSMA-CD
      
        // Datalink Layer >>> Physical Layer
      
        //////////////////////////////////////////////////////
      
        // Physical Layer >>> Datalink Layer
      
        // Bit-Unstuffing (Simple Protocol)
      
        // Datalink Layer >>> Network Layer
    }
    
    // Bit-Stuffing
    public static String stuff(String inputData) {
    	// ...
    }
    
    // Bit-Unstuffing
    public static String unStuff(String inputData) {
    	// ...
    }
    
    // CSMACD
    public static void csmacd() {
    	// ...
    }
    
    // CSMA : non-Persistent
    // True : Channel busy , False : Channel idle
    public static void csma(int seconds) {
    	// ...
    }
    
    // Check transmission done
    // True : Transmission done , False : Transmission not done
    public static boolean isTransDone() {
    	// ...
    }
    
    // Check collision detected
    // True : Collision detected , False : Collision not detected
    public static boolean isColliDetected() {
    	// ...
    }
    
    // For time wait
    public static void wait(int seconds) {
    	// ...
    }
}
```



### PLSender.java

```java
// Physical Layer Sender
public class PLSender {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Datalink Layer >>> Physical Layer

        // MLT-3

        // Physical Layer >>> Physical Layer

        //////////////////////////////////////////////////////

        // Physical Layer >>> Physical Layer

        // Un MLT-3

        // Physical Layer >>> Datalink Layer
    }
    
    // MLT-3
    public static String mlt(String inputData) {
    	// ...
    }
    
    // Un MLT-3
    public static String unMlt(String physicalData) {
    	// ...
    }
}
```



### PLReceiver.java

```java
// Physical Layer Receiver
public class PLReceiver {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Physical Layer >>> Physical Layer

        // Un MLT-3

        // Physical Layer >>> Datalink Layer

        //////////////////////////////////////////////////////

        // Datalink Layer >>> Physical Layer

        // MLT-3

        // Physical Layer >>> Physical Layer
    }
    
    // MLT-3
    public static String mlt(String inputData) {
    	// ...
    }
    
    // Un MLT-3
    public static String unMlt(String physicalData) {
    	// ...
    }
}
```



### DLReceiver.java

```java
// Physical Layer Receiver
public class DLReceiver {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Physical Layer >>> Datalink Layer
      
        // Bit-Unstuffing (Simple Protocol)
      
        // Datalink Layer >>> Network Layer
      
        //////////////////////////////////////////////////////
      
        // Network Layer >>> Datalink Layer
      
        // Bit-Stuffing (Simple Protocol)
      
        // CSMA-CD
      
        // Datalink Layer >>> Physical Layer
    }
    
    // Bit-Stuffing
    public static String stuff(String inputData) {
    	// ...
    }
    
    // Bit-Unstuffing
    public static String unStuff(String inputData) {
    	// ...
    }
    
    // CSMACD
    public static void csmacd() {
    	// ...
    }
    
    // CSMA : non-Persistent
    // True : Channel busy , False : Channel idle
    public static void csma(int seconds) {
    	// ...
    }
    
    // Check transmission done
    // True : Transmission done , False : Transmission not done
    public static boolean isTransDone() {
    	// ...
    }
    
    // Check collision detected
    // True : Collision detected , False : Collision not detected
    public static boolean isColliDetected() {
    	// ...
    }
    
    // For time wait
    public static void wait(int seconds) {
    	// ...
    }
}
```



### NLReceiver.java

```java
// Network Layer Receiver
public class NLReceiver {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Datalink Layer >>> Network Layer

        // By-pass

        // Network Layer >>> Transport Layer

        //////////////////////////////////////////////////////

        // Transport Layer >>> Network Layer

        // By-pass

        // Network Layer >>> Datalink Layer
    }
}
```



### TLReceiver.java

```java
// Transport Layer Receiver
public class TLReceiver {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Network Layer >>> Transport Layer

        // Check Packet

        // Transport Layer >>> Application Layer

        //////////////////////////////////////////////////////

        // Create Ack (Ideal Case)
        // Convert to ASCII Code
        // Set ASCII to Binary

        // Transport Layer >>> Network Layer
    }
    
    // Check Packet
    public static boolean checkPacket(Data data) {
    	// ...
    }
}
```



### ALReceiver.java

```java
// Application Layer Receiver
public class ALReceiver {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Transport Layer >>> Application Layer

        // Check Received Data
    }
}
```

