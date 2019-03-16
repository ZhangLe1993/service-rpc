package com.service.rpc.core.decoder;

import com.service.rpc.core.Response;
import com.service.rpc.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author yule.zhang
 * @date 2019/3/16 19:45
 * @email zhangyule1993@sina.com
 * @description 响应解码  FrameDecoder  变为  ByteToMessageDecoder
 */
public class ResponseDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        byte [] bytes = new byte[byteBuf.readableBytes()];

        byteBuf.readBytes(bytes);

        Response response = SerializationUtil.desSerialize(bytes,Response.class);

        list.add(response);
    }
}
