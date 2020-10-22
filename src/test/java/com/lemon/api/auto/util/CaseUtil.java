package com.lemon.api.auto.util;

import com.lemon.api.auto.pojo.Case;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/***
 * Case用例工具类
 */
public class CaseUtil {
    /***
     * 保存所有用例对象（静态类型，放在内存中共享数据）
     */
    public static List<Case> cases = new ArrayList<Case>();

    static{
        //将所有数据解析封装到Cases
        String excelPath = PropertiesUtil.getExcelPath();
        String sheetName = "用例";
        ExcelUtil.load(excelPath,sheetName, Case.class);
    }

    /***
     *根据接口编号获取对应接口的测试数据
     * @param apiId 指定接口编号
     * @param cellNames  获取数据对应的列名
     * @return
     */
    public static Object[][] getCaseDataByApiId(String apiId, String[] cellNames){
        Class<Case> clazz = Case.class;
        //保存指定接口编号的case对象
        ArrayList<Case> csList = new ArrayList<Case>();
        //通过循环找出指定编号对应的用例数据
        for (Case cs:cases) {
            //循环处理,通过指定appid匹配用例对象中的appId
            if (cs.getApiId().equals(apiId)){
                csList.add(cs);
            }
        }
        Object[][] datas = new Object[csList.size()][cellNames.length];
        //处理csList
        for (int i = 0; i < csList.size(); i++) {
            Case cs = csList.get(i);
            for (int j = 0; j < cellNames.length ; j++) {
                //要反射的方法名
                String methodName = "get"+cellNames[j];
                try {
                    Method method = clazz.getMethod(methodName);
                    String value = (String) method.invoke(cs);
                    datas[i][j] = value;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return datas;
    }


}
