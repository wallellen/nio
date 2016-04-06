package com.wallellen.netty.section8;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/6/16.
 */
public class SubscribeClientHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ctx.write(req(i));
        }

        ctx.flush();
    }

    private Object req(int i) {
        SubscribeReq req = new SubscribeReq();
        req.setSubReqID(i);
        req.setProductName("prod" + i);
        req.setAddress("address");
        req.setPhoneNumber("12345676");
        req.setUserName("wallellen");
        return req;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println("Receive server response:[" + msg + "]");
    }
}