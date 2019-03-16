package com.service.rpc.core.decoder;

import com.service.rpc.core.Request;
import com.service.rpc.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author yule.zhang
 * @date 2019/3/16 19:44
 * @email zhangyule1993@sina.com
 * @description 请求解码
 */
public class RequestDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        Request request = SerializationUtil.desSerialize(bytes, Request.class);
        list.add(request);
    }
}
