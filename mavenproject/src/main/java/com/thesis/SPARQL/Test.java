package com.thesis.SPARQL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.update.UpdateAction;



public class Test {
	
	static final String queryFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/NewQuery";
	static final String outputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/output";
	static final String base  = "http://stix.mitre.org/STIX";
	static final String base_stol  = "http://www.vistology.com/ont/2013/STO-L.owl";
	

	public static void main(String[] args) {
		
		 // Read query to a string
	    File f = new File(queryFolderName);
	  	String[] filenames = f.list(); 
		for (String filename : filenames) {
	  		String queryFileName = queryFolderName + "/" + filename;
	  		  
		filename = filename.substring(0, filename.length()-4);
		StringBuilder contentBuilder = new StringBuilder();
		
		
	    try {

	      // Reads an ontology with imports.
	      
	      OntModel model = ModelFactory.createOntologyModel();
	      OntDocumentManager dm = model.getDocumentManager();
	      dm.addAltEntry(base_stol,
	                     "file:/Users/shanlu/Documents/Data/NewSTIX/dump.owl");
	      model.read(base_stol);
	      
	  	  
//	      // Read query to a string
//		    File f = new File(queryFolderName);
//		  	String[] filenames = f.list(); 
//		  	
//		  	StringBuilder contentBuilder = new StringBuilder();
//		  	
//			for (String filename : filenames) {
//		  		String queryFileName = queryFolderName + "/" + filename;
//		  		  
//		  		filename = filename.substring(0, filename.length()-4);
	      
//		  		StringBuilder contentBuilder = new StringBuilder();
		        try (BufferedReader br = new BufferedReader(new FileReader(queryFileName))) 
		        {
		 
		            String sCurrentLine;
		            while ((sCurrentLine = br.readLine()) != null) 
		            {
		                contentBuilder.append(sCurrentLine).append("\n");
		       
		            }
		        } 
		        catch (IOException e) 
		        {
		            e.printStackTrace();
		        }

		        String request = contentBuilder.toString();
		        contentBuilder.delete(0, contentBuilder.length());
		      
		      UpdateAction.parseExecute(request, model);
	
		      OutputStream out = new FileOutputStream(new File(outputFolderName + "/" + filename + ".owl"));
		      model.write(out);
		      out.close();
	        
	      model.close();
	      

	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
		
	}

}

