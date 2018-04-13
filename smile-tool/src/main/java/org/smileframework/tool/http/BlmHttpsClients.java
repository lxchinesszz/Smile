package org.smileframework.tool.http;

import org.apache.commons.httpclient.HttpException;
import org.apache.http.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smileframework.tool.json.JsonUtils;
import javax.net.ssl.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Package: safety.bankpay.util
 * @Description: 支付https连接
 * @author: liuxin
 * @date: 2017/11/15 下午2:21
 */
public class BlmHttpsClients {
    private static final Logger logger = LoggerFactory.getLogger(BlmHttpsClients.class);


    public static String sendByBody(String url, String body,boolean isPrintLog) throws Exception {
        CloseableHttpClient httpClient = HttpsBuilder.custom().createIgnoreVerifySSLHttpsClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(body));
        String result = "";
        CloseableHttpResponse response = null;
        Integer httpCode = 0;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                httpCode = response.getStatusLine().getStatusCode();
            }
        } catch (SocketTimeoutException e) {
            logger.error("连接超时" + e.toString());
        } catch (HttpException e) {
            logger.error("读取外部服务器数据失败" + e.toString());
        } catch (UnknownHostException e) {
            logger.error("请求的主机地址无效" + e.toString());
        } catch (IOException e) {
            logger.error("订单详情查询请求数据失败" + e.toString());
        } finally {
            httpPost.releaseConnection();
        }
        if (isPrintLog){
            logger.info("发送信息:{},返回:{},请求地址:{},请求状态:{}", body,result, url, httpCode);
        }
        return result;
    }

    public static String sendByBody(String url, String body, String charset,boolean isPrintLog) throws Exception {
        CloseableHttpClient httpClient = HttpsBuilder.custom().createIgnoreVerifySSLHttpsClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(body, charset));
        String result = "";
        CloseableHttpResponse response = null;
        Integer httpCode = 0;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, charset);
                httpCode = response.getStatusLine().getStatusCode();
            }
        } catch (SocketTimeoutException e) {
            logger.error("连接超时" + e.toString());
        } catch (HttpException e) {
            logger.error("读取外部服务器数据失败" + e.toString());
        } catch (UnknownHostException e) {
            logger.error("请求的主机地址无效" + e.toString());
        } catch (IOException e) {
            logger.error("订单详情查询请求数据失败" + e.toString());
        } finally {
            httpPost.releaseConnection();
        }
        if (isPrintLog){
            logger.info("发送信息:{},返回:{},请求地址:{},请求状态:{}", body,result, url, httpCode);
        }
        return result;
    }

    /**
     * get查询
     *
     * @param url
     * @return
     */
    public static String sendByGet(String url,boolean isPrintLog) {
        CloseableHttpClient httpClient = HttpsBuilder.custom().createIgnoreVerifySSLHttpsClient();
        HttpGet getMethod = new HttpGet(url);
        String result = "";
        CloseableHttpResponse response = null;
        Integer httpCode = 0;
        try {
            response = httpClient.execute(getMethod);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                httpCode = response.getStatusLine().getStatusCode();
            }
        } catch (SocketTimeoutException e) {
            logger.error("连接超时" + e.toString());
        } catch (HttpException e) {
            logger.error("读取外部服务器数据失败" + e.toString());
        } catch (UnknownHostException e) {
            logger.error("请求的主机地址无效" + e.toString());
        } catch (IOException e) {
            logger.error("订单详情查询请求数据失败" + e.toString());
        } finally {
            getMethod.releaseConnection();
        }
        if (isPrintLog){
            logger.info("发送信息:{},返回:{},请求地址:{},请求状态:{}", url,result, url, httpCode);
        }
        return result;
    }

    /**
     * 调用请求方地址
     *
     * @param url         接入方地址
     * @param requestJson 请求体
     * @return
     */
    public static String sendByJson(String url, String requestJson,String charset,boolean isPrintLog) {
        CloseableHttpClient httpClient = HttpsBuilder.custom().createIgnoreVerifySSLHttpsClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json;charset="+charset);
        int httpCode = -1;
        String result = "";
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new StringEntity(requestJson,charset));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                httpCode = response.getStatusLine().getStatusCode();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (isPrintLog){
            logger.info("发送信息:{},返回:{},请求地址:{},请求状态:{}", requestJson,result, url, httpCode);
        }  return result;
    }

    /**
     * 调用请求方地址
     *
     * @param url         接入方地址
     * @param requestJson 请求体
     * @return
     */
    public static String sendByJson(String url, String requestJson,boolean isPrintLog) {
        return sendByJson(url,requestJson,"utf-8",isPrintLog);
    }

    public static String sendByFDForm(String url, Map<String, String> paramMap) {
        SSLContext ignoreVerifySSL = HttpsBuilder.createIgnoreVerifySSL();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ignoreVerifySSL, new String[] { "TLSv1.2"},
                null, new HostnameVerifier(){
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }

        });
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslsf)
                .build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(2000);//max connection

        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
                .setConnectionManager(cm).build();
//        CloseableHttpClient httpClient =  HttpsBuilder.custom().setRegistry(registry).createHttpsClient();
        HttpPost httpPost = new HttpPost(url);
        String result = "";
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> param : paramMap.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        CloseableHttpResponse response = null;
        int httpCode = -1;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                httpCode = response.getStatusLine().getStatusCode();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        logger.info("发送信息:{},返回:{},请求地址:{},请求状态:{}", JsonUtils.toJson(paramMap), result, url, httpCode);
        return result;
    }

    /**
     * 调用请求方地址
     *
     * @param url      接入方地址
     * @param paramMap 请求体
     * @return
     */
    public static String sendByForm(String url, Map<String, String> paramMap) {
        CloseableHttpClient httpClient = HttpsBuilder.custom().createIgnoreVerifySSLHttpsClient();
        HttpPost httpPost = new HttpPost(url);
        String result = "";
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> param : paramMap.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        CloseableHttpResponse response = null;
        int httpCode = -1;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                httpCode = response.getStatusLine().getStatusCode();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        logger.info("发送信息:{},返回:{},请求地址:{},请求状态:{}", JsonUtils.toJson(paramMap), result, url, httpCode);
        return result;
    }
}
