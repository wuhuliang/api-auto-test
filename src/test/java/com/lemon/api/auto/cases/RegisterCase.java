package com.lemon.api.auto.cases;

import com.lemon.api.auto.util.CaseUtil;
import org.testng.annotations.DataProvider;

public class RegisterCase extends BaseProcessor{

    /***
     * 接口参数为json字符串
     * @return
     */
    @DataProvider
    public Object[][] datas() {
        String apiId = "2";
        Object[][] datas = CaseUtil.getCaseDataByApiId(apiId,cellnames);
        return datas;
    }
}
