package com.xyw55.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author xyw55
 * @date 2018/4/9
 */
public class EchoClientHandler extends SimpleChannelInboundHandler {
    /**
     * 服务端响应请求返回的数据的时候会自动调用该方法，通过实现该方法来接收服务端返回的数据，并实现客户端调用的业务逻辑
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        // 获取服务端返回的数据buf
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        // 将服务端返回的byte数组转换成字符串，并打印
        String body = new String(req, "UTF-8");
        System.out.println("receive data from server: " + body);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(" Unexcepted exception from downstream : " + cause.getMessage());
        // 发生异常，关闭链路
        ctx.close();
    }
}
