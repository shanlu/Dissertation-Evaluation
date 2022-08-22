package com.thesis.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

public class ScanOWLRules {
	static final String inputFileName  = "/Users/shanlu/Documents/Data/NewSTIX/owlRules.csv";
	
	
	public static List<Rule> readOWLRFile(String filename) throws IOException, CsvValidationException {
		List<Rule> ruleList = new ArrayList<Rule>();
		
		try (CSVReader reader = new CSVReader(new FileReader(filename))) {
			
			String[] lineInArray;
		      while ((lineInArray = reader.readNext()) != null) {
		    	  Rule rule = new Rule();
		    	  String name = lineInArray[0];
		    	  String bodyString  = lineInArray[1];
		    	  String headString = lineInArray[2];
		    	  if (bodyString.contains("...") || headString.contains("..."))
		    		  continue;
		    	  
		    	  rule.setName(name);
		    	  Set<String> variables = new HashSet<String>();
		    	  
		    	  List<Triple> body = new ArrayList<Triple>();
		    	  String[] bodyTriples = bodyString.split("\n");
		    	  for (String b : bodyTriples) {
		    		  b = b.substring(2, b.length()-1);
		    		  Triple t = new Triple();
		    		  String[] elements = b.split(", ");
		    		  if (elements[0].contains("?")) {
		    			  variables.add(elements[0]);
		    		  }
		    		  t.setSubject(elements[0]);
		    		  if (elements[1].contains("?")) {
		    			  variables.add(elements[1]);
		    		  }
		    		  t.setPredicate(elements[1]);
		    		  if (elements[2].contains("?")) {
		    			  variables.add(elements[2]);
		    		  }
		    		  t.setObject(elements[2]);
		    		  
		    		  body.add(t);
		    	  }
		    	  
		    	  List<Triple> head = new ArrayList<Triple>();
		    	  String[] headTriples = headString.split("\n");
		    	  for (String h : headTriples) {
		    		  h = h.substring(2, h.length()-1);
		    		  Triple t = new Triple();
		    		  String[] elements = h.split(", ");
		    		  if (elements[0].contains("?")) {
		    			  variables.add(elements[0]);
		    		  }
		    		  t.setSubject(elements[0]);
		    		  if (elements[1].contains("?")) {
		    			  variables.add(elements[1]);
		    		  }
		    		  t.setPredicate(elements[1]);
		    		  if (elements[2].contains("?")) {
		    			  variables.add(elements[2]);
		    		  }
		    		  t.setObject(elements[2]);
		    		  
		    		  head.add(t);
		    	  }
		    	  
		    	  rule.setBody(body);
		    	  rule.setHead(head);
		    	  rule.setVariables(variables);
		    	  
		    	  ruleList.add(rule);
		      }
		  }
		
		return ruleList;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, CsvException {
		
//		try (CSVReader reader = new CSVReader(new FileReader(inputFileName))) {
//			
//			String[] lineInArray;
//		      while ((lineInArray = reader.readNext()) != null) {
//		          System.out.println(lineInArray[0]);
//		          System.out.println(lineInArray[1]);
//		          String[] bodyTriples = lineInArray[1].split("\n");
//		          for (String b : bodyTriples) {
//		        	  System.out.println(b);
//		        	  System.out.println("***");
//		          }
//		          System.out.println("---------");
//		          System.out.println(lineInArray[2]);
//		          String[] headTriples = lineInArray[2].split("\n");
//		          for (String h : headTriples) {
//		        	  System.out.println(h);
//		        	  System.out.println("***");
//		          }
//		          System.out.println("---------");
//		          System.out.println("===========");
//		      }
//		  }
		
		try {
			List<Rule> ruleList = readOWLRFile(inputFileName);
			
			for (Rule r : ruleList) {
				List<Triple> head = r.getHead();
				List<Triple> body = r.getBody();
				Set<String> variables = r.getVariables();
				
				System.out.println(r.getName());
//				System.out.println("Head========");
//				for (Triple t : head) {
//					System.out.println(t.getSubject());
//					System.out.println(t.getPredicate());
//					System.out.println(t.getObject());
//				}
//				System.out.println("Body========");
//				for (Triple t : body) {
//					System.out.println(t.getSubject());
//					System.out.println(t.getPredicate());
//					System.out.println(t.getObject());
//				}
//				System.out.println("Variables========");
//				for (String s : variables) {
//					System.out.println(s);
//				}
			}
			
			System.out.println(ruleList.size());
			
			}
			catch (IOException e) 
		    {
		        e.printStackTrace();
		    }
		
		
	}
	
}
