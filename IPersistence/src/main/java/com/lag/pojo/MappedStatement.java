package com.lag.pojo;

//每一个select标签都会封装成一个MappedStatement(容器对象)
public class MappedStatement {

    //id标识
    private String id;
    //返回值类型
    private  String resultType;
    //参数值类型
    private  String  paramType;
    //sql语句
    private String sql;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
