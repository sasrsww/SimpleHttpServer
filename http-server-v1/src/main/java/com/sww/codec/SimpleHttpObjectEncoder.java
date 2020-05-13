package com.sww.codec;

import com.sww.http.SimpleHttpResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * SimpleHttpObjectEncoder
 *
 * @author shenwenwen
 * @date 2020/4/26 20:32
 */
public class SimpleHttpObjectEncoder extends MessageToByteEncoder<SimpleHttpResponse> {

//    HTTP/1.1 200 OK
//    Date: Sun, 26 Apr 2020 12:46:44 GMT
//    Content-Type: application/json;charset=utf-8
//    Content-Length: 1183
//    X-Application-Context: subao-gateway:8080
//    X-Frame-Options: SAMEORIGIN
//    Proxy-Connection: keep-alive
//
//    {"success":true,"errorMsg":"","errorCode":"","model":""}}

    @Override
    protected void encode(ChannelHandlerContext ctx, SimpleHttpResponse msg, ByteBuf out) throws Exception {
        //响应行
        String respLine = msg.getVersion().text() + " " + msg.getStatus().code() + " " + msg.getStatus().reasonPhrase() + "\r\n";
        out.writeBytes(respLine.getBytes());

        //响应头
        for (String name : msg.getHeaders().keySet()) {
            List<String> values = msg.getHeaders().get(name);
            for (String value : values) {
                String header = name + ": " + value + "\r\n";
                out.writeBytes(header.getBytes());
            }
        }
        out.writeBytes("\r\n".getBytes());

        //响应体
        out.writeBytes(msg.getContent());

        byte[] bytes = new byte[out.readableBytes()];
        out.getBytes(out.readerIndex(), bytes);
        System.out.println(new String(bytes, 0, bytes.length));
    }

}
