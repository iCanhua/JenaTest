package com.scut.fan.ontology;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.HashSet;
import java.util.Iterator;

import java.util.Set;

/**
 * Created by FAN on 2016/10/21.
 */
public class ontParser {
    static String NS=ontDaoUtils.getNS();



    //打印本体
    public static void classSum(OntModel model){
        int disease=0;
        int count=0;
        int sybspy=0;
        System.out.println("本体模型类大小："+model.listClasses().toSet().size());

        Iterator it=model.listClasses();
        while (it.hasNext()){
            OntClass ontClass=(OntClass) it.next();

            if(!ontClass.isRestriction()){
                count++;
               // System.out.println(count+ontClass.toString());

            }else {
                Restriction re=ontClass.asRestriction();
                re.getRDFType();
                if(re.isSomeValuesFromRestriction()){
                    String aa=re.asSomeValuesFromRestriction().getSomeValuesFrom().toString();
                   // System.out.println("约束："+re.getOnProperty()+" SOME "+aa);
                   // System.out.println("yueshu URI"+re.asSomeValuesFromRestriction().getURI());
                }
                if(re.isAllValuesFromRestriction()){
                    String bb=re.asAllValuesFromRestriction().getAllValuesFrom().toString();
                   // System.out.println("约束："+re.getOnProperty()+" ONLY "+bb);
                }
            }
            if(isSuperClass(NS+"疾病及综合症",ontClass)){
                disease++;
            }

            if(ontClass.hasSuperClass(model.getOntClass(NS+"症状"))){
                sybspy++;
                System.out.println(ontClass.getLocalName());
            }
        }
        System.out.println("疾病个数："+disease);
        System.out.println("检查个数："+sybspy);
    }
    private static boolean isSuperClass(String superURI,OntClass ontClass){
        boolean finded=false;
        ExtendedIterator<OntClass> iterator=ontClass.listSuperClasses(false);
        while(iterator.hasNext()){
            OntClass superClass=(OntClass) iterator.next();
            if(superURI.equals(superClass.getURI())){
                finded=true;
            }
        }
        return finded;
    }

    /**
     * 出现一个问题，约束出现多次！打印多次，从中发现一个问题，
     * 约束是可以多次出现原因是它把三元组关系都列了出来，
     * 也就是我可以通过这样的方式去得到所有该疾病的相关症状！
     */
    public static void parseRestriction(OntModel model,String str){
        OntClass ontClass=model.getOntClass(NS+str);
        System.out.println("被约束类："+ontClass.toString());
        Iterator it =ontClass.listSuperClasses(true);
        while (it.hasNext()){
            OntClass sub=(OntClass)it.next();
            if(!sub.isRestriction()){
                System.out.println("命名类："+sub.getURI());
            }else {
                Restriction re=sub.asRestriction();
                System.out.println("不用转换也能打印出："+re.getSubClass().getURI());
                if(re.isSomeValuesFromRestriction()){
                    String aa=re.asSomeValuesFromRestriction().getSomeValuesFrom().toString();
                    System.out.println("约束："+re.asSomeValuesFromRestriction().getSubClass().toString()+re.getOnProperty()+" SOME "+aa);

                }
                if(re.isAllValuesFromRestriction()){
                    String bb=re.asAllValuesFromRestriction().getAllValuesFrom().toString();
                    System.out.println("约束："+re.getOnProperty()+" ONLY "+bb);
                }
            }
        }
    }

    /**
     * 构造约束
     */
    public static Restriction getRestriction(OntModel model, OntProperty ontProperty, Resource resource){
        Iterator<Restriction> i = model.listRestrictions();

        while (i.hasNext()) {
            Restriction r = i.next();
            if(r.isSomeValuesFromRestriction()){
                if(r.getOnProperty().getURI().equals(ontProperty.getURI())&&r.asSomeValuesFromRestriction().getSomeValuesFrom().getURI().equals(resource.getURI())){
                    System.out.println("约束："+r.getOnProperty()+" SOME "+r.asSomeValuesFromRestriction().getSomeValuesFrom().getURI());
                    ExtendedIterator it= r.asSomeValuesFromRestriction().listInstances();
                    //System.out.println("大小"+it.toSet().size());
                    while (it.hasNext()){
                        System.out.println("打印"+r.asSomeValuesFromRestriction().listInstances().next().getURI());
                    }
                    //System.out.println(r.asSomeValuesFromRestriction().listInstances().next().getURI());
                    return null;

                }
            }
            if(r.isAllValuesFromRestriction()){
                if(r.getOnProperty().getURI().equals(ontProperty.getURI())&&r.asAllValuesFromRestriction().getAllValuesFrom().getURI().equals(resource.getURI())){
                    System.out.println("约束："+r.getOnProperty()+" ONLY "+r.asAllValuesFromRestriction().getAllValuesFrom().getURI());
                    return null;
                }
            }
            System.out.println("循环完了吗");
        }
        return null;
    }

    //通过特殊属性来查找约束！
    public static Set<Restriction> getRestriction(OntModel ontModel, String property, String values) {
        Set<Restriction> restrictionSet = new HashSet<Restriction>();
        OntProperty p = ontModel.getOntProperty(ontDaoUtils.getNS() + property);
        Iterator<Restriction> i = p.listReferringRestrictions();
        while (i.hasNext()) {
            Restriction r = i.next();
            if (r.isSomeValuesFromRestriction()) {
                if (r.asSomeValuesFromRestriction().getSomeValuesFrom().getLocalName().equals(values)) {
                    restrictionSet.add(r);
                }
            }
            if (r.isAllValuesFromRestriction()) {
                if (r.asAllValuesFromRestriction().getAllValuesFrom().getLocalName().equals(values)) {
                    restrictionSet.add(r);
                }
            }
        }
        return restrictionSet;
    }

    public static Set<OntClass> getSubject(Set<Restriction> restrictionSet) {
        Set<OntClass> ontClassSet = new HashSet<OntClass>();
        for (Restriction r : restrictionSet) {
            for (Iterator<OntClass> i = r.listSubClasses(true); i.hasNext();) {
                OntClass ontClass = i.next();
                if (!ontClassSet.contains(ontClass))
                    System.out.println(ontClass.getLocalName());
                ontClassSet.add(ontClass);
            }
        }
        return ontClassSet;
    }

    public static Set<OntClass> getSuperClass(OntModel model,String uri){
        OntClass uriClass=model.getOntClass(NS+uri);
        for (Iterator<OntClass> i =uriClass.listSuperClasses(false);i.hasNext();){
            OntClass ontClass = i.next();
            System.out.println("找父类："+ontClass.getURI());
        }
        return null;
    }

    public static void listRDFType(OntModel model,String rdf){
        OntClass ontClass=model.getOntClass(NS+rdf);
        ontClass.getSuperClass();

    }


    public static void main(String[] args) {
        OntModel model= ontDaoUtils.getModel();
       // getSuperClass(model,"三房心");
        classSum(model);
//        parseRestriction(model,"饱餐");
          //getRestriction(model,model.getOntProperty(ontDaoUtils.getNS()+"擅长"),model.getResource(ontDaoUtils.getNS()+"心肌病"));
//        Set<Restriction> reSet=getRestriction(model,"引发","心包炎");
//        getSubject(reSet);
//        listRDFType(model,"于汇民");
//        OntClass ontClass=model.getOntClass(NS+"心包炎");
//
//        System.out.println(model.getRestriction(NS+"诱发")+"fdfd");
//        ExtendedIterator iterator=ontClass.listSuperClasses(false);
//        while(iterator.hasNext()){
//            OntClass f=(OntClass) iterator.next();
//            if(f.isRestriction()){
//                System.out.println("约束");
//                continue;
//            }
//            System.out.println("父类 "+f.getURI());
//        }


        //System.out.println("大小 "+iterator.toList().size());
    }
}
