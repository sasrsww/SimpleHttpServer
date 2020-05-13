package com.sww;

import com.sww.codec.SimpleHttpObjectDecoder;
import com.sww.codec.SimpleHttpObjectEncoder;
import com.sww.http.SimpleHttpRequest;
import com.sww.http.SimpleHttpResponse;
import com.sww.servlet.DefaultServlet;
import com.sww.servlet.DispatcherServlet;
import com.sww.util.HttpHandlerUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

/**
 * SimpleHttpServer
 *
 * @author shenwenwen
 * @date 2020/4/22 23:07
 */
@Slf4j
public class SimpleHttpServer {

    private int port;

    private DispatcherServlet dispatcherServlet = new DispatcherServlet();

    public SimpleHttpServer(int port) {
        this.port = port;
        dispatcherServlet.init();
    }

    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);
        NioEventLoopGroup workGroup = new NioEventLoopGroup(2);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap().group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(final Channel ch) throws Exception {
                            ch.pipeline().addLast("simpleHttpDecoder", new SimpleHttpObjectDecoder());
                            ch.pipeline().addLast("simpleHttpEncoder", new SimpleHttpObjectEncoder());
                            ch.pipeline()
                                    .addLast("serverHandle", new SimpleChannelInboundHandler<SimpleHttpRequest>() {

                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, SimpleHttpRequest request)
                                                throws Exception {
                                            SimpleHttpResponse httpResponse = dispatcherServlet.doService(request);
                                            if (httpResponse != null) {
                                                httpResponse.getHeaders().set(HttpHeaderNames.CONNECTION.toString(), HttpHeaderValues.KEEP_ALIVE.toString());
                                                httpResponse.getHeaders().set(HttpHeaderNames.CONTENT_LENGTH.toString(), httpResponse.getContent().readableBytes() + "");
                                                ch.writeAndFlush(httpResponse);
                                            }
                                        }

                                        @Override
                                        public void channelUnregistered(ChannelHandlerContext ctx) {
                                            ctx.channel().close();
                                        }

                                        @Override
                                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                            log.error("native request error", cause.getCause() == null ? cause : cause.getCause());
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("error", cause.getCause().toString());
                                            SimpleHttpResponse httpResponse = HttpHandlerUtil.buildJson(data);
                                            httpResponse.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
                                            ctx.channel().writeAndFlush(httpResponse);
                                        }
                                    });
                        }
                    });
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {

        new SimpleHttpServer(8998).start();
    }
}
