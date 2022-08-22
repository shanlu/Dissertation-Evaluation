package com.thesis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ScanBVRRules {
	static final String inputFileName  = "/Users/shanlu/Documents/Data/NewSTIX/rule_all.bvr";
	static final String baseURI = "http://stix.mitre.org/STIX";
	
	
	public static List<Rule> readBVRFile(String filename) throws IOException {
		File f = new File(filename);
		// Construct BufferedReader from FileReader
		BufferedReader br = new BufferedReader(new FileReader(f));
	 
		String queryString = "";
		List<String> queryFile = new ArrayList<String>();
		String line = null;
		
		List<Rule> ruleList = new ArrayList<Rule>();
		Rule rule = new Rule();
		Triple tri = new Triple();
		List<Triple> head = new ArrayList<Triple>();
		List<Triple> body = new ArrayList<Triple>();
		Set<String> variables = new HashSet<String>();
		String l = "";
		
		while ((line = br.readLine()) != null) {
			queryFile.add(line.trim());
			line = line.trim();
//			System.out.println(line);
			if (line.contains("<rule")) {
				rule = new Rule();
				rule.name = line.substring(line.indexOf("=") + 1, line.indexOf(">"));
			}
			if (line.contains("<body>")) {
				body = new ArrayList<Triple>();
				l = "body";
			}
			if (line.contains("<head>")) {
				head = new ArrayList<Triple>();
				l = "head";
			}
			if (line.contains("<triple>")) {
				tri = new Triple();
			}
			if (line.contains("<subject")) {
				if (line.contains("variable")) {
					String v = line.substring(line.indexOf("=") + 1, line.indexOf("/>"));
					tri.subject = v;
					variables.add(v);
				}
				else {
				String sub = line.substring(line.indexOf("=") + 1, line.indexOf("/>"));
				sub = sub.substring(sub.indexOf("\"")+1);
				sub = sub.substring(0, sub.indexOf("\""));
				tri.subject = sub;
				}
			}
			if (line.contains("<predicate")) {
				if (line.contains("variable")) {
					String v = line.substring(line.indexOf("=") + 1, line.indexOf("/>"));
					tri.predicate = v;
					variables.add(v);
				}
				else {
				String pre = line.substring(line.indexOf("=") + 1, line.indexOf("/>"));
				pre = pre.substring(pre.indexOf("\"")+1);
				pre = pre.substring(0, pre.indexOf("\""));
				tri.predicate = pre;
				}
			}
			if (line.contains("<object")) {
				if (line.contains("variable")) {
					String v = line.substring(line.indexOf("=") + 1, line.indexOf("/>"));
					tri.object = v;
					variables.add(v);
				}
				else {
				String obj = line.substring(line.indexOf("=") + 1, line.indexOf("/>"));
				obj = obj.substring(obj.indexOf("\"")+1);
				obj = obj.substring(0, obj.indexOf("\""));
				tri.object = obj;
				}
			}
			if (line.contains("</triple>")) {
				Triple temp = new Triple();
				temp = tri;
				if (l.equals("body")) {
					body.add(temp);
				}
				else {
					head.add(temp);
				}
				tri = new Triple();
			}
			if (line.contains("</body>")) {
				List<Triple> bodyTemp = new ArrayList<Triple>();
				bodyTemp = body;
				rule.body = bodyTemp;
				body = new ArrayList<Triple>();
			}
			if (line.contains("</head>")) {
				List<Triple> headTemp = new ArrayList<Triple>();
				headTemp = head;
				rule.head = headTemp;
				head = new ArrayList<Triple>();
			}
			if (line.contains("</rule>")) {
				Rule ruleTemp = new Rule();
				rule.variables = variables;
				ruleTemp = rule;
				ruleList.add(ruleTemp);
				rule = new Rule();
			}
			
		}
		br.close();
		
		
		return ruleList;
	}
	
	public static void main(String[] args) {
//		File f = new File(inputFileName);
		try {
			List<Rule> rulelist = readBVRFile(inputFileName);
			

			for (Rule r : rulelist) {
				List<Triple> head = r.getHead();
				List<Triple> body = r.getBody();
				Set<String> variables = r.getVariables();
				
				System.out.println(r.getName());
				System.out.println("Head========");
				for (Triple t : head) {
					System.out.println(t.getSubject());
					System.out.println(t.getPredicate());
					System.out.println(t.getObject());
				}
				System.out.println("Body========");
				for (Triple t : body) {
					System.out.println(t.getSubject());
					System.out.println(t.getPredicate());
					System.out.println(t.getObject());
				}
				System.out.println("Variables========");
				for (String s : variables) {
					System.out.println(s);
				}
				
			}
		}
		catch (IOException e) 
        {
            e.printStackTrace();
        }
		
	}

}

