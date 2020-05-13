package com.sww.http;


import com.sww.util.HttpUrlUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpVersion;

import java.util.Map;

import static io.netty.util.internal.ObjectUtil.checkNotNull;

/**
 * HttpRequest
 *
 * @author shenwenwen
 * @date 2020/4/25 21:43
 */
public class SimpleHttpRequest {

    private HttpVersion version;

    private HttpMethod method;

    private String uri;

    private HttpHeaders headers;

    private String content;

    private String reqPath;

    private Map<String, String> reqParams;

    public SimpleHttpRequest(HttpVersion version, HttpMethod method, String uri, HttpHeaders headers, String content) {
        this.version = checkNotNull(version, "version");
        this.headers = checkNotNull(headers, "headers");
        this.method = checkNotNull(method, "method");
        this.uri = checkNotNull(uri, "uri");
        this.content = checkNotNull(content, "content");

        reqPath = HttpUrlUtil.getReqPath(this.uri());

        reqParams = HttpUrlUtil.getReqParams(this.uri());
    }

    public String content() {
        return this.content;
    }

    public HttpHeaders headers() {
        return this.headers;
    }

    public HttpVersion protocolVersion() {
        return version;
    }

    public HttpMethod method() {
        return method;
    }

    public String uri() {
        return uri;
    }

    public String getReqPath() {
        return reqPath;
    }

    public Map<String, String> getReqParams() {
        return reqParams;
    }
}
