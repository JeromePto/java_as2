package commonData;

import java.io.Serializable;

public class DataServerToClient implements Serializable{
	
	private static final long serialVersionUID = 2L;
	private int sampleRate;
	private String message;
	private long shift;

	public DataServerToClient(int samplingRate, long shift) {
		this.sampleRate = samplingRate;
		this.shift = shift;
		this.message = "CREATED";
	}
	
	public String toString() {
		return "Sample Rate: " + sampleRate + " Shift: " + shift + " Message: " + message;
	}
	
	public long getShift() {
		return shift;
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
