package com.sww.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import static io.netty.util.internal.ObjectUtil.checkNotNull;

/**
 * SimpleHttpResponse
 *
 * @author shenwenwen
 * @date 2020/4/26 20:40
 */
public class SimpleHttpResponse {
    private HttpVersion version;

    private HttpResponseStatus status;

    private ByteBuf content;

    private HttpHeaders headers;

    public SimpleHttpResponse(HttpVersion version, HttpResponseStatus status) {
        this.version = checkNotNull(version, "version");
        this.status = checkNotNull(status, "status");
        this.headers = new HttpHeaders();

        content = Unpooled.buffer(0);
    }

    public HttpVersion getVersion() {
        return version;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }

    public ByteBuf getContent() {
        return content;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeader(String name, String value) {
        headers.set(name, value);
    }
}
