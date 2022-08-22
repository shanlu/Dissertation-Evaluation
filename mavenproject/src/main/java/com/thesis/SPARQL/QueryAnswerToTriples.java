package com.thesis.SPARQL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thesis.util.Triple;

public class QueryAnswerToTriples {
	static final String outputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/QueryAnswer";
	
	public static List<Triple> answeringQ(String filename) throws IOException {
		List<Triple> answers = new ArrayList<>();
		
		File f = new File(filename);
		// Construct BufferedReader from FileReader
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		String line = br.readLine();
		String predicate = "";
		while (line != null) {
			if (line.contains("Relation")) {
				predicate = line.substring(line.indexOf("<")+1, line.indexOf(">"));
			}
			else if (line.contains("object")){
				List<String> values = new ArrayList<>();
				 
				 Pattern pattern = Pattern.compile("\\<(.*?)\\>");
				 Matcher m = pattern.matcher(line);
				 while (m.find()) {
				   values.add(m.group(1));
//				   System.out.println(m.group(1));
				 }
				 
				 Triple t = new Triple();
					t.setSubject(values.get(0));
					t.setPredicate(predicate);
					t.setObject(values.get(1));
					
//					System.out.println(t.getSubject() + ", " + t.getPredicate() + ", " + t.getObject());
					answers.add(t);
			}
			line = br.readLine();
		}
		
		return answers;
	}

}
