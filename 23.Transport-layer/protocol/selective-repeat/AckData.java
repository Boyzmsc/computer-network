import java.io.*;

public class AckData implements Serializable {

	int ackNo;

	public int getAckNo() {
		return ackNo;
	}

	public void setAckNo(int ackNo) {
		this.ackNo = ackNo;
	}

	@Override
	public String toString() {
		return "Ack (ackNo : " + ackNo + ")";
	}
}
