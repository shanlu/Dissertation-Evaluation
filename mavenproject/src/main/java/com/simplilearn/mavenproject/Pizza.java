package com.simplilearn.mavenproject;

import java.io.File;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyInputSourceException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;


public class Pizza {
	static final String pizzaFile  = "/Users/shanlu/Documents/Data/pizza.owl";
	static final String base_pizza  = "http://www.co-ode.org/ontologies/pizza/pizza.owl";
	
	public static void main(String[] args) throws OWLOntologyCreationException {
		OWLOntologyManager manager_pizza = OWLManager.createOWLOntologyManager();
	
		File file_pizza = new File(pizzaFile);
		
		try {
			OWLOntology pizza = manager_pizza.loadOntologyFromOntologyDocument(file_pizza);
			System.out.println("Loaded ontology: " + pizza);
			OWLDataFactory dataFactory_pizza = manager_pizza.getOWLDataFactory();
			
			OWLClass CheeseyPizza = dataFactory_pizza.getOWLClass(IRI.create(base_pizza + "#CheeseyPizza"));
			Set<OWLEquivalentClassesAxiom> eqAxioms = pizza.getEquivalentClassesAxioms(CheeseyPizza);
			for (OWLEquivalentClassesAxiom eqAxiom: eqAxioms) {
				System.out.println("OK1");
				System.out.println(eqAxiom); 
			}
			
			System.out.println(pizza.getEquivalentClassesAxioms(CheeseyPizza).toString());
			
		} catch (OWLOntologyCreationException ex) {
            // throw custom exception
        }
	}

}

