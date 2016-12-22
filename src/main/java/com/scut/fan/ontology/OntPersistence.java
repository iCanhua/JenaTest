package com.scut.fan.ontology;

import org.apache.jena.ontology.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * Created by FAN on 2016/12/21.
 */
public class OntPersistence {
    static String NS=ontDaoUtils.getNS();
    public static boolean addRestrictionAsSuper(OntModel model,String ontClasslocalName,String restrictedPro,String restrictedType,String restrictedFilter){
        boolean mark=true;
        //处理restrictedPro和restrictedFilter
        OntProperty property=model.getObjectProperty(NS+restrictedPro);
        OntClass filter=model.getOntClass(NS+restrictedFilter);
        if(null==property||null==filter){
            mark=false;
            throw new RuntimeException("该property和filter在对应的model中不存在！请检查");
        }
        System.out.println("构造约束的材料完毕："+property.getLocalName()+"  "+filter.getLocalName());
        Restriction restriction=model.createRestriction(property);
        //处理restrictedType，并完整约束
        if("some".equals(restrictedType)){
           restriction.convertToSomeValuesFromRestriction(filter);
        }else if("only".equals(restrictedType)){
           restriction.convertToAllValuesFromRestriction(filter);
        }else{
            mark=false;
            throw new RuntimeException("请输入正确的restrictedType或者不兼容该restrictedType");
        }
        //处理ontClasslocalName
        OntClass ont=model.getOntClass(NS+ontClasslocalName);
        if(null==ont){
            mark=false;
            //throw new RuntimeException("该类:"+ont.getLocalName()+" 在对应的model中不存在！请检查");
        }
        if(mark) restriction.setSubClass(ont);
        return mark;
    }

    public static void main(String[] args) {
        //task_1();

    }

    public static void task_1(){
        OntModel model=ontDaoUtils.getModel();
        Iterator it=model.listClasses();
        int count=0;
        while (it.hasNext()){
            OntClass ontClass=(OntClass) it.next();
            if(ontClass.hasSuperClass(model.getOntClass(NS+"症状"))){
                boolean success1=addRestrictionAsSuper(model,ontClass.getLocalName(),"是症状","some","心内科");
                boolean success2=addRestrictionAsSuper(model,ontClass.getLocalName(),"是症状","some","心外科");
                count++;
                // System.out.println("成功与否："+success1+" "+success2);

            }
            if(count==119){
                break;
            }
        }
        try {
            OutputStream out=new FileOutputStream("C:\\Users\\FAN\\Desktop\\医院项目\\医生推荐系统\\本体库及工具\\本体库\\正式本体库\\master\\11.25(write).owl");
            model.write(out,"RDF/XML");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void task_2(){

    }
}


