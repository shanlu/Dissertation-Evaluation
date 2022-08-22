package com.thesis.SPARQL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;

import com.thesis.util.Triple;

public class QueryAnswerCheck {
	
	static final String queryFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/SelectQ";
	static final String outputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/QueryAnswer-check";
	static final String base  = "http://stix.mitre.org/STIX";
	static final String base_stol  = "http://www.vistology.com/ont/2013/STO-L.owl";
	
	static final String ontoOutputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/Relevant";
	
	public static void answeringQ(String filename, String queryFolderName, String base, String outputFolderName) throws IOException {
		
		String queryFileName = queryFolderName + "/" + filename;
		String filenumber = filename.substring(0, filename.length()-4);
		
		StringBuilder contentBuilder = new StringBuilder();
  		String relation = "";
  		
	    try {

	      // Reads an ontology with imports.
	      
	      OntModel model = ModelFactory.createOntologyModel();
	      OntDocumentManager dm = model.getDocumentManager();
	      String onto_path = "file:/Users/shanlu/Documents/Data/NewSTIX/Relevant/" + filenumber + ".owl";
	      dm.addAltEntry(base,onto_path);
	      model.read(base);
	      
	      
	      try (BufferedReader br = new BufferedReader(new FileReader(queryFileName))) 
	        {
	 
	            String sCurrentLine;
	            while ((sCurrentLine = br.readLine()) != null) 
	            {
	                contentBuilder.append(sCurrentLine).append("\n");
	                
	                if (sCurrentLine.contains("stol:Relation")) {
	                	relation = sCurrentLine.substring(0, sCurrentLine.indexOf(">")+1);
	                }
	            }
	        } 
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        }

	        String request = contentBuilder.toString();
	        
	        Query query = QueryFactory.create(request);
	        QueryExecution qexec = QueryExecutionFactory.create(query, model);
	        try {
	            ResultSet results = qexec.execSelect();
	            if(results.hasNext()) {
	                System.out.println("has results!");
	                int count = 0;
	                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFolderName+ "/" + filename));
	    		    writer.write("Relation: " + relation + "\n");
	    		    
	    		    while(results.hasNext()) {
		                QuerySolution soln = results.nextSolution();
		    		    writer.write(soln.toString());
		    		    writer.write("\n");
		    		    count++;
		            }
	    		    writer.write("The number of answers: " + count);
	    		    writer.close();
	    		    
	            }
	            else {
	                System.out.println("No Results!");
	            }
	           
	        }
		    catch(Exception e) {
		            e.printStackTrace(System.out);
		    }
	        System.out.println(filename + " Done!");
  		
	    }
	    catch (Exception e) {
		      e.printStackTrace();
	    }
	}
	
	public static void main(String[] args) {
		
		
		File f = new File(queryFolderName);
	  	String[] filenames = f.list(); 
		for (String filename : filenames) {
	  		String queryFileName = queryFolderName + "/" + filename;
//	  		filename = filename.substring(0, filename.length()-4); 
	  		
	  		if (filename.equals("4332.txt")) {
	  		
	  		StringBuilder contentBuilder = new StringBuilder();
	  		String relation = "";
	  		
		    try {

		      // Reads an ontology with imports.
		      
		      OntModel model = ModelFactory.createOntologyModel();
		      OntDocumentManager dm = model.getDocumentManager();
		      dm.addAltEntry(base,
	                     "file:/Users/shanlu/Documents/Data/NewSTIX/Relevant/4922.owl");
		      model.read(base);
		      
		      
		      try (BufferedReader br = new BufferedReader(new FileReader(queryFileName))) 
		        {
		 
		            String sCurrentLine;
		            while ((sCurrentLine = br.readLine()) != null) 
		            {
		                contentBuilder.append(sCurrentLine).append("\n");
		                
		                if (sCurrentLine.contains("stol:Relation")) {
		                	relation = sCurrentLine.substring(0, sCurrentLine.indexOf(">")+1);
		                }
		            }
		        } 
		        catch (IOException e) 
		        {
		            e.printStackTrace();
		        }

		        String request = contentBuilder.toString();
		        
		        Query query = QueryFactory.create(request);
		        QueryExecution qexec = QueryExecutionFactory.create(query, model);
		        try {
		            ResultSet results = qexec.execSelect();
		            if(results.hasNext()) {
		                System.out.println("has results!");
		                int count = 0;
		                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFolderName+ "/" + filename));
		    		    writer.write("Relation: " + relation + "\n");
		    		    
		    		    while(results.hasNext()) {
			                QuerySolution soln = results.nextSolution();
			    		    writer.write(soln.toString());
			    		    writer.write("\n");
			    		    count++;
			            }
		    		    writer.write("The number of answers: " + count);
		    		    writer.close();
		    		    
		            }
		            else {
		                System.out.println("No Results!");
		            }
		           
		        }
			    catch(Exception e) {
			            e.printStackTrace(System.out);
			    }
		        System.out.println(filename + " Done!");
	  		
		    }
		    catch (Exception e) {
			      e.printStackTrace();
		    }
		    
		}
		}
		
	}

}
