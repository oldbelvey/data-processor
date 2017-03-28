package belvey.fs.processor.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.hadoop.io.WritableComparable;

import belvey.fs.processor.util.DateUtils;

public class DataEntity implements WritableComparable<DataEntity>{
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



	@Override
	public String toString() {
		return id+" "+startTime+" "+endTime+" "+name+" "+age;
	}
	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int compareTo(DataEntity o) {

		Date aStart;
		Date bStart;
		try {
			aStart = DateUtils.getdate(this.getStartTime());
			bStart = DateUtils.getdate(o.getStartTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return aStart.compareTo(bStart);
	}	
	
	
	
}
