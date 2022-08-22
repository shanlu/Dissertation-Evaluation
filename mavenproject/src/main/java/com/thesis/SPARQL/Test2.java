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



public class Test2 {
	
	static final String queryFileName  = "/Users/shanlu/Documents/Data/NewSTIX/preprocessing.txt";
//	static final String queryFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/NewQuery";
//	static final String outputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/output";
	static final String base  = "http://stix.mitre.org/STIX";
	

	public static void main(String[] args) {
		
		 // Read query to a string
	    try {

	      // Reads an ontology with imports.
	      
	      OntModel model = ModelFactory.createOntologyModel();
	      OntDocumentManager dm = model.getDocumentManager();
	      dm.addAltEntry(base,
	                     "file:/Users/shanlu/Documents/Data/NewSTIX/bvrDump_ruleall.rdf");
	      model.read(base);
	      
	  	  
	      
	      StringBuilder contentBuilder = new StringBuilder();
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


	      UpdateAction.parseExecute(request, model) ;

	      OutputStream out = new FileOutputStream(new File("/Users/shanlu/Documents/Data/NewSTIX/dump.owl"));
	      model.write(out);
	      out.close();
	        
	      model.close();

	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

}

