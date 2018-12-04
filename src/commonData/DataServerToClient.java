package commonData;

import java.io.Serializable;

public class DataServerToClient implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int sampleRate;
	private String message;

	public DataServerToClient(int samplingRate) {
		this.sampleRate = samplingRate;
		this.message = "CREATED";
	}
	
	public String toString() {
		return "Sample Rate: " + sampleRate + "\nMessage: " + message;
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

}
