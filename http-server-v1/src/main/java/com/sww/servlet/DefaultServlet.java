package com.sww.servlet;

import com.sww.http.SimpleHttpRequest;
import com.sww.http.SimpleHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.InputStream;
import java.net.URI;

/**
 * DefaultServlet
 *
 * @author shenwenwen
 * @date 2020/4/29 23:32
 */
public class DefaultServlet {

    public SimpleHttpResponse doService(SimpleHttpRequest request) throws Exception {
        URI uri = new URI(request.uri());
        String path = uri.getPath();
        if ("/".equals(path)) {
            path = "index.html";
        }
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        SimpleHttpResponse httpResponse;
        if (inputStream != null) {
            String mime = path.substring(path.lastIndexOf(".") + 1);
            httpResponse = new SimpleHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            httpResponse.setHeader(HttpHeaderNames.CONTENT_TYPE.toString(), "text/html; charset=utf-8");
            try {
                byte[] bts = new byte[8192];
                int len;
                while ((len = inputStream.read(bts)) != -1) {
                    httpResponse.getContent().writeBytes(bts, 0, len);
                }
            } finally {
                inputStream.close();
            }
        } else {
            httpResponse = new SimpleHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
            httpResponse.setHeader(HttpHeaderNames.CONTENT_TYPE.toString(), "application/octet-stream");
        }
        return httpResponse;
    }
}
