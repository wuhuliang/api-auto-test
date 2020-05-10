package com.lemon.api.auto.cases;

import com.lemon.api.auto.util.CaseUtil;
import org.testng.annotations.DataProvider;

public class RechargeCase extends BaseProcessor{
    @DataProvider
    public Object[][] datas() {
        String apiId = "4";
        String cellnames[] = {"CaseId","ApiId","Params","ExpectedResponseData"};
        Object[][] datas = CaseUtil.getCaseDataByApiId(apiId,cellnames);
        return datas;
    }
}
