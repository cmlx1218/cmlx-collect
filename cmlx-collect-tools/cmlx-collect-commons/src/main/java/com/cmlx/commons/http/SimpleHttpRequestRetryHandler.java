package com.cmlx.commons.http;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 11:10
 * @Desc -> 连接失败重试处理
 **/
public class SimpleHttpRequestRetryHandler implements HttpRequestRetryHandler {

    private int retryNumber;

    public SimpleHttpRequestRetryHandler(int retryNumber) {
        super();
        this.retryNumber = retryNumber;
    }

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if(executionCount >= retryNumber){
            return false;
        }
        if(exception instanceof InterruptedIOException){
            return true;
        }
        if(exception instanceof UnknownHostException){
            return false;
        }
        if(exception instanceof ConnectTimeoutException){
            return true;
        }
        if(exception instanceof SSLException){
            return false;
        }
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpRequest request = clientContext.getRequest();
        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
        if(idempotent){
            return true;
        }
        return false;
    }
}

