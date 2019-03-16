package com.service.rpc.core.encoder;

import com.service.rpc.core.Response;
import com.service.rpc.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author yule.zhang
 * @date 2019/3/16 19:44
 * @email zhangyule1993@sina.com
 * @description 响应编码  OneToOneEncoder  从  3 版本变更为  MessageToMessageEncoder
 */
public class ResponseEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        Response Response = (Response) o;
        byte[] bytes = SerializationUtil.serialize(Response);
        byteBuf.writeBytes(bytes);
    }
}
