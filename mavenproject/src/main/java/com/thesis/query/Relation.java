package com.thesis.query;

import java.util.List;

public class Relation {
	String name;
	List<String> domains;
	List<String> ranges;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getDomains() {
		return domains;
	}
	public void setDomains(List<String> domains) {
		this.domains = domains;
	}
	public List<String> getRanges() {
		return ranges;
	}
	public void setRanges(List<String> ranges) {
		this.ranges = ranges;
	}
	
	
	

}

