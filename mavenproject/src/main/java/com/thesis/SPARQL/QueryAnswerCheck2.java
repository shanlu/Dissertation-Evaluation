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

public class QueryAnswerCheck2 {
	
	static final String queryFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/Query_new";
	static final String outputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/NewEx";
	static final String base  = "http://stix.mitre.org/STIX";
	static final String base_stol  = "http://www.vistology.com/ont/2013/STO-L.owl";
	
	static final String ontoOutputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/Relevant_new";
	
	public static void answeringQ(String filename, String queryFolderName, String base, String outputFolderName) throws IOException {
		
	}
	
	public static void main(String[] args) {
		
		File onto = new File(ontoOutputFolderName);
		String[] ontonames = onto.list();
		for (String ontoname : ontonames) {
			String onto_file_path = "file:"+ ontoOutputFolderName +"/"+ ontoname;
			String onto_file_name = ontoname.substring(0, ontoname.length()-4); 
			
			File out_put_folder = new File(outputFolderName+ "/" + onto_file_name);
			if (!out_put_folder.exists()){
				out_put_folder.mkdirs();
			}
		
		File f = new File(queryFolderName);
	  	String[] filenames = f.list(); 
		for (String filename : filenames) {
	  		String queryFileName = queryFolderName + "/" + filename;
//	  		filename = filename.substring(0, filename.length()-4); 
	  		
	  		
	  		StringBuilder contentBuilder = new StringBuilder();
	  		String relation = "";
	  		
		    try {

		      // Reads an ontology with imports.
		      
		      OntModel model = ModelFactory.createOntologyModel();
		      OntDocumentManager dm = model.getDocumentManager();
//		      dm.addAltEntry(base,
//	                     "file:/Users/shanlu/Documents/Data/NewSTIX/Relevant_new/2432.owl");
		      dm.addAltEntry(base, onto_file_path);
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
		                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFolderName+ "/" + onto_file_name + "/" + filename));
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
