package com.lemon.api.auto.pojo;

/***
 * 保存用例信息
 */
public class Case {
    private String caseId;
    private String apiId;
    private String desc;
    private String params;
    private String expectedResponseData;
    private String actualResponseData;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getExpectedResponseData() {
        return expectedResponseData;
    }

    public void setExpectedResponseData(String expectedResponseData) {
        this.expectedResponseData = expectedResponseData;
    }

    public String getActualResponseData() {
        return actualResponseData;
    }

    public void setActualResponseData(String actualResponseData) {
        this.actualResponseData = actualResponseData;
    }

    @Override
    public String toString(){
        return "caseId="+caseId+",apiId="+apiId+",desc="+desc+",params="+params
                +",expectedResul="+expectedResponseData+",actualResult="+actualResponseData;
    }


}
