package com.scut.fan.ontology;

import org.apache.jena.ontology.*;
import org.apache.jena.ontology.impl.OntModelImpl;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.lib.StringAbbrev;

import java.util.Iterator;

/**
 * Created by FAN on 2016/10/11.
 */
public class ontDaoUtils {
    static String SOURSE;
    static  OntModel getModel(){
        OntModel  base= ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        base.read("file:/C:\\Users\\FAN\\Desktop\\医院项目\\医生推荐系统\\本体库及工具\\本体库\\正式本体库\\master\\11.25(write).owl");
        OntDocumentManager documentManager=base.getDocumentManager();
        base = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_TRANS_INF, base );
        System.out.println("本体模型读取成功");
        return base;

    }

    static String getNS(){
        SOURSE ="http://www.semanticweb.org/fan/ontologies/2016/7/untitled-ontology-26";
        String NS=SOURSE+"#";
        return NS;
    }
    static String getPrefix(){
        return "PREFIX ontology: <http://www.semanticweb.org/fan/ontologies/2016/7/untitled-ontology-26#>"+
                "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>";
    }
    static String getRdfsPref(String prefix){
        if (prefix.equals("rdfs")){
            return "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>";
        }

        return null;
    }


}
