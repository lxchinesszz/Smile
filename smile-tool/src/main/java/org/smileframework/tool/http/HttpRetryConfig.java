package org.smileframework.tool.http;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;

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
        return HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setRetryHandler(httpRequestRetryHandler)
                .build();
    }

    public CloseableHttpClient buildRetryHttpClient(int retryCount) {
        if (httpRequestRetryHandler == null) {
            httpRequestRetryHandler = httpRequestRetryHandler(retryCount);
        }
        return HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setRetryHandler(httpRequestRetryHandler)
                .build();
    }

    /**
     * 默认重试5秒
     *
     * @return
     */
    public static HttpRequestRetryHandler httpRequestRetryHandler(final int retryCount) {
        return new SmileHttpRequestRetryHandler(retryCount);
    }

    /**
     * 默认重试5秒
     *
     * @return
     */
    public static HttpRequestRetryHandler httpRequestRetryHandler() {
        return new SmileHttpRequestRetryHandler(2);
    }
}
