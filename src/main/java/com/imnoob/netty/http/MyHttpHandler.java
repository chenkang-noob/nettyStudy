package com.imnoob.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class MyHttpHandler extends SimpleChannelInboundHandler<HttpObject> {


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开连接");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        if (msg instanceof HttpRequest){
            DefaultHttpRequest request = (DefaultHttpRequest) msg;


            String path = request.uri();
            System.out.println("路径："+path);

            ByteBuf content = Unpooled.copiedBuffer("我是服务器", CharsetUtil.UTF_8);

            HttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,content);

            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            httpResponse.headers().set("token", "asdlkjhfglaasdasf");

            ctx.writeAndFlush(httpResponse);
        }
    }
}
