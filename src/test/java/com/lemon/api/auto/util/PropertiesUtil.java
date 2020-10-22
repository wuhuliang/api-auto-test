package com.lemon.api.auto.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    public static Properties properties = new Properties();
    static {
        try {
           InputStream inStream = new FileInputStream(new File("src/test/resources/config.properties"));
           properties.load(inStream);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /***
     * 获取测试用例文件路径
     * @return
     */
    public static String getExcelPath(){
        return properties.getProperty("excel.path");
    }
}
