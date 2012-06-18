package de.workacclaimer.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Model of a job which is stored in the database
 * @author Jan Barocka
 *
 */
public class Job {
	private long id;
	private String title;
	private String description;
	private long pausetime;
	private long starttime;
	private long stoptime;
	private int jgid;
	
	public int getJgid() {
		return jgid;
	}
	public void setJgid(int jgid) {
		this.jgid = jgid;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getPausetime() {
		return pausetime;
	}
	
	public String getPausetimeString(){
		long t = pausetime;
		DecimalFormat df = new DecimalFormat("00");
		
		//Sekunden
		t = t/1000;
		//Minuten
		t = t/60;
		String time = ":"+df.format(t);
		//Stunden
		t = t/60;
		time = df.format(t)+time;
		return time;
	}
	
	public void setPausetime(long pausetime) {
		this.pausetime = pausetime;
	}
	public long getStarttime() {
		return starttime;
	}
	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}
	public long getStoptime() {
		return stoptime;
	}
	public void setStoptime(long stoptime) {
		this.stoptime = stoptime;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStartDate(){
		Date d= new Date();
		d.setTime(starttime);
		return d.toLocaleString();
	}
	
	public String getTimeString(){
		long t = stoptime - starttime;
		DecimalFormat df = new DecimalFormat("00");
		
		//Sekunden
		t = t/1000;
		//Minuten
		t = t/60;
		String time = ":"+df.format(t);
		//Stunden
		t = t/60;
		time = df.format(t)+time;
		return time;
	}
	
	@Override
	public String toString() {
		return title;
	}

	
}
