package commonData;

import java.io.Serializable;
import java.util.Calendar;

public class DataClientToServer implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private float temperature;
	private Calendar calendar;
	private long time;

	public DataClientToServer(String name, float temperature, long time) {
		this.calendar = Calendar.getInstance();
		this.name = name;
		this.temperature = temperature;
		this.time = time;
	}
	
	public String toString()
	{
		return "Client: " + name + " Date: " + this.calendar.getTime().toString() + " Time: " + time + " Temperature: " + temperature;
	}
	
	public long getTime() {
		return time;
	}
	
	public String getName() {
		return name;
	}
	
	public float getTemperature() {
		return temperature;
	}
	
	public Calendar getCalendar() {
		return calendar;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
}
