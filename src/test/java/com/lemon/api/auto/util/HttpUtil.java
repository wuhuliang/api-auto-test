package com.lemon.api.auto.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.*;

/***
 * 接口调用工具类
 */
public class HttpUtil {
    public static Map<String, String> cookies = new HashMap<String, String>();

    /***
     * 以POST方式处理接口调用
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, Map<String, String> params){
        //准备请求方式：POST
        HttpPost postTest = new HttpPost(url);
        //准备请求参数：parameters
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        //取出map中所有参数名
        Set<String> keys = params.keySet();
        //通过循环将参数保存到list集合
        for (String name:keys) {
            String value = params.get(name);
            parameters.add(new BasicNameValuePair(name,value));
        }

        String reMessage = "";
        try {
            HttpEntity httpEntity = new UrlEncodedFormEntity(parameters, "utf-8");
            //把请求参数传递进去
            postTest.setEntity(httpEntity);
            //创建client,准备用来发请求
            HttpClient client = HttpClients.createDefault();
            //请求前把cookie添加到header
            addCookieInRequestHeaderBeforeRequest(postTest);
            //发出http请求
            HttpResponse result = client.execute(postTest);
            //取出响应头的header
            getAndStoreCookiesFromResponseHeader(result);
            //处理响应数据
            int code = result.getStatusLine().getStatusCode();
            reMessage = EntityUtils.toString(result.getEntity());
            System.out.println("code=【"+code+"】,result=【"+reMessage+"】");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reMessage;
    }


    /***
     * 以GET方式处理接口调用
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, String> params){
        Set<String> keys = params.keySet();
        //定义标志位
        int mark = 1;
        for (String name:keys) {
            //第一个参数使用“？”拼接
            if (mark ==1){
                url+=("?"+name+params.get(name));
            }else{
                //后面的参数用“&”拼接
                url+=("&"+name+params.get(name));
            }
            mark++;
        }
        System.out.println("url:"+url);
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse result = null;
        String message = "";
        try {
            addCookieInRequestHeaderBeforeRequest(httpGet);
            result = httpClient.execute(httpGet);
            getAndStoreCookiesFromResponseHeader(result);
            int code= result.getStatusLine().getStatusCode();
            System.out.println(code);
            message = EntityUtils.toString(result.getEntity());
            System.out.println("code=【"+code+"】,result=【"+message+"】");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    public static String doService(String url, String type, Map<String,String> params){
        String result = "";
        if (type.equals("post")){
            result = HttpUtil.doPost(url,params);
        }else if (type.equals("get")){
            result = HttpUtil.doGet(url,params);
        }
        return result;
    }

    private static void addCookieInRequestHeaderBeforeRequest(HttpRequest httpRequest) {
        String kmgSession = cookies.get("kmgSession");
        if (kmgSession!=null){
            httpRequest.addHeader("Cookie",kmgSession);
        }
    }

    /***
     * 保存获取header里的cookie
     * @param httpResponse
     */
    private static void getAndStoreCookiesFromResponseHeader(HttpResponse httpResponse) {
        //从响应头里取出名字为“Set-Cookie”的响应头
        Header setCookieHeader = httpResponse.getFirstHeader("Cookie");
        //如果不为空，进行处理
        if (setCookieHeader!=null){
            String cookiePairsString = setCookieHeader.getValue();
            if (cookiePairsString!=null&&cookiePairsString.trim().length()>0){
                //以“；”分割，并且把所有取出的cookie保存在一个字符串列表中
                String[] cookiePair = cookiePairsString.split(";");
                if (cookiePair!=null){
                    for (String cookie: cookiePair) {
                        //如果包含kmgSession，则意味着响应头里有会话ID数据
                        if (cookie.contains("kmgSession")){
                            //把需要的header保存到map
                            cookies.put("kmgSession",cookie);
                        }
                    }
                }
            }
        }

    }
}
