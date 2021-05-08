package com.cmlx.commons.http;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 11:10
 * @Desc -> 关闭过期连接
 **/
public class IdelConnectionMonitorThread extends Thread {

    private final HttpClientConnectionManager connectionManager;
    private volatile boolean shutdown;


    public IdelConnectionMonitorThread(HttpClientConnectionManager connectionManager) {
        super();
        this.connectionManager = connectionManager;
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    if (null != connectionManager) {
                        connectionManager.closeExpiredConnections();
//                    connectionManager.closeIdleConnections(30, TimeUnit.SECONDS);
                    }
                }
            }
        } catch (InterruptedException e) {
        }
    }

    public void shutDown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}

