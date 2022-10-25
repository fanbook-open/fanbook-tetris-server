package com.minghui.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.minghui.commons.constant.FanbookConstant;
import com.minghui.commons.constant.FanbookPostConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Base64Util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http请求工具类
 *
 * @Author 明辉
 * @Date 2021-06-11
 */
@Slf4j
public class HttpClientUtil {

    /**
     * 向Fanbook俄罗斯方块机器发送消息卡片的post请求
     *
     * @param URL
     * @param jsonStr
     * @return JSONObject
     */
    public static JSONObject sendMsgCardPost(String URL, String jsonStr) {
        String string=RestTemplateUtil.builder().post(URL, JSONObject.parseObject(jsonStr));
        return JSONObject.parseObject(string);
    }

    public static JSONObject sendMsgCardPost1(String URL, String jsonStr) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try {
            java.net.URL url = new URL(URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("Accept", "application/json");
            //获取输出流
            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(jsonStr);
            out.flush();
            out.close();
            //取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            } else {
                System.out.println("url:" + url);
                System.out.println("parm:" + jsonStr);
                System.out.println("conn:" + conn.getResponseMessage());

                System.out.println("ResponseCode is an error code:" + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return JSON.parseObject(result.toString());
    }


    /**
     * 向Fanbook获取access_token的post请求
     *
     * @param code
     * @return JSONObject
     */
    public static JSONObject getTokenPost(String code) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        // 创建Http Post请求
        String url = FanbookPostConstant.FANBOOK_GET_ACCESS_TOKEN_URL;
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
        httpPost.addHeader("authorization", "Basic " + Base64Util.encode(FanbookConstant.CLIENT_ID_AND_SECRET_KEY));
        System.out.println(FanbookConstant.CLIENT_ID_AND_SECRET_KEY);
        // 创建请求内容
        StringEntity stringEntity = null;

        try {
//            code=URLDecoder.decode(code, "utf-8");
//            stringEntity = new StringEntity("grant_type=" + FanbookConstant.GRANT_TYPE +
//                    "&code=" + URLDecoder.decode(code, "utf-8") +
//                    "&redirect_uri=" + FanbookPostConstant.REDIRECT_URL);
            stringEntity = new StringEntity("grant_type=" + FanbookConstant.GRANT_TYPE +
                    "&code=" + code +
                    "&redirect_uri=" + FanbookPostConstant.REDIRECT_URL);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        stringEntity.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(stringEntity);
        log.info("TokenPost url:"+url);
        log.info("TokenPost-authorization:"+"Basic " + Base64Util.encode(FanbookConstant.CLIENT_ID_AND_SECRET_KEY));
        log.info("TokenPost code:"+code);
        log.info("TokenPost redirect_uri:"+ FanbookPostConstant.REDIRECT_URL);

        // 执行http请求
        try {
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            return JSON.parseObject(resultString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向Fanbook获取用户信息的post请求
     *
     * @param URL
     * @param authorization
     * @return
     */
    public static JSONObject getUserInfoPost(String URL, String authorization) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try {
            java.net.URL url = new URL(URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("authorization", authorization);
            //获取输出流
            out = new OutputStreamWriter(conn.getOutputStream());
            out.flush();
            out.close();
            //取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            } else {
                System.out.println("ResponseCode is an error code:" + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return JSON.parseObject(result.toString());
    }
}
