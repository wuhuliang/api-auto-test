package com.lemon.api.auto.util;

import com.lemon.api.auto.pojo.RestApi;
import com.lemon.api.auto.util.ExcelUtil;

import java.util.ArrayList;
import java.util.List;

public class RestUtil {
    /***
     * 保存所有接口对象（静态类型，放在内存中共享数据）
     */
    public static List<RestApi> rests = new ArrayList<RestApi>();

    static {
        String excelPath = "src\\test\\resources\\cases_v7.xlsx";
        String sheetName = "接口信息";
        ExcelUtil.load(excelPath,sheetName, RestApi.class);
    }

    /***
     * 根据接口编号获取接口地址
     * @param apiId
     * @return
     */
    public static String getUrlByApiId(String apiId) {
        for (RestApi rest:rests) {
            if (rest.getApiId().equals(apiId)){
                return rest.getUrl();
            }
        }
        return "apiId"+apiId+"没有url值";
    }

    /***
     * 根据接口编号获取接口请求类型
     * @param apiId
     * @return
     */
    public static String getTypeByApiId(String apiId) {
        for (RestApi rest:rests) {
            if (rest.getApiId().equals(apiId)){
                return rest.getType();
            }
        }
        return "apiId"+apiId+"没有type值";
    }
}
