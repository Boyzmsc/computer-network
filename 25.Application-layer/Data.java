import java.io.*;

public class Data implements Serializable {
   String ack;
   String data;
   int ackNo;
   int seqNo;

   public String getAck() {
      return ack;
   }

   public void setAck(String a) {
      this.ack = a;
   }

   public int getSeqNo() {
      return seqNo;
   }

   public void setSeqNo(int n) {
      this.seqNo = n;
   }

   public int getAckNo() {
      return ackNo;
   }

   public void setAckNo(int n) {
      this.ackNo = n;
   }

   public String getData() {
      return data;
   }

   public void setData(String d) {
      this.data = d;
   }
}
