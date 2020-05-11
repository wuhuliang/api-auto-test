package com.lemon.api.auto.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JDBCUtil {

    public static Properties properties = new Properties();

    static {
        System.out.println("开始解析properties文件");
        InputStream inStream = null;
        try {
            inStream = new FileInputStream(new File("src/test/resources/jdbc.properties"));
            properties.load(inStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /***
     * 根据sql查询表数据，并以map返回，key为字段名，value为字段值
     * @param sql
     * @param values 条件字段的值
     * @return
     * @throws Exception
     */
    public static Map<String, Object> query(String sql, Object ... values) { //可变参数

        Map<String, Object> columnLableAndValues = new HashMap<String, Object>();
        try{
            //1.根据连接信息，获得数据库连接（连上数据库）
            Connection connection = getConnection();
            //2.获取PreparedStatement对象（此类型的对象有提供数据库操作方法）
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //3.设置条件字段的值（实时绑定）
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            //4.调用查询方法，执行查询，返回ResultSet（结果集）
            ResultSet resultSet = preparedStatement.executeQuery();
            //获取查询信息，得到查询字段的数量
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            //5.从结果集取查询数据
            columnLableAndValues = new HashMap<String, Object>();
            while (resultSet.next()) {
                //循环取出每个查询字段的值
                for (int i = 1; i <= columnCount; i++) {
                    String columnLable = metaData.getColumnLabel(i);
                    String columnValue = resultSet.getObject(columnLable).toString();
                    columnLableAndValues.put(columnLable, columnValue);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return columnLableAndValues;
    }

    /***
     * 根据连接信息，获得数据库连接（连上数据库）
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        //从properties中取数据库连接信息
        String url = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.user");
        String password = properties.getProperty("jdbc.password");
        return DriverManager.getConnection(url,user,password);
    }
}
