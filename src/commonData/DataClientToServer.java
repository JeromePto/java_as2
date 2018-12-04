package commonData;

import java.io.Serializable;
import java.util.Calendar;

public class DataClientToServer implements Serializable{
	
	private String name;
	private float temperature;
	private Calendar calendar;

	public DataClientToServer(String name, float temperature) {
		this.calendar = Calendar.getInstance();
		this.name = name;
		this.temperature = temperature;
	}
	
	public String toString()
	{
		return "Client: " + name + "\nDate: " + this.calendar.getTime().toString() + "\nTemperature: " + temperature;
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
}
