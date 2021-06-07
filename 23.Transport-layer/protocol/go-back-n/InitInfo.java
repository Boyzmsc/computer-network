import java.io.Serializable;

public class InitInfo implements Serializable {

   private int seqNumBits;

	private int windowSize;

	private long packetSize;

	private int numPackets;

   public int getSeqNumBits() {
		return seqNumBits;
	}

	public void setSeqNumBits(int seqNumBits) {
		this.seqNumBits = seqNumBits;
	}

	public int getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	public long getPacketSize() {
		return packetSize;
	}

	public void setPacketSize(long packetSize) {
		this.packetSize = packetSize;
	}

	public int getNumPackets() {
		return numPackets;
	}

	public void setNumPackets(int numPackets) {
		this.numPackets = numPackets;
	}

	@Override
	public String toString() {
		return "(windowSize : " + windowSize + ", packetSize : " + packetSize+ ", numPackets : " + numPackets + ", seqNumBits : " + seqNumBits + ")";
	}

}
