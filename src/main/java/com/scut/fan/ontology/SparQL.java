package com.scut.fan.ontology;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;

/**
 * Created by FAN on 2016/10/11.
 */
public class SparQL {
    public static void main(String[] args) {
        OntModel model= ontDaoUtils.getModel();

    }


    public static void simpleTest(OntModel ontModel){
        String queryString=ontDaoUtils.getPrefix()+
                "SELECT ?x "+
                "where {ontology:尿少 rdfs:subClassOf  ?x}";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, ontModel);
        ResultSet results = qexec.execSelect();
        ResultSetFormatter.out(System.out, results, query);
        for ( ; results.hasNext() ; )
        {
            QuerySolution soln = results.nextSolution() ;
        }
        //Model resultModel =qexec.execConstruct();
       // resultModel= ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF,resultModel);
        // Important - free up resources used running the query
        qexec.close();
    }





}
