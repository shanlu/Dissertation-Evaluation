package com.simplilearn.mavenproject;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyInputSourceException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
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


public class CleanData {
	static final String inputFileName  = "/Users/shanlu/Documents/Data/NewSTIX/Empty/STIX.owl";
	static final String inputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/Empty";
	static final String base  = "http://stix.mitre.org/STIX";
	static final String base_cybox  = "http://cybox.mitre.org/cybox_v1";
	static final String base_capec  = "http://capec.mitre.org/capec_v1";
	static final String base_stol  = "http://www.vistology.com/ont/2013/STO-L.owl";

	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		File file = new File(inputFileName);
		
		File folder = new File(inputFolderName);
		
		AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
		manager.addIRIMapper(mapper);
		
		SimpleIRIMapper iriMapper1 =  new SimpleIRIMapper(IRI.create("http://data-marking.mitre.org/DataMarking#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Empty/DataMarking.owl")));
		manager.addIRIMapper(iriMapper1);
		SimpleIRIMapper iriMapper2 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/Common#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Empty/CyboxCommon.owl")));
		manager.addIRIMapper(iriMapper2);
		SimpleIRIMapper iriMapper3 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/cybox_v1#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Empty/Cybox.owl")));
		manager.addIRIMapper(iriMapper3);
		SimpleIRIMapper iriMapper4 =  new SimpleIRIMapper(IRI.create("http://maec.mitre.org/XMLSchema/maec-core-2#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Empty/Maec.owl")));
		manager.addIRIMapper(iriMapper4);
		SimpleIRIMapper iriMapper5 =  new SimpleIRIMapper(IRI.create("http://capec.mitre.org/capec_v1#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Empty/Capec.owl")));
		manager.addIRIMapper(iriMapper5);
		
		
		try {
			OWLOntology stix = manager.loadOntologyFromOntologyDocument(file);
			System.out.println("Loaded ontology: " + stix);
			
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			
			OWLClass indicator = dataFactory.getOWLClass(IRI.create(base_capec + "#Indicator"));
			OWLClass observable = dataFactory.getOWLClass(IRI.create(base_cybox + "#Observable"));
			OWLClass event = dataFactory.getOWLClass(IRI.create(base + "#SnortEvent"));
			
			OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(stix));
			
			OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
			OWLReasoner reasoner = reasonerFactory.createReasoner(stix);
			reasoner.precomputeInferences();
			
			NodeSet<OWLNamedIndividual> individualsNodeSet1 = reasoner.getInstances(indicator, false);
	        Set<OWLNamedIndividual> individuals1 = individualsNodeSet1.getFlattened();
	        for (OWLNamedIndividual in : individuals1) {
	        	System.out.println(in);
	        	in.accept(remover);
	        }
	        
	        NodeSet<OWLNamedIndividual> individualsNodeSet2 = reasoner.getInstances(observable, false);
	        Set<OWLNamedIndividual> individuals2 = individualsNodeSet2.getFlattened();
	        for (OWLNamedIndividual in : individuals2) {
	        	System.out.println(in);
	        	in.accept(remover);
	        }
	        
	        NodeSet<OWLNamedIndividual> individualsNodeSet3 = reasoner.getInstances(event, false);
	        Set<OWLNamedIndividual> individuals3 = individualsNodeSet3.getFlattened();
	        for (OWLNamedIndividual in : individuals3) {
	        	System.out.println(in);
	        	in.accept(remover);
	        }
			
	        manager.applyChanges(remover.getChanges());
	        manager.saveOntology(stix);
	        remover.reset();
			
			
		} catch (OWLOntologyCreationException ex) {
            // throw custom exception
        }
	}
	
}


