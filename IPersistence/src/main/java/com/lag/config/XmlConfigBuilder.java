package com.lag.config;

import com.lag.io.Resources;
import com.lag.pojo.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XmlConfigBuilder {
    private Configuration configuration;
    public XmlConfigBuilder(){

        this.configuration=new Configuration();
    }


 /*该方法就是使用dom4j对配置文件进行解析，封装Configuration*/
    public Configuration parseConfig(InputStream in) throws DocumentException, PropertyVetoException {
        Document document = new SAXReader().read(in);
        //sqlMapConfig.xml的根目录<configuration>
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for(Element element:list){
            String name = element.attribute("name").getValue();
            String value = element.attribute("value").getValue();
            properties.setProperty(name, value);

        }
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(comboPooledDataSource);

        //对userMapper.xml解析：拿到路径--转成字节输入流--dom4j进行解析；
        List <Element> mapperList = rootElement.selectNodes("//mapper");
        for(Element mapperElement:mapperList){
            Attribute resource = mapperElement.attribute("resource");
            String mapperPath = resource.getValue();
            InputStream resourceAsSteam = Resources.getResourceAsSteam(mapperPath);
            System.out.println("userMapperResourceAsSteam"+resourceAsSteam);
            XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
            xmlMapperBuilder.parse(resourceAsSteam);
        }

        return configuration;
    }



}
