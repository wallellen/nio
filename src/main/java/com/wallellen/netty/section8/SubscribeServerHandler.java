package com.wallellen.netty.section8;

import com.wallellen.netty.section7.SubscribeReq;
import com.wallellen.netty.section7.SubscribeResp;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/6/16.
 */
public class SubscribeServerHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        com.wallellen.netty.section7.SubscribeReq req = (SubscribeReq) msg;

        if ("wallellen".equals(req.getUserName())) {
            System.err.println("Service accept client subscribe req: [" + req.toString() + "]");
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }
    }

    private Object resp(int subReqID) {
        SubscribeResp resp = new SubscribeResp();
        resp.setDesc("test desc");
        resp.setRespCode(0);
        resp.setSubReqID(subReqID);
        return resp;
    }
}
