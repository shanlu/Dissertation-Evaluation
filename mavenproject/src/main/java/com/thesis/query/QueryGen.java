package com.thesis.query;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyInputSourceException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class QueryGen2 {
	
	static final String inputFileName  = "/Users/shanlu/Documents/Data/Parser/16/STIX.owl";
	static final String inputFolderName  = "/Users/shanlu/Documents/Data/Parser/16";
	static final String base  = "http://stix.mitre.org/STIX";
	static final String base_stol  = "http://www.vistology.com/ont/2013/STO-L.owl";

	private static void writeQueryFile(String relation, String domain, String range, int n) throws IOException {
		String fileContent = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
			"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
			"PREFIX stix: <http://stix.mitre.org/STIX#>\n" +
			"PREFIX stol: <http://www.vistology.com/ont/2013/STO-L.owl#>\n";
	    
	    if (relation==null) {
	    	BufferedWriter writer1 = new BufferedWriter(new FileWriter("/Users/shanlu/Documents/Data/NewSTIX/NewQuery/"+ n + "1.txt"));
		    writer1.write(fileContent);
	    	String select1 = "INSERT {\n" +
	    					"stix:Sq rdfs:subClassOf stol:Infon.\n" + 
	    					"stix:s rdf:type stix:Sq.\n" + 
	    					"stix:s stol:relevantRelation ?relation.\n" + 
	    					"stix:s stol:relevantIndiviudal ?object1.\n" + 
	    					"stix:s stol:relevantIndiviudal ?object2.}\n" + 
	    					"WHERE {\n" +
	    					"?relation rdf:type stol:Relation.\n" + 
	    					"?relation stol:anchor1 ?object1.\n" + 
	    					"?relation stol:anchor2 ?object2.\n" +
	    					"?object1 rdf:type " + domain + ".\n" + 
	    					"?object2 rdf:type " + range + ".}\n";
	    	writer1.write(select1);
	    	writer1.close();
	    }
	    else {
	    	BufferedWriter writer2 = new BufferedWriter(new FileWriter("/Users/shanlu/Documents/Data/NewSTIX/NewQuery/"+ n + "2.txt"));
		    writer2.write(fileContent);
	    	String select2 = "INSERT {\n" +
							"stix:Sq rdfs:subClassOf stol:Infon.\n" + 
							"stix:s rdf:type stix:Sq.\n" + 
							"stix:s stol:relevantRelation "+ relation +".\n" + 
							"stix:s stol:relevantIndiviudal ?object1.\n" + 
							"stix:s stol:relevantIndiviudal ?object2.}\n" + 
							"WHERE { \n" + 
							relation + " rdf:type stol:Relation.\n" + 
							relation + " stol:anchor1 ?object1.\n" + 
							relation + " stol:anchor2 ?object2.\n" +
							"?object1 rdf:type " + domain + ".\n" + 
							"?object2 rdf:type " + range + ".}\n";
	    	writer2.write(select2);
	    	writer2.close();
	    }
	    
	}
	
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
		
		ArrayList<Relation> relationList = new ArrayList<Relation>();
		ArrayList<OWLObjectProperty> proList = new ArrayList<OWLObjectProperty>();
		
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File(inputFileName);
		File folder = new File(inputFolderName);
		AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
		manager.addIRIMapper(mapper);
		SimpleIRIMapper iriMapper1 =  new SimpleIRIMapper(IRI.create("http://data-marking.mitre.org/DataMarking#"), IRI.create(new File("/Users/shanlu/Documents/Data/Parser/16/DataMarking.owl")));
		manager.addIRIMapper(iriMapper1);
		SimpleIRIMapper iriMapper2 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/Common#"), IRI.create(new File("/Users/shanlu/Documents/Data/Parser/16/CyboxCommon.owl")));
		manager.addIRIMapper(iriMapper2);
		SimpleIRIMapper iriMapper3 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/cybox_v1#"), IRI.create(new File("/Users/shanlu/Documents/Data/Parser/16/Cybox.owl")));
		manager.addIRIMapper(iriMapper3);
		SimpleIRIMapper iriMapper4 =  new SimpleIRIMapper(IRI.create("http://maec.mitre.org/XMLSchema/maec-core-2#"), IRI.create(new File("/Users/shanlu/Documents/Data/Parser/16/Maec.owl")));
		manager.addIRIMapper(iriMapper4);
		SimpleIRIMapper iriMapper5 =  new SimpleIRIMapper(IRI.create("http://capec.mitre.org/capec_v1#"), IRI.create(new File("/Users/shanlu/Documents/Data/Parser/16/Capec.owl")));
		manager.addIRIMapper(iriMapper5);
		
		
		try {
			OWLOntology stix = manager.loadOntologyFromOntologyDocument(file);
			System.out.println("Loaded ontology: " + stix);	
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			
			OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
			OWLReasoner reasoner = reasonerFactory.createReasoner(stix);
			reasoner.precomputeInferences();
			
			OWLClass Relation = dataFactory.getOWLClass(IRI.create(base_stol + "#Relation"));
			NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(Relation, false);
	        Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
	        
	        for (OWLNamedIndividual in : individuals) {
	        	
	        	ArrayList<String> domainList = new ArrayList<String>();
	    		ArrayList<String> rangeList = new ArrayList<String>();
	        	
	        	String re = in.toString();
	        	System.out.println(re);
	        	
	        	if (!re.contains("message") && !re.contains("classification")) {
	        	OWLObjectProperty pro = dataFactory.getOWLObjectProperty(IRI.create(re.substring(re.indexOf("<")+1, re.indexOf(">"))));
	        	proList.add(pro);
//	        	re = re.substring(re.indexOf("<")+1, re.indexOf(">"));
	        	Relation rela = new Relation();
	        	rela.setName(re);
	        	
	        	
	        	Set<OWLClass> domains = reasoner.getObjectPropertyDomains(pro, true).getFlattened();
	        	for (OWLClass c : domains) {
	        		if (!domainList.contains(c.toString())) {
	        			domainList.add(c.toString());
	        		}
		        	Set<OWLClass> subdomains = reasoner.getSubClasses(c, true).getFlattened();
		        	for (OWLClass s : subdomains) {
		        		if (!domainList.contains(s.toString())) {
		        			domainList.add(s.toString());
				        	Set<OWLClass> subsubdomains = reasoner.getSubClasses(s, true).getFlattened();
				        	for (OWLClass ss : subsubdomains) {
				        		if (!domainList.contains(ss.toString())) {
				        			domainList.add(ss.toString());
				        		}
				        	}
		        		}

		        	}
	        	}
	        	rela.setDomains(domainList);
	        	
	        	NodeSet<OWLClass> rangesNodeSet = reasoner.getObjectPropertyRanges(pro, true);
	        	Set<OWLClass> ranges = rangesNodeSet.getFlattened();
	        	for (OWLClass r : ranges) {
	        		if (!rangeList.contains(r.toString())) {
	        			rangeList.add(r.toString());
	        		}
	        		NodeSet<OWLClass> subrangesNodeSet = reasoner.getSubClasses(r, false);
		        	Set<OWLClass> subranges = reasoner.getSubClasses(r, false).getFlattened();
		        	for (OWLClass s : subranges) {
		        		if (!rangeList.contains(s.toString()) && !s.toString().equals("owl:Nothing")) {
		        			rangeList.add(s.toString());
		        		}
			        	Set<OWLClass> subsubranges = reasoner.getSubClasses(s, false).getFlattened();
			        	for (OWLClass ss : subranges) {
			        		if (!rangeList.contains(ss.toString()) && !s.toString().equals("owl:Nothing")) {
			        			rangeList.add(ss.toString());
			        		}
			        	}
		        	}
	        	}
	        	
	        	rela.setRanges(rangeList);
	        	relationList.add(rela);
	        	System.out.println(rela.name);
	        }
	        }
			
		} catch (OWLOntologyCreationException ex) {
            // throw custom exception
        }
		
		int count = 0;
		
		ArrayList<String> queryList = new ArrayList<String>();
		
		try {
			
			for (Relation rela : relationList) {
				String relation = rela.name;
				List<String> domains = rela.domains;
				List<String> ranges = rela.ranges;
				for (String domain : domains) {
					if (!domain.equals("owl:Nothing") && !domain.contains("http://capec.mitre.org/capec_v1#Indicator") && !domain.contains("http://cybox.mitre.org/cybox_v1#Observable")) {
					for (String range : ranges) {
						if (!range.equals("owl:Nothing") && !range.contains("http://capec.mitre.org/capec_v1#Indicator") && !range.contains("http://capec.mitre.org/capec_v1#Indicator")) {
						String q = relation+ "  " + domain + "  " + range;
						if (!queryList.contains(q)) {
							queryList.add(q);
							writeQueryFile(relation, domain, range, count++);
						}
					}
					}
				}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
