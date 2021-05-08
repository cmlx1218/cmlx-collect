package com.cmlx.commons.http;

import com.cmlx.commons.exception.EXPF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 11:10
 * @Desc ->
 **/
@Slf4j
public class HttpResponse {

    @Getter
    @Setter
    private int statusCode;
    @Getter
    @Setter
    private String statusCodeText;
    @Getter
    @Setter
    private String encoding;
    @Getter
    @Setter
    private String contentType;
    @Setter
    private String contentString;
    @Setter
    private byte[] contentBytes;
    @Getter
    @Setter
    private Header[] headers = new Header[]{};

    public HttpResponse() {
        this(0, Consts.UTF_8.displayName(), null, null, null);
    }

    public HttpResponse(int statusCode, String contentType, byte[] contentBytes) {
        this(statusCode, Consts.UTF_8.displayName(), contentType, contentBytes, null);
    }

    public HttpResponse(int statusCode, String encoding, String contentType) {
        this(statusCode, encoding, contentType, null, null);
    }

    public HttpResponse(int statusCode, String encoding, String contentType, byte[] contentBytes) {
        this(statusCode, encoding, contentType, contentBytes, null);
    }

    public HttpResponse(int statusCode, String contentType, byte[] contentBytes, Header[] headers) {
        this(statusCode, Consts.UTF_8.displayName(), contentType, contentBytes, headers);
    }

    public HttpResponse(int statusCode, String encoding, String contentType, byte[] contentBytes, Header[] headers) {
        this.statusCode = statusCode;
        this.encoding = encoding;
        this.contentType = contentType;
        this.contentBytes = contentBytes;
        this.headers = headers;
    }

    public InputStream getContentStream() {
        InputStream stream = null;
        if (null != contentBytes) stream = new ByteArrayInputStream(contentBytes);
        return stream;
    }

    public String getContentString() {
        if (null != contentBytes) {
            String encoding = this.encoding == null ? Consts.UTF_8.displayName() : this.encoding;
            try {
                contentString = new String(contentBytes, encoding);
            } catch (UnsupportedEncodingException e) {
                log.error(EXPF.getExceptionMsg(e));

            }
        }
        return contentString;
    }

    public boolean containsHeader(final String name) {
        for (int i = 0; i < this.headers.length; i++) {
            final Header header = this.headers[i];
            if (header.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public Header[] getHeaders(final String name) {
        final List<Header> headersFound = new ArrayList<>();
        for (int i = 0; i < this.headers.length; i++) {
            final Header header = this.headers[i];
            if (header.getName().equalsIgnoreCase(name)) {
                headersFound.add(header);
            }
        }
        return headersFound.toArray(new Header[headersFound.size()]);
    }

    public Header getFirstHeader(final String name) {
        for (int i = 0; i < this.headers.length; i++) {
            final Header header = this.headers[i];
            if (header.getName().equalsIgnoreCase(name)) {
                return header;
            }
        }
        return null;
    }

    public Header getLastHeader(final String name) {
        for (int i = headers.length - 1; i >= 0; i--) {
            final Header header = headers[i];
            if (header.getName().equalsIgnoreCase(name)) {
                return header;
            }
        }
        return null;
    }
}


