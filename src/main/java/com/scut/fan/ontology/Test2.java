package com.scut.fan.ontology;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.FileManager;

public class Test2 {
    public static String prefix = "PREFIX  untitled-ontology-26: <http://www.semanticweb.org/fan/ontologies/2016/7/untitled-ontology-26#>"
            + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
            + "PREFIX  owl: <http://www.w3.org/2002/07/owl#>" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";

    public static void main(String[] args) {
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_TRANS_INF);
        InputStream in = FileManager.get().open("file:/C:\\Users\\FAN\\Desktop\\医院项目\\医生推荐系统\\本体库及工具\\本体库\\正式本体库\\master\\11.25.owl");
        ontModel.read(in, null);

        Set<Individual> set1 = getIndividualByGoodAt(ontModel, "心肌病");
        System.out.println("One");
        for (Individual i : set1) {
            System.out.println(i);
        }
        System.out.println("Two");
        Set<Individual> set2 = getIndividualByChengyuan(ontModel, "冠心病组");
        for (Individual i : set1) {
            System.out.println(i);
        }
    }

    public static Set<Individual> getIndividualByGoodAt(OntModel ontModel, String sick) {
        String q = prefix + "SELECT ?a WHERE  "
                + "{ {?a rdf:type ?b. ?b owl:onProperty untitled-ontology-26:擅长 . ?b owl:someValuesFrom untitled-ontology-26:"
                + sick + ".} "
                + "UNION {?a rdf:type ?c. ?c owl:onProperty untitled-ontology-26:尤其擅长 . ?c owl:someValuesFrom untitled-ontology-26:"
                + sick + ".}" + "UNION {?a untitled-ontology-26:尤其擅长 untitled-ontology-26:" + sick + ".}"
                + "UNION {?a untitled-ontology-26:擅长 untitled-ontology-26:" + sick + ".}}";
        Set<Individual> set = getIndividualByQuery(ontModel, q);
        return set;

    }

    public static Set<Individual> getIndividualByChengyuan(OntModel ontModel, String group) {
        String q = prefix + "SELECT ?a WHERE " + "{?a untitled-ontology-26:是成员 untitled-ontology-26:" + group + "}";
        Set<Individual> set = getIndividualByQuery(ontModel, q);
        return set;

    }

    public static Set<Individual> getIndividualByQuery(OntModel ontModel, String q) {
        Query query = QueryFactory.create(q);
        Set<Individual> set = new HashSet<Individual>();
        try  {
            QueryExecution qexec = QueryExecutionFactory.create(query, ontModel);
            ResultSet results = qexec.execSelect();
            for (; results.hasNext();) {
                QuerySolution solution = results.next();
                RDFNode node = solution.get("a");
                if (node.canAs(Individual.class)) {
                    Individual individual = node.as(Individual.class);
                    set.add(individual);
                }
            }
        }finally {

        }
        return set;
    }
}

