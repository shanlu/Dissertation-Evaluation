# Dissertation-Evaluation
This repository is an evaluation of relevance reasoning method in my PhD dissertation on MACCDC 2012 dataset. 

The U.S. National CyberWatch Mid-Atlantic Collegiate Cyber Defence Competition (MACCDC) provides college students with real life cyber defence experience. MACCDC official website: http://maccdc.org/. The SecRepo dataset (http://www.secrepo.com/) provides Snort alsrts generated on the dataset.

The Ontology folder contains the domain ontologies and the BVR rules. STIX.owl is the knowledge base in which Snort alserts are annotated as instances. STIX_empty.owl is the domian ontology without Snort alerts data. STO-L.owl is the top-level ontology that is imported into STIX.owl. rule_all.bvr contains the user-defined domain rules.

First, Parser.java in com.thesis.data package is used to annotate Snort alserts to the domain ontology.

Then, QueryGen.java in com.thesis.query package is used to generate SPARQL queries. 

QueryAnswer.java in com.thesis.SPARQL package invokes Jena SPARQL engine to answer SPARQL queries and write results into files. QueryAnswerToTriples.java interprate the results returned from the SPARQL engine into triples. 

Backtracking.java in com.thesis.relevance package is used to extract relevant facts for query answers and asserted these relevant facts into a STIX_empty.owl. 

