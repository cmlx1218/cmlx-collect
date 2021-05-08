package com.cmlx.commons.http;

import com.cmlx.commons.constant.ErrorCode;
import com.cmlx.commons.exception.EXPF;
import com.cmlx.commons.http.config.HttpManagerConfig;
import com.cmlx.commons.http.config.HttpManagerProperties;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 11:09
 * @Desc ->
 **/
public class HttpClientManager {

    private Lock lock = new ReentrantLock();
    private HttpManagerProperties managerProperties;

    // 连接池
    @Getter
    private PoolingHttpClientConnectionManager clientManager;
    // 默认client
    @Getter
    private CloseableHttpClient defaultClient;
    private Map<HttpHost, CloseableHttpClient> hostClient;
    // 过期连接回收线程
    private IdelConnectionMonitorThread monitorThread;

    @Getter
    @Setter
    private RequestConfig defaultRequestConfig;
    @Getter
    @Setter
    private HttpRequestRetryHandler defaultRequestRetryHandler;

    public HttpClientManager(HttpManagerProperties managerProperties) {
        Assert.notNull(managerProperties, "config can not be null");
        this.managerProperties = managerProperties;
        init();
    }

    public HttpClientManager(HttpManagerConfig managerConfig) {
        this.managerProperties = new HttpManagerProperties();
        this.managerProperties.setTimeOutSocket(managerConfig.timeOutSocket());
        this.managerProperties.setTimeOutConnection(managerConfig.timeOutConnection());
        this.managerProperties.setTimeOutRequestConnection(managerConfig.timeOutRequestConnection());
        this.managerProperties.setRetryNumber(managerConfig.retryNumber());
        this.managerProperties.setMaxTotal(managerConfig.maxTotal());
        init();
    }

    public void init() {
        this.clientManager = new PoolingHttpClientConnectionManager();

        ConnectionConfig defaultConnectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8).build();
        SocketConfig defaultSocketConfig = SocketConfig.custom().setSoTimeout(managerProperties.getTimeOutSocket()).setTcpNoDelay(true).build();

        this.clientManager.setDefaultConnectionConfig(defaultConnectionConfig);
        this.clientManager.setDefaultSocketConfig(defaultSocketConfig);
        this.clientManager.setMaxTotal(managerProperties.getMaxTotal());

        this.defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT).setSocketTimeout(managerProperties.getTimeOutSocket()).setConnectionRequestTimeout(managerProperties.getTimeOutRequestConnection())
                .setConnectTimeout(managerProperties.getTimeOutConnection()).build();
        this.defaultRequestRetryHandler = new SimpleHttpRequestRetryHandler(managerProperties.getRetryNumber());

        this.defaultClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).setRetryHandler(defaultRequestRetryHandler).setConnectionManager(clientManager).build();
        this.monitorThread = new IdelConnectionMonitorThread(clientManager);
        this.monitorThread.start();
    }

    public HttpClientBuilder createHttpClient() {
        return HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).setRetryHandler(defaultRequestRetryHandler).setConnectionManager(clientManager);
    }

    public void registerClient(HttpHost host) {
        CloseableHttpClient createHttpClient = createHttpClient().build();
        registerClient(host, createHttpClient);
    }

    public void registerClient(HttpHost host, CloseableHttpClient client) {
        lock.lock();
        try {
            if (null == hostClient) {
                hostClient = new HashMap<>();
            }
            hostClient.put(host, client);
        } finally {
            lock.unlock();
        }
    }

    public void setMaxPerRoute(HttpHost host, int maxPerRoute) {
        this.clientManager.setMaxPerRoute(new HttpRoute(host), maxPerRoute);
    }

    public void removeClient(HttpHost host) throws IOException {
        lock.lock();
        try {
            if (!CollectionUtils.isEmpty(hostClient)) {
                CloseableHttpClient closeableHttpClient = hostClient.get(host);
                if (null != closeableHttpClient) {
                    closeableHttpClient.close();
                    hostClient.remove(host);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void close() {
        lock.lock();
        try {
            if (null != defaultClient) {
                monitorThread.shutDown();
                clientManager.close();
                defaultClient.close();
                if (!CollectionUtils.isEmpty(hostClient)) {
                    hostClient.forEach((httpHost, closeableHttpClient) -> {
                        try {
                            closeableHttpClient.close();
                        } catch (IOException e) {
                        }
                    });
                }
            }
        } catch (IOException e) {
        } finally {
            lock.unlock();
        }
    }

    public HttpResponse execute(HttpRequest request, HttpClientContext context) throws Exception {
        CloseableHttpClient client = this.defaultClient;
        if (null != hostClient) {
            CloseableHttpClient closeableHttpClient = hostClient.get(request.getHttpHost());
            if (null != closeableHttpClient)
                client = closeableHttpClient;
        }
        return execute(client,request,context);
    }

    public HttpResponse execute(HttpRequest request) throws Exception {
        CloseableHttpClient client = this.defaultClient;
        if(null != hostClient){
            CloseableHttpClient closeableHttpClient = hostClient.get(request.getHttpHost());
            if(null != closeableHttpClient)
                client = closeableHttpClient;
        }
        return execute(client,request);
    }

    /**
     *  TODO 封装异常抛出roll back
     *
     */
    public HttpResponse execute(CloseableHttpClient client, HttpRequest request) throws Exception {
        CloseableHttpResponse closeableHttpResponse = null;
        HttpEntity entity = null;
        HttpResponse response;
        try {
            HttpRequestBase httpBase = request.getHttpBase();
            closeableHttpResponse = client.execute(httpBase);
            int code = closeableHttpResponse.getStatusLine().getStatusCode();
            String codeText = closeableHttpResponse.getStatusLine().getReasonPhrase();
            response = new HttpResponse();
            response.setStatusCode(code);
            response.setStatusCodeText(codeText);
            parsingHeader(response, closeableHttpResponse);
        } catch (IOException e) {
            throw EXPF.exception(ErrorCode.HttpRequestError.getCode(),ErrorCode.HttpRequestError.getTemplate(),null,this.getClass().getSimpleName(),true);
        } finally {
            if (null != closeableHttpResponse) {
                try {
                    closeableHttpResponse.close();
                } catch (IOException e) {
                }
            }
            if (null != entity) {
                try {
                    entity.getContent().close();
                } catch (IllegalStateException e) {
                } catch (IOException e) {
                }
            }
            if (null != request) {
                request.close();
            }
        }
        return response;
    }

    /**
     *  TODO 封装异常抛出roll back
     *
     */
    public HttpResponse execute(CloseableHttpClient client, HttpRequest request, HttpClientContext context) throws Exception {
        CloseableHttpResponse closeableHttpResponse = null;
        HttpEntity entity = null;
        HttpResponse response = null;
        try {
            closeableHttpResponse = client.execute(request.getHttpBase(), context);
            int code = closeableHttpResponse.getStatusLine().getStatusCode();
            String codeText = closeableHttpResponse.getStatusLine().getReasonPhrase();
            response = new HttpResponse();
            response.setStatusCode(code);
            response.setStatusCodeText(codeText);
            parsingHeader(response, closeableHttpResponse);
        } catch (IOException e) {
            throw EXPF.exception(ErrorCode.HttpRequestError.getCode(),ErrorCode.HttpRequestError.getTemplate(),null,this.getClass().getSimpleName(),true);
        } finally {
            try {
                if (null != closeableHttpResponse) {
                    closeableHttpResponse.close();
                }
            } catch (IOException e) {
            }
            if (null != entity) {
                try {
                    entity.getContent().close();
                } catch (IllegalStateException e) {
                } catch (IOException e) {
                }
            }
            if (null != request) {
                request.close();
            }
        }
        return response;
    }

    private void parsingHeader(HttpResponse response, CloseableHttpResponse closeableHttpResponse) throws IOException {
        if (closeableHttpResponse.containsHeader(HttpHeaders.CONTENT_ENCODING)) {
            Header encodingHeader = closeableHttpResponse.getFirstHeader(HttpHeaders.CONTENT_ENCODING);
            response.setEncoding(encodingHeader.getValue());
        }
        if (closeableHttpResponse.containsHeader(HttpHeaders.CONTENT_TYPE)) {
            Header contentTypeHeader = closeableHttpResponse.getFirstHeader(HttpHeaders.CONTENT_TYPE);
            if (contentTypeHeader.getValue().contains(";")) {
                String[] cType = contentTypeHeader.getValue().split(";");
                response.setContentType(cType[0]);
                String[] charSet = cType[1].split("=");
                response.setEncoding(charSet[1]);
            } else {
                response.setContentType(contentTypeHeader.getValue());
            }
        }
        response.setHeaders(closeableHttpResponse.getAllHeaders());
        HttpEntity entity = closeableHttpResponse.getEntity();
        Header contentType = entity.getContentType();
        Header contentEncoding = entity.getContentEncoding();
        if (null != contentEncoding) {
            response.setEncoding(contentEncoding.getValue());
        }
        if (null != contentType) {
            if (contentType.getValue().contains(";")) {
                String[] cType = contentType.getValue().split(";");
                response.setContentType(cType[0]);
                String[] charSet = cType[1].split("=");
                response.setEncoding(charSet[1]);
            } else {
                response.setContentType(contentType.getValue());
            }
        }
        byte[] byteArray = EntityUtils.toByteArray(entity);
        response.setContentBytes(byteArray);
    }
}

