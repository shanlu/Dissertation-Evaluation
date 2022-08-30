# Dissertation-Evaluation
This repository is an evaluation of relevance reasoning method in my PhD dissertation on MACCDC 2012 dataset. 

The U.S. National CyberWatch Mid-Atlantic Collegiate Cyber Defence Competition (MACCDC) provides college students with real life cyber defence experience. MACCDC official website: http://maccdc.org/. The SecRepo dataset (http://www.secrepo.com/) provides Snort alsrts generated on the dataset.

The Ontology folder contains the domain ontologies and the BVR rules. STIX.owl is the knowledge base in which Snort alserts are annotated as instances. STIX_empty is the domian ontology without Snort alerts data. The java classes in com.thesis.data package are used to annotate Snort alserts to domain ontology.

The java classes in com.thesis.query package are used to generate SPARQL queries. The java classes in com.thesis.SPARQL package are used to answer SPARQL queries, and check the query results. The java class in com.thesis.relevance package is used to extract relevant facts for query answers. The java classes in com.thesis.results package is used to calculate precision and recall. 
