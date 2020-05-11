package com.lemon.api.auto.util;

import com.alibaba.fastjson.JSONObject;
import com.lemon.api.auto.pojo.DBChecker;
import com.lemon.api.auto.pojo.DBQueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBCheckUtil {
    /***
     * 根据脚本执行查询并返回查询结果
     * @param validateSql
     * @return
     */
    public static String doQuery(String validateSql) {
        //将脚本字符串封装成对象
        List<DBChecker> dbCheckers = JSONObject.parseArray(validateSql, DBChecker.class);
        //准备列表，用来封装查询结果对象
        List<DBQueryResult> dbQueryResults = new ArrayList<DBQueryResult>();
        //循环遍历，取出sql脚本执行
        for (DBChecker dbChecker:dbCheckers) {
            //拿到sql编号和脚本
            String no = dbChecker.getNo();
            String sql = dbChecker.getSql();
            //执行查询，获取到结果
            Map<String, Object> columnLableAndValues = JDBCUtil.query(sql);
            //获取的结果封装到DBQueryResult实体类对象
            DBQueryResult dbQueryResult = new DBQueryResult();
            dbQueryResult.setNo(no);
            dbQueryResult.setColumnLableAndValues(columnLableAndValues);
            //把对象封装进列表，该列表是一个Json格式字符串对象组成
            dbQueryResults.add(dbQueryResult);
        }
        return JSONObject.toJSONString(dbQueryResults);//利用反序列化技术，把列表转化为json数组
    }

    public static void main(String[] args) {
        String validateSql = "[{\"no\":\"1\",\"sql\":\"SELECT COUNT(sage) FROM student WHERE sage=19;\"}" +
                "{\"no\":\"2\",\"sql\":\"SELECT COUNT(sage) FROM student WHERE sage=18;\"}]";
        String result = doQuery(validateSql);
        System.out.println(result);

    }
}
