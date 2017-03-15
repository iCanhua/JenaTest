package com.scut.fan.fnlp;
import org.fnlp.nlp.cn.CNFactory;
import org.fnlp.nlp.parser.dep.DependencyTree;
import org.fnlp.util.exception.LoadModelException;

import java.util.HashMap;

/**
 * Created by FAN on 2017/2/22.
 */
public class test {
    public static void main(String[] args) throws LoadModelException {
        // 创建中文处理工厂对象，并使用“models”目录下的模型文件初始化
        CNFactory factory = CNFactory.getInstance("models");
        DependencyTree dependencyTree=factory.parse2T("眼睛感觉疼痛，耳朵发现流血");
        System.out.println(dependencyTree.toString());
        System.out.println(dependencyTree.getTypes());




    }
}
