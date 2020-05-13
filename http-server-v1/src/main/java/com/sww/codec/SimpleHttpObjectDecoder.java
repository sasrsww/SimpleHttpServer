package com.sww.codec;

import com.google.common.base.Splitter;
import com.sww.http.HttpHeaders;
import com.sww.http.HttpMethod;
import com.sww.http.SimpleHttpRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpVersion;

import java.util.List;

/**
 * SimpleHttpObjectDecoder
 * CR 	13	Carriage return (回车)	 \n
 * LF 	10	Line feed character(换行)	\r
 * SP 	32	Horizontal space(空格)
 * COLON 	58	COLON(冒号)	:
 * @author shenwenwen
 * @date 2020/4/24 23:25
 */
public class SimpleHttpObjectDecoder extends ByteToMessageDecoder {

//    参考：https://www.cnblogs.com/chris-oil/p/6098258.html
//    GET /subao-proxy-customer/saas/mobileH5/getBaseInfo?token= HTTP/1.1
//    Host: car.baoinsurance.com
//    Accept: application/json, text/plain
//    User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36
//    Referer: http://car.baoinsurance.com/subao-h5/aggregation/login/index.html?_1587742526649
//    Accept-Encoding: gzip, deflate
//    Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7
//    Cookie: __MYLOG_UID=0bbb57cb-7ac1-4298-b868-c7b32e46f63c
//    Connection: keep-alive

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        //简单解析 不严谨的处理
        String reqContent = buffer2String(buf);
        System.out.println(reqContent);

        List<String> httpList = Splitter.on("\r\n").splitToList(reqContent);
        //解析请求行 获取到第一个 /n/r
        List<String> reqLine = Splitter.on(" ").splitToList(httpList.get(0));
        HttpMethod httpMethod = HttpMethod.resolve(reqLine.get(0));
        String reqUri = reqLine.get(1);
        HttpVersion httpVersion = HttpVersion.valueOf(reqLine.get(2));

        //解析请求头
        HttpHeaders httpHeaders = parseHttpHeader(httpList);

        //解析请求体
        String body = httpList.get(httpList.size() - 1);

        SimpleHttpRequest httpRequest = new SimpleHttpRequest(httpVersion, httpMethod, reqUri, httpHeaders, body);
        out.add(httpRequest);
    }

    private HttpHeaders parseHttpHeader(List<String> httpList) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (int i = 1; i < httpList.size() - 2; i++) {
            List<String> nameValue = Splitter.on(": ").splitToList(httpList.get(i));
            httpHeaders.add(nameValue.get(0), nameValue.get(1));
        }
        return httpHeaders;
    }

    private String buffer2String(ByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        return new String(bytes, 0, bytes.length);
    }
}
