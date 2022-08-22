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
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVWriter;
import com.thesis.util.Triple;

public class Calculate2 {
	
	static final String outputFileName  = "/Users/shanlu/Documents/Data/NewSTIX/Results/calculate2.csv";
	static final String intputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/NewEx";
	static final String intputFolderName2  = "/Users/shanlu/Documents/Data/NewSTIX/ThirdEx";
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
	
	public static void combineTwoFile(File f1, File f2) throws IOException {
		int n1 = readNumber(f1);
		int n2 = readNumber(f2);
		int number = Math.max(n1, n2);
		
		writeToFile(f1, number);
		
	}
	
	public static void writeToFile(File f, int number) throws IOException {
	      //Instantiating the Scanner class to read the file
	      Scanner sc = new Scanner(f);
	      //instantiating the StringBuffer class
	      StringBuffer buffer = new StringBuffer();
	      
	      String oldLine = "";
	      //Reading lines of the file and appending them to StringBuffer
	      while (sc.hasNextLine()) {
	    	  String line = sc.nextLine();
	    	  if (line.contains("The number of answers")) {
	    		  oldLine = line;
	    	  }
	         buffer.append(line+System.lineSeparator());
	      }
	      String fileContents = buffer.toString();
//	      System.out.println("Contents of the file: "+fileContents);
	      //closing the Scanner object
	      sc.close();
	      String newLine = "The number of answers: " + number;
	      //Replacing the old line with new line
	      fileContents = fileContents.replaceAll(oldLine, newLine);
	      //instantiating the FileWriter class
	      FileWriter writer = new FileWriter(f.getAbsolutePath());
//	      System.out.println("");
//	      System.out.println("new data: "+fileContents);
	      writer.append(fileContents);
	      writer.flush();
	}
	
	public static void main(String[] args) throws IOException {
		List<String> query  = new ArrayList<>();
		List<String> precision  = new ArrayList<>();
		List<String> recall = new ArrayList<>();
		
		java.lang.Runtime rt = java.lang.Runtime.getRuntime();
		
		File in = new File(intputFolderName);
		String[] subnames = in.list();
		int i = 1;
		for (String sub : subnames) {
			query.add(String.valueOf(i));
			i++;
			File sub_folder = new File(intputFolderName+"/"+sub);
			String[] answersnames = sub_folder.list();
			if (sub_folder.list()!=null) {
				for (String answer : answersnames) {
					File f1 = new File(intputFolderName2 +"/"+answer);
					File f2 = new File(intputFolderName+"/"+sub+"/"+answer);
					if (f1.exists()) {
						combineTwoFile(f1, f2);
					}
					else {
						String command = "cp " + intputFolderName+"/"+sub+"/"+answer + " " + intputFolderName2 +"/"+answer;
						rt.exec(command);
					}
				}
			}
			
			File check_folder = new File(intputFolderName2);
			String[] checks = check_folder.list();
			double number = 0.00;
			if (check_folder.list()!=null) {
				number = check_folder.list().length;
			}
			double pre = number/70.00;
			System.out.println("Precision is: " + pre);
			precision.add(String.valueOf(pre));
			
			String rc = calculateRecall(check_folder);
			System.out.println("Recall is: " + rc);
			recall.add(rc);
		}
		
		
		writeToTable(outputFileName, query, precision, recall);
	}

}
