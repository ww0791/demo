package com.example.demo.until;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.MyEntity;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;

import java.text.ParseException;

public class HttpClientUntil {

    public static CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    public static <T>Object get(String url, Class<T> t){
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)

        //String mailNo="9862120028918";
        // 参数
       // StringBuffer params = new StringBuffer();
        // 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
       // params.append("mailNo="+mailNo/*+"&spellName=&exp-textName=&tk=312ebcf5&tm=1598243961139&callback=_jqjsp&_1598243961140="*/);

        // 创建Get请求
       // HttpGet httpGet = new HttpGet("https://biz.trace.ickd.cn/auto/" + mailNo+"?" + params);
        String s = null;
        HttpGet httpGet = new HttpGet(url);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间(单位毫秒)
                    .setConnectTimeout(5000)
                    // 设置请求超时时间(单位毫秒)
                    .setConnectionRequestTimeout(5000)
                    // socket读写超时时间(单位毫秒)
                    .setSocketTimeout(5000)
                    // 设置是否允许重定向(默认为true)
                    .setRedirectsEnabled(true).build();

            // 将上面的配置信息 运用到这个Get请求里
            httpGet.setConfig(requestConfig);

            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);

            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                s = EntityUtils.toString(responseEntity);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(Strings.isNotEmpty(s)){
           return JSONObject.parseObject(s,t);
        }
        return null;
    }
}
