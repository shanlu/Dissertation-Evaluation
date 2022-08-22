package com.thesis.OWLAxiom;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.SimpleIRIMapper;


public class OWLAxiom {
	
	static final String base  = "http://stix.mitre.org/STIX";
	static final String base_stol  = "http://www.vistology.com/ont/2013/STO-L.owl";
	static final String inputFileName  = "/Users/shanlu/Documents/Data/NewSTIX/STIX.owl";
	static final String inputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX";
	
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File(inputFileName);
		File folder = new File(inputFolderName);
		AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
		manager.addIRIMapper(mapper);
		SimpleIRIMapper iriMapper1 =  new SimpleIRIMapper(IRI.create("http://data-marking.mitre.org/DataMarking#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/DataMarking.owl")));
		manager.addIRIMapper(iriMapper1);
		SimpleIRIMapper iriMapper2 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/Common#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/CyboxCommon.owl")));
		manager.addIRIMapper(iriMapper2);
		SimpleIRIMapper iriMapper3 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/cybox_v1#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Cybox.owl")));
		manager.addIRIMapper(iriMapper3);
		SimpleIRIMapper iriMapper4 =  new SimpleIRIMapper(IRI.create("http://maec.mitre.org/XMLSchema/maec-core-2#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Maec.owl")));
		manager.addIRIMapper(iriMapper4);
		SimpleIRIMapper iriMapper5 =  new SimpleIRIMapper(IRI.create("http://capec.mitre.org/capec_v1#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Capec.owl")));
		manager.addIRIMapper(iriMapper5);
		
		
		try {
			OWLOntology stix = manager.loadOntologyFromOntologyDocument(file);
			System.out.println("Loaded ontology: " + stix);	
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			
			OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
			OWLReasoner reasoner = reasonerFactory.createReasoner(stix);
			reasoner.precomputeInferences();
			
			OWLDocumentFormat format = manager.getOntologyFormat(stix);
	        System.out.println("    format: " + format);
			
			OWLClass event = dataFactory.getOWLClass(IRI.create(base + "#PingIndicator"));
//			OWLClass observable = dataFactory.getOWLClass(IRI.create(base + "#AuthenticationObservable"));
			Set<OWLEquivalentClassesAxiom> axioms1 = stix.getEquivalentClassesAxioms(event);
//			Set<OWLEquivalentClassesAxiom> axioms2 = stix.getEquivalentClassesAxioms(observable);
//			
			OWLDataProperty da = dataFactory.getOWLDataProperty(IRI.create(base + "#message"));
			OWLNamedIndividual sub = dataFactory.getOWLNamedIndividual(IRI.create(base + "#snort1Event29031"));
			Set<OWLLiteral> lits = reasoner.getDataPropertyValues(sub, da);
        	for (OWLLiteral l : lits) {
        		System.out.println(l.toString());
        	}
//        	
//        	OWLNamedIndividual i1 = dataFactory.getOWLNamedIndividual(IRI.create(base + "#indicator_snort1Event33219"));
//        	OWLObjectProperty re = dataFactory.getOWLObjectProperty(IRI.create(base + "#isPrerequisiteOf"));
//        	Set<OWLObjectPropertyAssertionAxiom> objPAxioms = stix.getObjectPropertyAssertionAxioms(i1);
//        	for (OWLObjectPropertyAssertionAxiom obja : objPAxioms) {
//        		System.out.println(obja.toString());
//        		System.out.println(obja.getObjectPropertiesInSignature());
//        		System.out.println(obja.getObject().toString());
//        	}
        	
        	
        	
//        	Set<OWLNamedIndividual> values = reasoner.getObjectPropertyValues(i1, re).getFlattened();
//        	for (OWLNamedIndividual v : values) {
//        		System.out.println(v.getIRI().toString());
//    
//        	}
        	
			
			System.out.println(axioms1.toString());
			for (OWLEquivalentClassesAxiom a : axioms1) {
				System.out.println(a.getAxiomType());
				
				System.out.println("=======Classes in Axiom");
				Set<OWLClass> cs = a.getClassesInSignature();
				for (OWLClass c: cs) {
					System.out.println(c.getIRI());
				}
				System.out.println("=======Individuals in Axiom");
				Set<OWLNamedIndividual> is = a.getIndividualsInSignature();
				for (OWLNamedIndividual i: is) {
					System.out.println(i.getIRI());
				}
				System.out.println("=======DataProperty in Axiom");
				Set<OWLDataProperty> ds = a.getDataPropertiesInSignature();
				for (OWLDataProperty d: ds) {
					System.out.println(d.getIRI());
				}
				System.out.println("=======ObjectProperty in Axiom");
				Set<OWLObjectProperty> ps = a.getObjectPropertiesInSignature();
				for (OWLObjectProperty p: ps) {
					System.out.println(p.getIRI());
				}
				
				Pattern p = Pattern.compile("\"([^\"]*)\"");
				Matcher m = p.matcher(a.toString());
				while (m.find()) {
				  System.out.println(m.group(1));
				}
				
				if (!ds.isEmpty()) {
					
				}
			}
			
//			System.out.println(axioms2.toString());
//			for (OWLEquivalentClassesAxiom a : axioms2) {
//				System.out.println(a.getAxiomType());
//				System.out.println(a.getSignature().toString());
//				
//				System.out.println("=======Classes in Axiom");
//				Set<OWLClass> cs = a.getClassesInSignature();
//				for (OWLClass c: cs) {
//					System.out.println(c.getIRI());
//					Set<OWLNamedIndividual> instances = c.getIndividualsInSignature();	
//					for (OWLNamedIndividual i: instances) {
//						System.out.println(i.getIRI());
//					}
//				}
//				System.out.println("=======Individuals in Axiom");
//				Set<OWLNamedIndividual> is = a.getIndividualsInSignature();
//				for (OWLNamedIndividual i: is) {
//					System.out.println(i.getIRI());
//				}
//				System.out.println("=======DataProperty in Axiom");
//				Set<OWLDataProperty> ds = a.getDataPropertiesInSignature();
//				for (OWLDataProperty d: ds) {
//					System.out.println(d.getIRI());
//				}
//				System.out.println("=======ObjectProperty in Axiom");
//				Set<OWLObjectProperty> ps = a.getObjectPropertiesInSignature();
//				for (OWLObjectProperty p: ps) {
//					System.out.println(p.getIRI());
//				}
//			}
//			
			
			
			
		} catch (OWLOntologyCreationException ex) {
        // throw custom exception
		}
		
	}

}
