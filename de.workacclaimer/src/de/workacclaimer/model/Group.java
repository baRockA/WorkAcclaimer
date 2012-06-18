package de.workacclaimer.model;

public class Group {
	private long id;
	private String gtitle;
	private String gdescription;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return gtitle;
	}
	public void setTitle(String gtitle) {
		this.gtitle = gtitle;
	}
	public String getDescription() {
		return gdescription;
	}
	public void setDescription(String gdescription) {
		this.gdescription = gdescription;
	}
	@Override
	public String toString() {
		return gtitle;
	}
	
}
