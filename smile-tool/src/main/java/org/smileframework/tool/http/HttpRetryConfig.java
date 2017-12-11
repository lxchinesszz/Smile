package org.smileframework.tool.http;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.impl.client.HttpClients;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * @Package: smile.http
 * @Description: 重试机制
 * @link http://sharong.iteye.com/blog/2250777
 * @author: liuxin
 * @date: 2017/10/10 下午6:04
 */
public class HttpRetryConfig {
    private static int RETRY_COUNT = 5;
    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    private HttpRequestRetryHandler httpRequestRetryHandler;

    public HttpRetryConfig(PoolingHttpClientConnectionManager clientConnectionManager) {
        this(clientConnectionManager, null);
    }

    public HttpRetryConfig(PoolingHttpClientConnectionManager clientConnectionManager, HttpRequestRetryHandler retryHandler) {
        this.poolingHttpClientConnectionManager = clientConnectionManager;
        this.httpRequestRetryHandler = retryHandler;
    }



    public CloseableHttpClient buildRetryHttpClient() {
        if (httpRequestRetryHandler == null) {
            httpRequestRetryHandler = httpRequestRetryHandler();
        }
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setRetryHandler(httpRequestRetryHandler)
                .build();
        return httpClient;
    }

    public CloseableHttpClient buildRetryHttpClient(int retryCount) {
        if (httpRequestRetryHandler == null) {
            httpRequestRetryHandler = httpRequestRetryHandler(retryCount);
        }
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setRetryHandler(httpRequestRetryHandler)
                .build();
        return httpClient;
    }

    /**
     * 默认重试5秒
     *
     * @return
     */
    public static HttpRequestRetryHandler httpRequestRetryHandler(final int retryCount) {
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= retryCount) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return true;
                }
                if (exception instanceof SSLException) {// ssl握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return true;
            }
        };
        return httpRequestRetryHandler;
    }

    /**
     * 默认重试5秒
     *
     * @return
     */
    public static HttpRequestRetryHandler httpRequestRetryHandler() {
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= RETRY_COUNT) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return true;
                }
                if (exception instanceof SSLException) {// ssl握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return true;
            }
        };
        return httpRequestRetryHandler;
    }
}
