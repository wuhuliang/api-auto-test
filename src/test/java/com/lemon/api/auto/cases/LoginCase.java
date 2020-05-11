package com.lemon.api.auto.cases;

import com.lemon.api.auto.util.CaseUtil;
import org.testng.annotations.DataProvider;

public class LoginCase extends BaseProcessor{

    /***
     * 接口参数为json字符串
     * @return
     */
    @DataProvider
    public Object[][] datas() {
        String apiId = "1";
        Object[][] datas = CaseUtil.getCaseDataByApiId(apiId,cellnames);
        return datas;
    }

}
