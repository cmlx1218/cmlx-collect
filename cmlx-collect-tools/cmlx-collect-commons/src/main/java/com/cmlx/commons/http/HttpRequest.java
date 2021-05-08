package com.cmlx.commons.http;

import com.cmlx.commons.support.StringUtility;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHeader;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 11:09
 * @Desc ->
 **/
public class HttpRequest {
    private HttpRequestBase httpBase;
    @Getter
    private Map<String, Object> parameter;
    private List<Header> headers;
    private MultipartEntityBuilder multipartEntityBuilder;
    @Getter
    @Setter
    private StringEntity stringEntity;
    @Getter
    @Setter
    private UrlEncodedFormEntity formEntity;

    private Type type;
    private StringBuilder url;

    public HttpRequest(String url) {
        this(Type.GET, url);
    }

    public HttpRequest(Type type, String url) {
        this(type, url, new ArrayList<>());
    }

    public HttpRequest(Type type, String url, List<Header> headers) {
        this.type = type;
        this.url = new StringBuilder(url);
        this.headers = headers;
        this.parameter = new IdentityHashMap<>();
    }

    private void initMultiparEntity() {
        if (null != stringEntity || null != formEntity) return;

        if (!CollectionUtils.isEmpty(parameter)) {
            switch (type) {
                case GET:
                case DELETE:
                    try {
                        StringUtility.composeUrl(this.url, parameter);
                    } catch (UnsupportedEncodingException e) {
                    }
                    break;
                case POST:
                case PUT:
                    multipartEntityBuilder = MultipartEntityBuilder.create();
                    multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                    multipartEntityBuilder.setCharset(Consts.UTF_8);
                    parameter.forEach((s, o) -> {
                        if (o instanceof File) {
                            multipartEntityBuilder.addBinaryBody(s, (File) o);
                        } else if (o instanceof byte[]) {
                            multipartEntityBuilder.addBinaryBody(s, (byte[]) o);
                        } else {
                            multipartEntityBuilder.addTextBody(s, String.valueOf(o), ContentType.create("text/plain",
                                    Consts.UTF_8));
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    private void initHttpBase() {
        switch (type) {
            case GET:
                httpBase = new HttpGet(URI.create(this.url.toString()));
                break;
            case POST:
                HttpPost post = new HttpPost(URI.create(this.url.toString()));
                setEntity(post);
                httpBase = post;
                break;
            case PUT:
                HttpPut put = new HttpPut(URI.create(this.url.toString()));
                setEntity(put);
                httpBase = put;
                break;
            case DELETE:
                httpBase = new HttpDelete(URI.create(this.url.toString()));
                break;
            default:
                break;
        }
    }

    private void setEntity(HttpEntityEnclosingRequestBase httpBase) {
        if (null != multipartEntityBuilder) {
            httpBase.setEntity(multipartEntityBuilder.build());
        } else if (null != stringEntity) {
            httpBase.setEntity(stringEntity);
        } else if (null != formEntity) {
            httpBase.setEntity(formEntity);
        }
    }

    private void initHeader() {
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach(header -> httpBase.addHeader(header));
        }
    }

    public HttpRequestBase getHttpBase() {
        initMultiparEntity();
        initHttpBase();
        initHeader();
        return httpBase;
    }

    public void addParameter(String name, String value) {
        this.parameter.put(new String(name), value);
    }

    public void addAllParameter(Map<String, String> parameter) {
        this.parameter.putAll(parameter);
    }

    public void addParameter(Map<String, Object> parameter) {
        this.parameter.putAll(parameter);
    }

    public void addHeader(String name, String value) {
        this.addHeader(new BasicHeader(name, value));
    }

    public void addHeader(Header header) {
        this.headers.add(header);
    }

    public void addHeader(List<Header> header) {
        this.headers.addAll(header);
    }

    public void addHeader(Map<String, String> header) {
        header.forEach((s, s2) -> this.headers.add(new BasicHeader(s, s2)));
    }

    public void close() {
        if (null != httpBase) {
            httpBase.releaseConnection();
        }
    }

    public HttpHost getHttpHost() {
        return URIUtils.extractHost(URI.create(this.url.toString()));
    }

    public static enum Type {
        GET, POST, PUT, DELETE;
    }
}

