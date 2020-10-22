package com.lemon.api.auto.cases;

import com.alibaba.fastjson.JSONObject;
import com.lemon.api.auto.pojo.WriteBackData;
import com.lemon.api.auto.util.*;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import java.util.Map;

/***
 * 接口测试统一处理类
 */
public class BaseProcessor {
    //每个测试类传入的值都一样，提取到父类中
    public String cellnames[] = {"CaseId","ApiId","Params","ExpectedResponseData","PreValidateSql","AfterValidateSql"};

    @Test(dataProvider = "datas")
    public void test1(String caseId, String apiId, String parameter, String expectedResponseData,
                      String preValidateSql, String afterValidateSql){ //{"Username":"test1@qq.com","Password":"123"}
        //在接口调用前查询想要验证的字段
        if (preValidateSql!=null&&preValidateSql.trim().length()>0){
            String preValidataResult = DBCheckUtil.doQuery(preValidateSql);
            ExcelUtil.writeBackDatas.add(new WriteBackData("用例",caseId,"PreValidateResult",preValidataResult));
        }
        //url
        String url = RestUtil.getUrlByApiId(apiId);
        //type
        String type = RestUtil.getTypeByApiId(apiId);
        //接口参数,通过解析json字符串，强制转换为Map<String,String>类型
        Map<String,String> params = (Map<String, String>) JSONObject.parse(parameter);
        //调用doService方法完成接口调用，拿到接口响应数据
        String actualResponseData = HttpUtil.doService(url,type,params);
        //断言 期望结果比较实际结果
        actualResponseData = AssertUtil.assertEquals(actualResponseData, expectedResponseData);
        //保存回写数据对象
        ExcelUtil.writeBackDatas.add(new WriteBackData("用例",caseId,"ActualResponseData",actualResponseData));
        //在接口调用后查询想要验证的字段
        if (afterValidateSql!=null&&afterValidateSql.trim().length()>0){
            String afterValidataResult = DBCheckUtil.doQuery(afterValidateSql);
            ExcelUtil.writeBackDatas.add(new WriteBackData("用例",caseId,"AfterValidateResult",afterValidataResult));
        }
    }

    @AfterSuite
    public void batchWriteBackDatas(){
            ExcelUtil.batchWriteBackDatas(PropertiesUtil.getExcelPath());
    }

}
