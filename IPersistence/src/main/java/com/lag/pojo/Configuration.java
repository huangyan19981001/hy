package com.lag.pojo;

import javax.sql.DataSource;
import java.util.Map;

//数据库配置信息容器
public class Configuration {
    private DataSource dataSource;
    /*key:statementId value:封装好MappedStatement对象*/
    private Map<String,MappedStatement> MappedStatementMap;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*map的键是由userMapper.xml中的id,id=namespace+id*/
    public Map<String, MappedStatement> getMappedStatementMap() {
        return MappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
        MappedStatementMap = mappedStatementMap;
    }



}
