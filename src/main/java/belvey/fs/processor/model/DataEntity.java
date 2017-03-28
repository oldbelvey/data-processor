package belvey.fs.processor.model;

import java.text.ParseException;
import java.util.Date;

import belvey.fs.processor.util.DateUtils;

public class DataEntity {
	private String id;
	private String startTime;
	private String endTime;
	private String name;
	private String age;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}


	/**
	 * a(this)小于b时返回值小于0
	 * @param b
	 * @return
	 * @throws ParseException
	 */
	public int compareTo(DataEntity b) throws ParseException {
		Date aStart = DateUtils.getdate(this.getStartTime());
		Date bStart = DateUtils.getdate(b.getStartTime());
		
		return aStart.compareTo(bStart);
	}	
	
	
}
