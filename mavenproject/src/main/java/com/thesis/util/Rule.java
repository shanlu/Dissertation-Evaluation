package com.thesis.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Rule {
	String name;
	List<Triple> head = new ArrayList<Triple>();
	List<Triple> body = new ArrayList<Triple>();
	Set<String> variables = new HashSet<String>();
	
	
	public List<Triple> getHead() {
		return head;
	}
	public void setHead(List<Triple> head) {
		this.head = head;
	}
	public List<Triple> getBody() {
		return body;
	}
	public void setBody(List<Triple> body) {
		this.body = body;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<String> getVariables() {
		return variables;
	}
	public void setVariables(Set<String> variables) {
		this.variables = variables;
	}
	
	
	
}