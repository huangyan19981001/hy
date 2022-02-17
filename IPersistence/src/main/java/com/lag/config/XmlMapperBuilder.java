package com.lag.config;

import com.lag.pojo.Configuration;
import com.lag.pojo.MappedStatement;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class XmlMapperBuilder {

    private  Configuration configuration;
    public XmlMapperBuilder(Configuration configuration) {

        this.configuration=configuration;
    }

   public void parse(InputStream in) throws DocumentException {
       System.out.println("InputStream in"+in);
        Document document = new SAXReader().read(in);
       /*得到userMapper.xml的根标签：<mapper>*/
       Element rootElement = document.getRootElement();
       String namespace = rootElement.attributeValue("namespace");
        List<Element> element = rootElement.elements();
       HashMap<String, MappedStatement> MappedStatementHashMap = new HashMap<String, MappedStatement>();
        for(Element elt: element){
            String id = elt.attributeValue("id");
            String parameterType = elt.attributeValue("parameterType");
            String resultType = elt.attributeValue("resultType");
            String sql = elt.getTextTrim();
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setParamType(parameterType);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSql(sql);
            //将实体类存到configuration中去
            String key=namespace+"."+id;

            MappedStatementHashMap.put(key,mappedStatement);
            configuration.setMappedStatementMap(MappedStatementHashMap);
            /*configuration.getMappedStatementMap().put(key,mappedStatement);*/
        }
             /*List <Element> mapperList= rootElement.selectNodes("insert");
            for(Element element:mapperList){
           String id =element.attribute("id").getValue();
           String parameterType = element.attribute("parameterType").getValue();
           System.out.println("parameterType:"+parameterType);
          *//* if(element.attribute("parameterType").getValue()==null||element.attribute("parameterType").getValue()==""){
               parameterType="";
           }else {
               parameterType=element.attribute("parameterType").getValue();
           }*//*
           String resultType = element.attribute("resultType").getValue();
           String sql = element.getTextTrim();
           System.out.println("sql:"+sql);
           MappedStatement mappedStatement = new MappedStatement();
           mappedStatement.setId(id);
           mappedStatement.setResultType(resultType);
           mappedStatement.setParamType(parameterType);
           mappedStatement.setSql(sql);
           String key=namespace+"."+id;
           System.out.println("mappedStatement:"+mappedStatement);
           System.out.println("key:"+key);
           HashMap<String, MappedStatement> MappedStatementHashMap = new HashMap<String, MappedStatement>();
           MappedStatementHashMap.put(key,mappedStatement);
           configuration.setMappedStatementMap(MappedStatementHashMap);
      }*/




   }



}
