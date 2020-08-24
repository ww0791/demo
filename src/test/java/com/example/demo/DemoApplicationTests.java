package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.MyEntity;
import com.example.demo.schedule.MyfirstSchedule;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@SpringBootTest("DemoApplication")
class DemoApplicationTests {

    @Autowired
    JavaMailSenderImpl javaMailSender;

    @Test
    @Scheduled(cron = "0/59 * * * * ? ")
    void contextLoads() {
        SimpleMailMessage msg=new SimpleMailMessage();
        msg.setSubject("测试主题");
        msg.setText("测试内容");
        msg.setTo("1430365719@qq.com");
        msg.setFrom("wwjx0791@163.com");
        javaMailSender.send(msg);
        System.out.println("发送成功");
    }

    @Test
    void test(){
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String mailNo="9862120028918";
        // 参数
        StringBuffer params = new StringBuffer();
        // 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
        params.append("mailNo="+mailNo+"&spellName=&exp-textName=&tk=312ebcf5&tm=1598243961139&callback=_jqjsp&_1598243961140=");

        // 创建Get请求
        HttpGet httpGet = new HttpGet("https://biz.trace.ickd.cn/auto/" + mailNo+"?" + params);
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
                String s = EntityUtils.toString(responseEntity);
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + s);
                System.out.println(s.replace("_jqjsp(","").replace(")",""));
                System.out.println(JSONObject.parseObject(s.replace("_jqjsp(","").replace(")","").replace(";",""),MyEntity.class));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
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

    }

}
