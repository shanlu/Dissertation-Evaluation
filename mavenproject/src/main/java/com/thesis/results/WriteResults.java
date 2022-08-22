package com.thesis.results;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class WriteResults {
	
	static final String intputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/Relevant";
	static final String outputFileName  = "/Users/shanlu/Documents/Data/NewSTIX/Results/result.csv";
	
	static final String ontoFolderName  = "/Users/shanlu/Documents/Data/NewSTIX";
	static final String queryFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/SelectQ";
	static final String outputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/QueryAnswer-check";
	static final String outputFolderName2  = "/Users/shanlu/Documents/Data/NewSTIX/QueryAnswer-origin";
	static final String base  = "http://stix.mitre.org/STIX";
	
	
	public static void writeToTable(String outputFileName, List<String> query, List<String> file_size, List<String> axiom_count, List<String> individual_count, List<String> inference_time) {
		File result_folder = new File(outputFileName);
	    try {
	        // create FileWriter object with file as parameter
	        FileWriter outputfile = new FileWriter(result_folder);
	  
	        // create CSVWriter object filewriter object as parameter
	        CSVWriter writer = new CSVWriter(outputfile);
	  
	        // adding header to csv
	        String[] header = { "Number", "Query_name", "File_size", "Axiom_count", "Individual_count", "Inference_time" };
	        writer.writeNext(header);
	  
	        int n = query.size();
	        
	        for (int i=0; i<n; i++) {
	        	String[] data = new String[6];
	        	data[0] = String.valueOf(i);
	        	data[1] = query.get(i);
	        	data[2] = file_size.get(i);
	        	data[3] = axiom_count.get(i);
	        	data[4] = individual_count.get(i);
	        	data[5] = inference_time.get(i);
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
	
	public static void writeToTable2(String outputFileName, List<String> query, List<String> inference_time) {
		File result_folder = new File(outputFileName);
	    try {
	        // create FileWriter object with file as parameter
	        FileWriter outputfile = new FileWriter(result_folder);
	  
	        // create CSVWriter object filewriter object as parameter
	        CSVWriter writer = new CSVWriter(outputfile);
	  
	        // adding header to csv
	        String[] header = { "Number", "Query_name", "Inference_time" };
	        writer.writeNext(header);
	  
	        int n = query.size();
	        
	        for (int i=0; i<n; i++) {
	        	String[] data = new String[3];
	        	data[0] = String.valueOf(i);
	        	data[1] = query.get(i);
	        	data[2] = inference_time.get(i);
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
	
public static String answeringQ(String filename, String queryFolderName, String base, String outputFolderName) throws IOException {
		String answer = "yes";
;		String queryFileName = queryFolderName + "/" + filename;
		String filenumber = filename.substring(0, filename.length()-4);
		
		StringBuilder contentBuilder = new StringBuilder();
  		String relation = "";
  		
	    try {

	      // Reads an ontology with imports.
	      
	      OntModel model = ModelFactory.createOntologyModel();
	      OntDocumentManager dm = model.getDocumentManager();
//	      String onto_path = "file:/Users/shanlu/Documents/Data/NewSTIX/Relevant/" + filenumber + ".owl";
	      String onto_path = "file:/Users/shanlu/Documents/Data/NewSTIX/bvrDump_ruleall.rdf";
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
	                answer = "no";
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
	    
	    return answer;
	}

	
	public static void main(String[] args) throws FileNotFoundException, IOException, CsvException, OWLOntologyStorageException {
		
		List<String> query = new ArrayList<>();
		List<String> file_size = new ArrayList<>();
		List<String> axiom_count = new ArrayList<>();
		List<String> individual_count = new ArrayList<>();
		List<String> inference_time = new ArrayList<>();
		
		File rele_folder = new File(intputFolderName);
		String[] onto_names = rele_folder.list();
		

		
		for (String onto_name : onto_names) {
			String file_name = intputFolderName + "/" + onto_name;
			String name = onto_name.substring(0, onto_name.length()-4);
			
			
			
			File f = new File(file_name);
			long fileSize = f.length();
			
//			System.out.println(onto_name + " has " + fileSize + "bytes");
			
			long start_time = System.currentTimeMillis();
			answeringQ(name+".txt", queryFolderName, base, outputFolderName2);
			long end_time = System.currentTimeMillis();
//			System.out.println("inference time: " + String.valueOf(end_time-start_time));
			
			File answer = new File(outputFolderName+"/"+name+".txt");

			if (answer.exists()) {
			query.add(name);
			file_size.add(String.valueOf(fileSize));
			inference_time.add(String.valueOf(end_time-start_time));
			
			
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			File folder = new File(ontoFolderName);
			
//			AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
//			manager.addIRIMapper(mapper);
//			
//			SimpleIRIMapper iriMapper1 =  new SimpleIRIMapper(IRI.create("http://data-marking.mitre.org/DataMarking#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/DataMarking.owl")));
//			manager.addIRIMapper(iriMapper1);
//			SimpleIRIMapper iriMapper2 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/Common#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/CyboxCommon.owl")));
//			manager.addIRIMapper(iriMapper2);
//			SimpleIRIMapper iriMapper3 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/cybox_v1#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Cybox.owl")));
//			manager.addIRIMapper(iriMapper3);
//			SimpleIRIMapper iriMapper4 =  new SimpleIRIMapper(IRI.create("http://maec.mitre.org/XMLSchema/maec-core-2#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Maec.owl")));
//			manager.addIRIMapper(iriMapper4);
//			SimpleIRIMapper iriMapper5 =  new SimpleIRIMapper(IRI.create("http://capec.mitre.org/capec_v1#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Capec.owl")));
//			manager.addIRIMapper(iriMapper5);
//			
//			try {
//				OWLOntology stix = manager.loadOntologyFromOntologyDocument(f);
////				System.out.println("Loaded ontology: " + stix);
//				OWLDataFactory dataFactory = manager.getOWLDataFactory();
//				
//				int a_count = stix.getAxiomCount();
//				axiom_count.add(String.valueOf(a_count));
//				Set<OWLNamedIndividual> ins = stix.getIndividualsInSignature();
//				int i_count = ins.size();
//				individual_count.add(String.valueOf(i_count));
////				System.out.println("Axiom count: " + a_count);
////				System.out.println("Individual count: " + i_count);
//				
//			} catch (OWLOntologyCreationException ex) {
//            // throw custom exception
//			}
			
			}
			
		}
		
		System.out.println("It has " + query.size() + " queries");
//		System.out.println("It has " + file_size.size() + " sizes");
//		System.out.println("It has " + axiom_count.size() + " axioms");
//		System.out.println("It has " + individual_count.size() + " individuals");
		System.out.println("It has " + inference_time.size() + " times");
		
//		writeToTable(outputFileName, query, file_size, axiom_count, individual_count, inference_time);
		writeToTable2(outputFileName, query, inference_time);
	}
}
