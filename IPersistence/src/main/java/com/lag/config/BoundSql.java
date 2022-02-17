package com.lag.config;

import com.lag.utils.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

public class BoundSql {
    //1.对转换之后的sql语句进行存放
    //2.对解析过程中对#{}解析出来里面的参数名称进行存放
    private  String SqlText;
    private List<ParameterMapping> parameterMappingList=new  ArrayList();

    public BoundSql(String sqlText, List<ParameterMapping> parameterMappingList) {
        this.SqlText = sqlText;
        this.parameterMappingList = parameterMappingList;
    }

    public String getSqlText() {
        return SqlText;
    }

    public void setSqlText(String sqlText) {
        SqlText = sqlText;
    }

    public List<ParameterMapping> getParameterMappingList() {
        return parameterMappingList;
    }

    public void setParameterMappingList(List<ParameterMapping> parameterMappingList) {
        this.parameterMappingList = parameterMappingList;
    }


}
