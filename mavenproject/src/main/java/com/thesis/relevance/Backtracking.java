package com.thesis.relevance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyInputSourceException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import com.thesis.util.Triple;

import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplString;

import com.opencsv.exceptions.CsvException;
import com.thesis.SPARQL.QueryAnswer;
import com.thesis.SPARQL.QueryAnswerToTriples;
import com.thesis.util.Rule;
import com.thesis.util.ScanBVRRules;
import com.thesis.util.ScanOWLRules;

public class Backtracking {
	
	static final String baseOntoName  = "/Users/shanlu/Documents/Data/NewSTIX/STIX.owl";
	static final String inferredOntoName  = "/Users/shanlu/Documents/Data/NewSTIX/bvrDump_ruleall.rdf";
	static final String inputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX";
	
	static final String outputFolderName  = "/Users/shanlu/Documents/Data/NewSTIX/Relevant";
	static final String queryAnswerFolder = "/Users/shanlu/Documents/Data/NewSTIX/QueryAnswer";
	
	static final String base  = "http://stix.mitre.org/STIX";
	static final String base_stol  = "http://www.vistology.com/ont/2013/STO-L.owl";
	
	static final String BVRruleFileName = "/Users/shanlu/Documents/Data/NewSTIX/rule_all.bvr";

	
	
	static Set<String> relevantFacts = new HashSet<>();
	static List<Rule> rules = new ArrayList<Rule>();
	
	
	public static boolean isDomainFact(Triple fact, OWLDataFactory dataFactory, OWLReasoner reasoner) {
		if (fact.getPredicate().equals("rdf:type")) {
			OWLNamedIndividual sub = dataFactory.getOWLNamedIndividual(IRI.create(fact.getSubject()));
			OWLClass obj = dataFactory.getOWLClass(IRI.create(fact.getObject()));
			NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(obj, false);
	        Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
	        for (OWLNamedIndividual in : individuals) {
	        	if (sub.equals(in)) {
	        		return true;
	        	}
	        }
		}
		else {
			OWLObjectProperty re = dataFactory.getOWLObjectProperty(IRI.create(fact.getPredicate()));
			OWLNamedIndividual sub = dataFactory.getOWLNamedIndividual(IRI.create(fact.getSubject()));
			OWLNamedIndividual obj = dataFactory.getOWLNamedIndividual(IRI.create(fact.getObject()));
			Set<OWLNamedIndividual> values = reasoner.getObjectPropertyValues(sub, re).getFlattened();
        	for (OWLNamedIndividual v : values) {
        		if (v.getIRI().toString().equals(fact.getObject())) {
        			return true;
        		}
        	}
        	
        	OWLDataProperty da = dataFactory.getOWLDataProperty(IRI.create(fact.getPredicate()));
			Set<OWLLiteral> lits = reasoner.getDataPropertyValues(sub, da);
        	for (OWLLiteral l : lits) {
        		if (l.toString().contains(fact.getObject())) {
        			return true;
        		}
        	}
		}
	
		return false;
	}
	public static boolean isBaseFact(Triple fact, OWLDataFactory dataFactory, OWLOntology base, OWLReasoner reasoner) {
		
		if (fact.getPredicate().equals("rdf:type")) {
			OWLNamedIndividual sub = dataFactory.getOWLNamedIndividual(IRI.create(fact.getSubject()));
			OWLClass obj = dataFactory.getOWLClass(IRI.create(fact.getObject()));
	        Set<OWLNamedIndividual> individuals = obj.getIndividualsInSignature();
	        for (OWLNamedIndividual in : individuals) {
	        	if (sub.equals(in)) {
	        		return true;
	        	}
	        }
		}
		else {
			OWLNamedIndividual sub = dataFactory.getOWLNamedIndividual(IRI.create(fact.getSubject()));
			Set<OWLObjectPropertyAssertionAxiom> subPAxioms = base.getObjectPropertyAssertionAxioms(sub);
			for (OWLObjectPropertyAssertionAxiom a : subPAxioms) {
        		Set<OWLObjectProperty> ps = a.getObjectPropertiesInSignature();
        		for (OWLObjectProperty p : ps) {
        			if (p.getIRI().toString().equals(fact.getPredicate()) && a.getObject().toString().contains(fact.getObject())) {
        				return true;
        			}
        		}
        	}
        	
        	OWLDataProperty da = dataFactory.getOWLDataProperty(IRI.create(fact.getPredicate()));
			Set<OWLLiteral> lits = reasoner.getDataPropertyValues(sub, da);
        	for (OWLLiteral l : lits) {
        		if (l.toString().contains(fact.getObject())) {
        			return true;
        		}
        	}
		}
		
		return false;
	}
	
	public static void addFacts(Triple fact, OWLDataFactory dataFactory_out, OWLOntologyManager manager_output, OWLOntology out) throws OWLOntologyStorageException {
//		System.out.println("Added to Relevant Facts ===" + fact.getSubject()+ ",  " + fact.getPredicate() + ",  " + fact.getObject());
		
		if (fact.getPredicate().equals("rdf:type")) {
			OWLNamedIndividual sub = dataFactory_out.getOWLNamedIndividual(IRI.create(fact.getSubject()));
			OWLClass obj = dataFactory_out.getOWLClass(IRI.create(fact.getObject()));
			OWLAxiom axiom = dataFactory_out.getOWLClassAssertionAxiom(obj, sub);
			manager_output.addAxiom(out, axiom);
			manager_output.saveOntology(out);
		}
		else if (!fact.getObject().contains("http")) {
			OWLNamedIndividual sub = dataFactory_out.getOWLNamedIndividual(IRI.create(fact.getSubject()));
			OWLDataProperty re = dataFactory_out.getOWLDataProperty(IRI.create(fact.getPredicate()));
			OWLLiteral owlLiteral = new OWLLiteralImplString(fact.getObject());
			OWLAxiom axiom = dataFactory_out.getOWLDataPropertyAssertionAxiom(re, sub, owlLiteral);
			manager_output.addAxiom(out, axiom);
			manager_output.saveOntology(out);
		}
		else {
			OWLNamedIndividual sub = dataFactory_out.getOWLNamedIndividual(IRI.create(fact.getSubject()));
			OWLObjectProperty re = dataFactory_out.getOWLObjectProperty(IRI.create(fact.getPredicate()));
			OWLNamedIndividual obj = dataFactory_out.getOWLNamedIndividual(IRI.create(fact.getObject()));
			OWLAxiom axiom = dataFactory_out.getOWLObjectPropertyAssertionAxiom(re, sub, obj);
			manager_output.addAxiom(out, axiom);
			manager_output.saveOntology(out);
		}
		
	}
	
	
	public static void buildDerivation(Triple answerToQuery, OWLReasoner reasoner_inferred, OWLOntology inferred, OWLDataFactory dataFactory_inferred, OWLReasoner reasoner, OWLOntology base, OWLDataFactory dataFactory, OWLDataFactory dataFactory_out, OWLOntologyManager manager_output, OWLOntology out) throws OWLOntologyStorageException {
		if (!relevantFacts.contains(answerToQuery.getSubject() + ", " + answerToQuery.getPredicate() + ", " + answerToQuery.getObject())) {
			relevantFacts.add(answerToQuery.getSubject() + ", " + answerToQuery.getPredicate() + ", " + answerToQuery.getObject());
			addFacts(answerToQuery, dataFactory_out, manager_output, out);
		}

		
		 if (isBaseFact(answerToQuery, dataFactory, base, reasoner)){
			 return;
		 }
		 
		 String subject = answerToQuery.getSubject();
		 String predicate = answerToQuery.getPredicate();
		 String object = answerToQuery.getObject();
		 
		 List<Triple> nextStep = new ArrayList<>();
//		 System.out.println("Building Derivation ==" + subject + ", " + predicate + ", " + object);
		 
		 if (predicate.equals("rdf:type")) {
			 OWLClass objectC = dataFactory.getOWLClass(IRI.create(object));
			 Set<OWLEquivalentClassesAxiom> axioms = base.getEquivalentClassesAxioms(objectC);
			 
			 for (OWLEquivalentClassesAxiom a : axioms) {
				 Set<OWLClass> cs = a.getClassesInSignature();
				 Set<OWLNamedIndividual> is = a.getIndividualsInSignature();
				 Set<OWLObjectProperty> ps = a.getObjectPropertiesInSignature();
				 Set<OWLDataProperty> ds = a.getDataPropertiesInSignature();
				 Set<String> values = new HashSet<>();
				 
				 Pattern pattern = Pattern.compile("\"([^\"]*)\"");
				 Matcher m = pattern.matcher(a.toString());
				 while (m.find()) {
				   values.add(m.group(1));
				 }
					
				 if (!is.isEmpty()) {
//					 System.out.println("hasValue Axioms with objectproperty");
					 for (OWLNamedIndividual i: is) {
							String newObject = i.getIRI().toString();
							for (OWLObjectProperty p: ps) {
								String newPredicate = p.getIRI().toString();
								Triple t = new Triple();
								t.setSubject(subject);
								t.setPredicate(newPredicate);
								t.setObject(newObject);
								
								nextStep.add(t);
//								System.out.println("NEXT:==" + t.getSubject() + ", " + t.getPredicate() + ", " + t.getObject());
							}
					}
				 }
				 
				 if (!ds.isEmpty()) {
//					 System.out.println("hasValue Axioms with dataproperty");
					 for (OWLDataProperty d : ds) {
						 String newPredicate = d.getIRI().toString();
						 for (String v : values) {
							 Triple t = new Triple();
							 t.setSubject(subject);
							 t.setPredicate(newPredicate);
							 t.setObject(v);
							
							 nextStep.add(t);
//							 System.out.println("NEXT:==" + t.getSubject() + ", " + t.getPredicate() + ", " + t.getObject());
						 }
					 }
				 }
				 
				 if (!cs.isEmpty()) {
//					 System.out.println("someValue Axioms with objectproperty");
					 for (OWLClass c: cs) {
						 if (!c.getIRI().toString().equals(object)) {
							 OWLNamedIndividual sub_instance = dataFactory_inferred.getOWLNamedIndividual(IRI.create(subject));
							 for (OWLObjectProperty p: ps) {
								 Set<OWLNamedIndividual> newobjs = reasoner_inferred.getObjectPropertyValues(sub_instance, p).getFlattened();
						        	for (OWLNamedIndividual newobj : newobjs) {
						        		Triple t1 = new Triple();
										t1.setSubject(subject);
										t1.setPredicate(p.getIRI().toString());
										t1.setObject(newobj.getIRI().toString());
										nextStep.add(t1);
										
//										System.out.println("NEXT:==" + t1.getSubject() + ", " + t1.getPredicate() + ", " + t1.getObject());
										
										Triple t2 = new Triple();
										t2.setSubject(newobj.getIRI().toString());
										t2.setPredicate("rdf:type");
										t2.setObject(c.getIRI().toString());
										nextStep.add(t2);
										
//										System.out.println("NEXT:==" + t2.getSubject() + ", " + t2.getPredicate() + ", " + t2.getObject());
						    
						        	}
							 }
					 }
						 
					}
				 }
			 }
			 
		 }
		 else {
			 
//			 System.out.println("Checking rules for ===" + predicate);
			 Iterator<Rule> rule_iterator = rules.iterator();
		 
			 while (rule_iterator.hasNext()) {
				Rule cur = rule_iterator.next();
				Set<String> variables = cur.getVariables();
				List<Triple> head = cur.getHead();
				
				if (!head.get(0).getPredicate().equals(predicate)) {
//					System.out.println(cur.getName() + " is NOT a relevant rule!");
					continue;
				}
				
				List<Triple> body = cur.getBody();
				HashMap<String, String> map = new HashMap<>();
				
				if (variables.contains(cur.getHead().get(0).getSubject())) {
					map.put(cur.getHead().get(0).getSubject(), subject);
				}
				if (variables.contains(cur.getHead().get(0).getObject())) {
					map.put(cur.getHead().get(0).getObject(), object);
				}
				
				Iterator<Triple> body_iterator = body.iterator();
				while (body_iterator.hasNext()) {
					Triple curT = body_iterator.next();
					String curT_sub = curT.getSubject();
					String curT_pre = curT.getPredicate();
					String curT_obj = curT.getObject();
					
					if (variables.contains(curT_sub) && map.containsKey(curT_sub)) {
						curT_sub = map.get(curT_sub);
					}
					if (variables.contains(curT_pre) && map.containsKey(curT_pre)) {
						curT_pre = map.get(curT_pre);
					}
					if (variables.contains(curT_obj) && map.containsKey(curT_obj)) {
						curT_obj = map.get(curT_obj);
					}
					
					if (!variables.contains(curT_sub) && !variables.contains(curT_pre) && !variables.contains(curT_obj)) {
						Triple t = new Triple();
						t.setSubject(curT_sub);
						t.setPredicate(curT_pre);
						t.setObject(curT_obj);
						nextStep.add(t);
//						System.out.println("Body triple match! ==" + t.getSubject() + ", " +t.getPredicate() + ", " + t.getObject());
					}
					else {
						if (variables.contains(curT_sub) && map.containsKey(curT_sub)) {
							curT_sub = map.get(curT_sub);
						}
						if (variables.contains(curT_pre) && map.containsKey(curT_pre)) {
							curT_pre = map.get(curT_pre);
						}
						if (variables.contains(curT_obj) && map.containsKey(curT_obj)) {
							curT_obj = map.get(curT_obj);
						}
					
						if (curT_pre.equals("rdf:type")) {
							OWLClass type = dataFactory_inferred.getOWLClass(IRI.create(curT_obj));
							Set<OWLNamedIndividual> instances = reasoner_inferred.getInstances(type, false).getFlattened();
							for (OWLNamedIndividual ins : instances) {
								if (ins.getIRI().toString().equals(curT_sub)) {
									String newSub = ins.getIRI().toString();
									Triple t = new Triple();
									t.setSubject(newSub);
									t.setPredicate(curT_pre);
									t.setObject(curT_obj);
									
									nextStep.add(t);
									
								}
							}
						}
						else {
//							System.out.println("Body triple with variable! ==" + curT_sub + ", " +curT_pre + ", " + curT_obj);
							if (map.containsKey(curT_sub)) {
								curT_sub = map.get(curT_sub);
							}
							OWLObjectProperty re = dataFactory_inferred.getOWLObjectProperty(IRI.create(curT_pre));
							OWLNamedIndividual sub = dataFactory_inferred.getOWLNamedIndividual(IRI.create(curT_sub));
							Set<OWLNamedIndividual> values = reasoner_inferred.getObjectPropertyValues(sub, re).getFlattened();
							for (OWLNamedIndividual i : values) {
								String newObj = i.getIRI().toString();
								map.put(curT_obj, newObj);
								Triple t = new Triple();
								t.setSubject(curT_sub);
								t.setPredicate(curT_pre);
								t.setObject(newObj);
								
								nextStep.add(t);
								
//								System.out.println("NEXT:==" + t.getSubject() + ", " + t.getPredicate() + ", " + t.getObject());
							}
							
						}
					}
					
				}
				
				map.clear();
				
			 }
		 
		 }
			 
		 for (Triple next : nextStep) {
//			 System.out.println("next: " + next.getSubject() + ", " + next.getPredicate() + ", " + next.getObject());
			 if (isDomainFact(next, dataFactory_inferred, reasoner_inferred)){
//				 System.out.println("next will be added: " + next.getSubject() + ", " + next.getPredicate() + ", " + next.getObject());
				 buildDerivation(next, reasoner_inferred, inferred, dataFactory_inferred, reasoner, base, dataFactory, dataFactory_out, manager_output, out);
			 }
		 }
		
		
		return;
		
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, CsvException, OWLOntologyStorageException {
		List<Rule> BVRrules = ScanBVRRules.readBVRFile(BVRruleFileName);
		rules.addAll(BVRrules);
		
		java.lang.Runtime rt = java.lang.Runtime.getRuntime();
			
			File Q_folder = new File(queryAnswerFolder);
			String[] query_answer_filenames = Q_folder.list(); 
			int i = 0;

			for (String qa_file :query_answer_filenames) {
				
				System.out.println("Read Query Answer in: " + qa_file);
				String output_filename = qa_file.substring(0, qa_file.length()-4); 
				
				java.lang.Process p1 = rt.exec("cp /Users/shanlu/Documents/Data/NewSTIX/Empty/STIX_empty.owl /Users/shanlu/Documents/Data/NewSTIX/Relevant/STIX_empty.owl");
				
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLOntologyManager manager_inferred = OWLManager.createOWLOntologyManager();
				OWLOntologyManager manager_output = OWLManager.createOWLOntologyManager();
				
				File file = new File(baseOntoName);
				File file_inferred = new File(inferredOntoName);
				File folder = new File(inputFolderName);
				
				AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
				manager.addIRIMapper(mapper);
				manager_output.addIRIMapper(mapper);
				
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
				
				
				SimpleIRIMapper iriMapper6 =  new SimpleIRIMapper(IRI.create("http://data-marking.mitre.org/DataMarking#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/DataMarking.owl")));
				manager_output.addIRIMapper(iriMapper6);
				SimpleIRIMapper iriMapper7 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/Common#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/CyboxCommon.owl")));
				manager_output.addIRIMapper(iriMapper7);
				SimpleIRIMapper iriMapper8 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/cybox_v1#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Cybox.owl")));
				manager_output.addIRIMapper(iriMapper8);
				SimpleIRIMapper iriMapper9 =  new SimpleIRIMapper(IRI.create("http://maec.mitre.org/XMLSchema/maec-core-2#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Maec.owl")));
				manager_output.addIRIMapper(iriMapper9);
				SimpleIRIMapper iriMapper10 =  new SimpleIRIMapper(IRI.create("http://capec.mitre.org/capec_v1#"), IRI.create(new File("/Users/shanlu/Documents/Data/NewSTIX/Capec.owl")));
				manager_output.addIRIMapper(iriMapper10);
				
				try {
					OWLOntology stix = manager.loadOntologyFromOntologyDocument(file);
					System.out.println("Loaded ontology: " + stix);
					OWLDataFactory dataFactory = manager.getOWLDataFactory();
					
					OWLOntology temp = manager_inferred.loadOntologyFromOntologyDocument(file_inferred);
					System.out.println("Loaded ontology: " + temp);
					OWLDataFactory dataFactory_inferred = manager_inferred.getOWLDataFactory();
					
					OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
					OWLReasoner reasoner = reasonerFactory.createReasoner(stix);
					reasoner.precomputeInferences();
					OWLReasoner reasoner1 = reasonerFactory.createReasoner(temp);
					reasoner1.precomputeInferences();
				
				
					String outputFileName = outputFolderName + "/STIX_empty.owl";
					File file_output = new File(outputFileName);
					OWLOntology out = manager_output.loadOntologyFromOntologyDocument(file_output);
					System.out.println("Loaded ontology: " + out);
					OWLDataFactory dataFactory_out = manager_output.getOWLDataFactory();
					
					List<Triple> tests = QueryAnswerToTriples.answeringQ(queryAnswerFolder+"/"+qa_file);
					for (Triple test : tests) {
//						System.out.println(test.getSubject() + ", " + test.getPredicate() + ", " + test.getObject());
						buildDerivation(test,reasoner1, temp, dataFactory_inferred, reasoner, stix, dataFactory, dataFactory_out, manager_output, out);
					}
					
					String rename_command = "mv /Users/shanlu/Documents/Data/NewSTIX/Relevant/STIX_empty.owl /Users/shanlu/Documents/Data/NewSTIX/Relevant/" + output_filename + ".owl";
					java.lang.Process p2 = rt.exec(rename_command);
					
					relevantFacts = new HashSet<>();
					
				} catch (OWLOntologyCreationException ex) {
		            // throw custom exception
		        }
				
			}
		
	}


}
