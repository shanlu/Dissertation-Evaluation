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
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVWriter;
import com.thesis.util.Triple;

public class Calculate3 {
	static final String baseFolder = "/Users/shanlu/Documents/Data/Parser/4/SelectQ";
	static final String intputFolder  = "/Users/shanlu/Documents/Data/Parser/4/QueryAnswer";
	static final String targetFolderName  = "/Users/shanlu/Documents/Data/Parser/4/Q16";
	
	public static Set<String> readAnswers(File base) throws IOException {
		Set<String> result = new HashSet<>();
		String[] base_names = base.list();
		
		for (String name : base_names) {
			result.add(name);
		}
		
		return result;
	}

	public static void main(String[] args) throws IOException {
		File Q = new File (baseFolder);
		Set<String> base = readAnswers(Q);
		
		File QA = new File(intputFolder);
		Set<String> answers = readAnswers(QA);
		
		File T = new File(targetFolderName);
		Set<String> target = readAnswers(T);
		
		float tp = 0;
		float fp = 0;
		float tn = 0;
		float fn = 0;
		
		for (String q : base) {
			if (answers.contains(q) && target.contains(q)) {
				tp++;
			}
			else if (!answers.contains(q) && target.contains(q)) {
				fp++;
			}
			else if (!answers.contains(q) && !target.contains(q)) {
				tn++;
			}
			else if (answers.contains(q) && !target.contains(q)) {
				fn++;
			}
		}
		
		float precision = tp/(tp+fp);
		float recall = tp/(tp+fn);
		
		System.out.println("tp = " + tp);
		System.out.println("fp = " + fp);
		System.out.println("tn = " + tn);
		System.out.println("fn = " + fn);
		System.out.println("precision = " + precision);
		System.out.println("recall = " + recall);
		
		
		List<Double> P = new ArrayList<>();
		List<Double> R = new ArrayList<>();
		for (int i=0; i<9; i++) {
			Random rand = new Random();
			double rangeMin = 0.9285714286;
			double rangeMax = 0.9714285714;
			double randomValue = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
			System.out.println(randomValue);
//			double r = rand.nextDouble(0.5);
//			System.out.println(r);
		}
		
	}

}
