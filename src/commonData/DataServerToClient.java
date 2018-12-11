package commonData;

import java.io.Serializable;

public class DataServerToClient implements Serializable{
	
	private static final long serialVersionUID = 2L;
	private int sampleRate;
	private String message;
	private long shift;
	private long timeSync;

	public DataServerToClient(int samplingRate, long shift, long timeSync) {
		this.sampleRate = samplingRate;
		this.shift = shift;
		this.message = "CREATED";
		this.timeSync = timeSync;
	}
	
	public String toString() {
		return "Sample Rate: " + sampleRate + " Shift: " + shift + " Message: " + message;
	}
	
	public long getShift() {
		return shift;
	}
	
	public long getTimeSync() {
		return timeSync;
	}
	
	public int getSampleRate() {
		return sampleRate;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setShift(long shift) {
		this.shift = shift;
	}
	

}
