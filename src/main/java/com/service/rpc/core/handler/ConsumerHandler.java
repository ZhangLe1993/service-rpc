package com.service.rpc.core.handler;

import com.service.rpc.core.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yule.zhang
 * @date 2019/3/16 23:50
 * @email zhangyule1993@sina.com
 * @description 发送请求的处理类
 */
public class ConsumerHandler extends SimpleChannelInboundHandler {

    private Response response;

    public ConsumerHandler(Response response) {
        this.response = response;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        response  = (Response) o;
    }
}
