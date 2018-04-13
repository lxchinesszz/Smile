package org.smileframework.tool.http;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @Package: smile.http
 * @Description: 参考文档: http://blog.csdn.net/daguairen/article/details/52442149
 * https构建,
 * 客户端的TrustStore文件。客户端的TrustStore文件中保存着被客户端所信任的服务器的证书信息。
 * 客户端在进行SSL连接时，JSSE将根据这个文件中的证书决定是否信任服务器端的证书。
 * 1. 一种解决这个问题的方法是按照信任管理器的处理规则，把站点的证书放到证书库文件jssecacerts中
 * java InstallCert msptest.srcb.com:537
 * 2. 证书信任管理器类就是实现了接口X509TrustManager的类。我们可以自己实现该接口，让它信任我们指定的证书。
 * @author: liuxin
 * @date: 2017/11/25 下午2:14
 */
public class HttpsBuilder {
    private static PoolingHttpClientConnectionManager connMgr;

    private static RequestConfig requestConfig;

    private HttpsBuilder() {
    }

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(7000);
        // 设置读取超时
        configBuilder.setSocketTimeout(7000);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(700);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }

    public static HttpsBuilder custom() {
        return new HttpsBuilder();
    }

    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSLv3");
            // 用于对证书的校验
            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager trustManager = new X509TrustManager() {
                //该方法检查客户端的证书，若不信任该证书则抛出异常。由于我们不需要对客户端进行认证，
                // 因此我们只需要执行默认的信任管理器的这个方法。
                @Override
                public void checkClientTrusted(
                        X509Certificate[] paramArrayOfX509Certificate,
                        String paramString) throws CertificateException {
                }

                //该方法检查服务器的证书，若不信任该证书同样抛出异常,若空实现,即信任所有服务器证书
                @Override
                public void checkServerTrusted(
                        X509Certificate[] paramArrayOfX509Certificate,
                        String paramString) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sc.init(null, new TrustManager[]{trustManager}, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sc;
    }


    public HttpsBuilder setIgnoreVerifySSL(SSLContext sslContext) {
        return setIgnoreVerifySSL(sslContext, "TLSv1.2");
    }

    public HttpsBuilder setIgnoreVerifySSL(SSLContext sslContext, String TLSProtocol) {
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[]{TLSProtocol},
                null, new HostnameVerifier() {
            //允许https
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }

        });
        Registry<ConnectionSocketFactory> socketFactoryRegistry
                = RegistryBuilder.<ConnectionSocketFactory>create().register("http",
                PlainConnectionSocketFactory.INSTANCE).register("https",
                sslsf).build();
        connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        return this;
    }


    /**
     * 默认 .setRetryHandler(httpRequestRetryHandler)
     *
     * @return
     */
    public CloseableHttpClient createHttpsClient() {
        return HttpClients.custom().setConnectionManager(connMgr).build();
    }

    /**
     * 设置重试次数
     *
     * @param retryCount
     * @return
     */
    public CloseableHttpClient createHttpsClient(final int retryCount) {
        HttpRequestRetryHandler httpRequestRetryHandler = HttpRetryConfig.httpRequestRetryHandler(retryCount);
        return HttpClients.custom().setConnectionManager(connMgr).setRetryHandler(httpRequestRetryHandler).build();
    }

    /**
     * 设置忽略检查证书
     *
     * @return
     */
    public CloseableHttpClient createIgnoreVerifySSLHttpsClient(final int retryCount) {
        setIgnoreVerifySSL(createIgnoreVerifySSL());
        HttpRequestRetryHandler httpRequestRetryHandler = HttpRetryConfig.httpRequestRetryHandler(retryCount);
        return HttpClients.custom().setConnectionManager(connMgr).setRetryHandler(httpRequestRetryHandler).build();
    }

    /**
     * 设置忽略检查证书
     *
     * @return
     */
    public CloseableHttpClient createIgnoreVerifySSLHttpsClient() {
        setIgnoreVerifySSL(createIgnoreVerifySSL());
        return HttpClients.custom().setConnectionManager(connMgr).build();
    }

    public static void main(String[] args) throws Exception {
        /**
         *
         * http://blog.csdn.net/daguairen/article/details/52442149
         * 接口层:具体实现由X509TrustManager实现,如果空方法,就不验证证书
         * 1.首先创建SSLContext
         * 2.将SSLContext放入到ConnectionSocketFactory
         * 3.创建连接管理器,并可以设置一些参数
         * 3.生成http客户端
         */
        SSLContext sslContext = null;
        /**
         *sslContext放入sslSocketFactory
         */
        SSLSocketFactory sslSocketFactory = null;

        Registry<ConnectionSocketFactory> socketFactoryRegistry
                = RegistryBuilder.<ConnectionSocketFactory>create().register("http",
                PlainConnectionSocketFactory.INSTANCE).register("https",
                new SSLConnectionSocketFactory(sslContext)).build();

        /**
         * 构建连接管理器
         */
        PoolingHttpClientConnectionManager connManager
                = new PoolingHttpClientConnectionManager(socketFactoryRegistry);


        /*****************************************/
        /**************创建Client使用API***********/
        /*****************************************/

        /**
         * 构建一个安全的http请求客户端
         */
        HttpsBuilder.custom().createHttpsClient();

        /**
         * 构建一个安全的http请求客户端,具有失败重试的功能
         */
        HttpsBuilder.custom().createHttpsClient(3);

        /**
         * 构建一个自己实现验证规则的客户端
         */
        HttpsBuilder.custom().setIgnoreVerifySSL(createIgnoreVerifySSL()).createHttpsClient();

        /**
         * 直接构建一个忽略安全的http请求客户端
         */
        HttpsBuilder.custom().createIgnoreVerifySSLHttpsClient();

        /**
         * 直接构建一个忽略安全的http请求客户端,具有失败重试的功能
         */
        HttpsBuilder.custom().createIgnoreVerifySSLHttpsClient(3);
    }

}
