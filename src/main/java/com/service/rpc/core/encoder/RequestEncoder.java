package com.service.rpc.core.encoder;

import com.service.rpc.core.Request;
import com.service.rpc.core.Response;
import com.service.rpc.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author yule.zhang
 * @date 2019/3/16 19:44
 * @email zhangyule1993@sina.com
 * @description 请求编码  序列化成二进制 byte []
 */
public class RequestEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        Request request = (Request) o;
        byte[] bytes = SerializationUtil.serialize(request);
        byteBuf.writeBytes(bytes);
    }
}
