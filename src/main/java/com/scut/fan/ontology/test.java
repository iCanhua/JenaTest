package com.scut.fan.ontology;

import org.apache.jena.ontology.*;
import org.apache.jena.ontology.impl.OntModelImpl;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.lib.StringAbbrev;

import java.util.Iterator;

/**
 * Created by FAN on 2016/8/20.
 */
public class test {
    public static void main(String[] args) {
        OntModel  base= ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        base.read("file:/C:\\Users\\FAN\\Desktop\\医院项目\\医生推荐系统\\本体库及工具\\本体库\\正式本体库\\master\\10.11.owl");
        OntDocumentManager documentManager=base.getDocumentManager();

        base.listClasses();
        base.getBaseModel();
        base = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF, base );
        System.out.println("开始");

        String SOURSE ="http://www.semanticweb.org/fan/ontologies/2016/7/untitled-ontology-26";

        String NS=SOURSE+"#";

        OntClass  cardiovascularDisease= base.getOntClass( NS + "心血管疾病" );
        Individual cvd1 = base.createIndividual( NS + "心血管疾病1", cardiovascularDisease );

// list the asserted types
        for (Iterator<Resource> i = cvd1.listRDFTypes(true);i.hasNext(); ) {
            System.out.println( cvd1.getURI() + " is asserted in class " + i.next() );
        }

// list the inferred types
        cvd1 = base.getIndividual( NS + "张曹进" );

        for (Iterator<Resource> i = cvd1.listRDFTypes(true); i.hasNext(); ) {
            OntClass ontClass=i.next().as(OntClass.class);
            System.out.println(ontClass.isRestriction());
            System.out.println( cvd1.getURI() + " is inferred to be in class " + ontClass.toString() );
        }

        System.out.println("第二次开始");

        OntClass biology = base.getOntClass( NS + "Biology" );
        for (Iterator<OntClass> i = biology.listSubClasses(); i.hasNext(); ) {
            OntClass c = i.next();
            System.out.println( c.getURI() );
        }

        System.out.println("第三次开始");
        OntProperty p = base.getOntProperty( NS + "is_part_of" );

        Iterator<Restriction> i=p.listReferringRestrictions();
        while (i.hasNext()){
            Restriction r=i.next();
            if(r.isSomeValuesFromRestriction()){
                SomeValuesFromRestriction restriction=r.asSomeValuesFromRestriction();
                System.out.println(restriction.getSomeValuesFrom().getURI()+restriction.getOnProperty().getURI());
            }
            System.out.println();
        }

    }
}
