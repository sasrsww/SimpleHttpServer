package com.sww.util;

import com.alibaba.fastjson.JSON;
import com.sww.http.SimpleHttpResponse;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;

public class HttpHandlerUtil {

  public static void writeJson(Channel channel, Object obj) {
    channel.writeAndFlush(buildJson(obj));
  }


  public static SimpleHttpResponse buildJson(Object obj) {
    SimpleHttpResponse response = new SimpleHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    response.getHeaders().set(HttpHeaderNames.CONTENT_TYPE.toString(), "application/json; charset=utf-8");
    if (obj != null) {
      try {
        String content = JSON.toJSONString(obj);
        response.getContent().writeBytes(content.getBytes(Charset.forName("utf-8")));
      } catch (Exception e) {
        response.setStatus(HttpResponseStatus.SERVICE_UNAVAILABLE);
      }
    }
    response.getHeaders().set(HttpHeaderNames.CONTENT_LENGTH.toString(), response.getContent().readableBytes() + "");
    return response;
  }

  public static SimpleHttpResponse buildContent(String content, String contentType) {
    SimpleHttpResponse response = new SimpleHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    response.getHeaders().set(HttpHeaderNames.CONTENT_TYPE.toString(), contentType);
    if (content != null) {
      response.getContent().writeBytes(content.getBytes(Charset.forName("utf-8")));
    }
    response.getHeaders().set(HttpHeaderNames.CONTENT_LENGTH.toString(), response.getContent().readableBytes() + "");
    return response;
  }
}
