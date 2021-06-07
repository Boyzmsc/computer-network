import java.io.*;

public class Packet implements Comparable<Packet>, Serializable {

	private int seqNum;
	private char data;
	boolean last = false;

   @Override
	public int compareTo(Packet o) {
		
		return this.getSeqNum()-(o.getSeqNum());
	}
	
	@Override
	public String toString() {
		return "Packet (seqNum : " + seqNum + ", data : " + data + ")";
	}

	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public char getData() {
		return data;
	}

	public void setData(char data) {
		this.data = data;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}
}
