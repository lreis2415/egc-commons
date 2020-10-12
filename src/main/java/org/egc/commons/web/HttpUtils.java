package org.egc.commons.web;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.ssl.SSLContexts;
import org.egc.commons.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/9/23 11:41
 */
public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 获取SSL套接字对象 重点：设置tls协议的版本
     *
     * @return ssl context
     */
    protected static SSLContext createIgnoreVerifySSL() {
        // 创建套接字对象
        SSLContext sslContext = null;
        try {
            //指定TLS版本
            sslContext = SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            log.error("Create SSLContext failed!", e);
        }
        // 实现X509TrustManager接口，用于绕过验证
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        try {
            assert sslContext != null;
            sslContext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            log.error("Initialize SSLContext failed!", e);
        }
        return sslContext;
    }

    /**
     * 绕过SSL
     * Gets ssl context.
     *
     * @return the ssl context
     */
    protected static SSLContext getSSLContext() {
        try {
            return SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new BusinessException(e, "get SSLContext failed!");
        }
    }


    /**
     * Gets Pooling HttpClient Connection Manager
     *
     * @return the pooling conn mgr
     */
    public static PoolingHttpClientConnectionManager getPoolingConnMgr() {
        //create a socketfactory in order to use an http connection manager
        PlainConnectionSocketFactory plainSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> connSocketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainSocketFactory)
                .register("https", new SSLConnectionSocketFactory(getSSLContext(),NoopHostnameVerifier.INSTANCE))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(connSocketFactoryRegistry);
        connManager.setMaxTotal(80);
        connManager.setDefaultMaxPerRoute(20);
        return connManager;
    }

    /**
     * TODO test， HTTPS
     * 异步
     * gets async Pooling HttpClient Connection Manager
     *
     * @return PoolingNHttpClientConnectionManager pooling n conn mgr
     * @throws IOReactorException the io reactor exception
     */
    public static PoolingNHttpClientConnectionManager getPoolingNConnMgr() throws IOReactorException {
        // 设置协议http对应的处理socket链接工厂的对象
        Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                .register("http", NoopIOSessionStrategy.INSTANCE)
                .build();
        //IO线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(Runtime.getRuntime().availableProcessors()).setSoKeepAlive(true).build();

        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);

        PoolingNHttpClientConnectionManager manager = new PoolingNHttpClientConnectionManager(ioReactor, null, sessionStrategyRegistry, null);
        manager.setMaxTotal(80);
        manager.setDefaultMaxPerRoute(20);
        return manager;
    }
}
