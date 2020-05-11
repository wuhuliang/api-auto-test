package com.lemon.api.auto.pojo;

import java.util.Map;

/***
 * 数据库查询结果实体类
 */
public class DBQueryResult {
    /***
     * 查询语句脚本编号
     */
    private String no;
    /***
     * 脚本查询到数据，保存到map中（key保存字段名，value保存对应字段查到的数据）
     */
    private Map<String, Object> columnLableAndValues;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Map<String, Object> getColumnLableAndValues() {
        return columnLableAndValues;
    }

    public void setColumnLableAndValues(Map<String, Object> columnLableAndValues) {
        this.columnLableAndValues = columnLableAndValues;
    }
}
