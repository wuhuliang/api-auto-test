package com.lemon.api.auto.util;

import org.testng.Assert;

public class AssertUtil {
    /***
     * 自定义类库：断言比较实际结果跟期望值是否一致
     * @param actualResponseData
     * @param expectedResponseData
     */
    public static String assertEquals(String actualResponseData, String expectedResponseData) {
        String result = "通过";
        try {
            Assert.assertEquals(actualResponseData, expectedResponseData);
        } catch (Throwable e){
            result = actualResponseData;
        }
        return result;

    }
}
