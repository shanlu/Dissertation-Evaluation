package com.thesis.results;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVWriter;
import com.thesis.util.Triple;

public class Calculate {
	
	static final String outputFileName  = "/Users/shanlu/Documents/Data/NewSTIX/Results/calculate.csv";
	static final String intputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/NewEx";
	static final String targetFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/QueryAnswer-check";
	
	public static void writeToTable(String outputFileName, List<String> query, List<String> precision, List<String> recall) {
		File result_folder = new File(outputFileName);
	    try {
	        // create FileWriter object with file as parameter
	        FileWriter outputfile = new FileWriter(result_folder);
	  
	        // create CSVWriter object filewriter object as parameter
	        CSVWriter writer = new CSVWriter(outputfile);
	  
	        // adding header to csv
	        String[] header = { "Number", "Query_name", "Precision", "Recall" };
	        writer.writeNext(header);
	  
	        int n = query.size();
	        
	        for (int i=0; i<n; i++) {
	        	String[] data = new String[6];
	        	data[0] = String.valueOf(i);
	        	data[1] = query.get(i);
	        	data[2] = precision.get(i);
	        	data[3] = recall.get(i);
	        	writer.writeNext(data);
	        }
	        // closing writer connection
	        writer.close();
	    }
	    catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
	
	public static int calculateBase(File target) throws IOException {
		int result = 0;
		String[] target_names = target.list();
		
		for (String name : target_names) {
			int number = readNumber(new File(targetFolderName+"/"+name));
			result = result + number;
		}
		
		return result;
	}
	
	public static String calculatePrecision(File source) throws IOException {
		String result = "";
		
		
		return result;
	}
	
	public static String calculateRecall(File source) throws IOException {
		String result = "";
		File target = new File(targetFolderName);
		String source_path = source.getAbsolutePath();
		
		double[] recalls = new double[71];
		int i = 0;
		double sum = 0;
		String[] target_names = target.list();
		if (source.list()==null) {
			System.out.println(source_path + " is empty");
			return "0";
		}
		Set<String> source_names = new HashSet<>(Arrays.asList(source.list()));
		for (String name : target_names) {
			if (!source_names.contains(name))
				recalls[i] = 0;
			else {
				File f_s = new File(source_path+"/"+name);
				File f_t = new File(targetFolderName+"/"+name);
				double rc = readNumber(f_s)/readNumber(f_t);
				recalls[i] = rc;
			}
			
			sum = sum + recalls[i];
			i++;
		}
		
		result = String.valueOf(sum/70);
		
		return result;
	}
	
	public static int readNumber(File f) throws IOException {
		int result = 0;
//		System.out.println("Read Number from: " + f.getAbsolutePath());
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = br.readLine();
		String number = "";
		while (line != null) {
			if (line.contains("The number of answers:")) {
				number = line.substring(line.indexOf(": ")+2);
				break;
			}
			line = br.readLine();
		}
		result = Integer.valueOf(number);
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		List<String> query  = new ArrayList<>();
		List<String> precision  = new ArrayList<>();
		List<String> recall = new ArrayList<>();
		
		File in = new File(intputFolderName);
		String[] subnames = in.list();
		for (String sub : subnames) {
			query.add(sub);
			File sub_folder = new File(intputFolderName+"/"+sub);
			double number = 0;
			if (sub_folder.list()!=null) {
				number = (double) sub_folder.list().length;
			}
			double pre = number/70.00;
			System.out.println("Precision is: " + pre);
			precision.add(String.valueOf(pre));
			
			String rc = calculateRecall(sub_folder);
			recall.add(rc);
		}
		
		
		writeToTable(outputFileName, query, precision, recall);
	}

}
