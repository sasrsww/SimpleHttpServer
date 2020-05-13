/*
 * Copyright 2020 Zhongan All right reserved. This software is the
 * confidential and proprietary information of Zhongan  ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Zhongan.
 */
package com.sww;

import com.sww.mvc.annotation.RequestMapping;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * SimpleTest
 *
 * @author shenwenwen
 * @date 2020/4/28 22:17
 */
public class SimpleTest {
    public static final String BASE_PACKAGE = "com.sww";

    public static void main(String[] args) {
        //查找资源文件
        Set<String> properties = getResourceReflections(BASE_PACKAGE).getResources(Pattern.compile(".*\\.properties"));
        for(String s : properties){
            System.out.println(s);
        }

        //找出被指定注解类标注的所有接口、类
        Reflections reflections = getFullReflections(BASE_PACKAGE);
        Set<Class<?>> annotationSet = getFullReflections(BASE_PACKAGE).getTypesAnnotatedWith(RequestMapping.class);
        for(Class<?> clazz : annotationSet){
            System.out.println(clazz.getName());
            RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
            System.out.println(annotation.value());
        }
    }

    private static Reflections getResourceReflections(String basePackage){
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addUrls(ClasspathHelper.forPackage(basePackage));
        builder.setScanners(new ResourcesScanner());

        Reflections reflections = new Reflections(builder);
        return reflections;
    }

    /**
     * 如果没有配置scanner，默认使用SubTypesScanner和TypeAnnotationsScanner
     * @param basePackage 包路径
     */
    private static Reflections getFullReflections(String basePackage){
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addUrls(ClasspathHelper.forPackage(basePackage));
        builder.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(),
                new MethodAnnotationsScanner(), new FieldAnnotationsScanner());
        builder.filterInputsBy(new FilterBuilder().includePackage(basePackage));

        Reflections reflections = new Reflections(builder);
        return reflections;
    }
}
