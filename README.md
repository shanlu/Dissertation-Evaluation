# Dissertation-Evaluation
This repository is an evaluation of relevance reasoning method in my PhD dissertation on MACCDC 2012 dataset. 

The U.S. National CyberWatch Mid-Atlantic Collegiate Cyber Defence Competition (MACCDC) provides college students with real life cyber defence experience. MACCDC official website: http://maccdc.org/. The SecRepo dataset (http://www.secrepo.com/) provides Snort alsrts generated on the dataset.

The Ontology folder contains the domain ontologies and the BVR rules. STIX.owl is the knowledge base in which Snort alserts are annotated as instances. STIX_empty is the domian ontology without Snort alerts data. STO-L.owl is the top-level ontology that is imported into STIX.owl. rule_all.bvr contains the user-defined domain rules.

Parser.java in com.thesis.data package is used to annotate Snort alserts to domain ontology.

QueryGen.java in com.thesis.query package is used to generate SPARQL queries. 

QueryAnswer.java in com.thesis.SPARQL package invokes Jena SPARQL engine to answer SPARQL queries and write results into files. QueryAnswerToTriples.java interprate the results returned from the SPARQL engine into triples. 

Backtracking.java in com.thesis.relevance package is used to extract relevant facts for query answers. 

Calculate.java in com.thesis.results package is used to calculate precision and recall. 
