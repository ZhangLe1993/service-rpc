package com.service.rpc.consumer;

import com.service.rpc.core.Request;
import com.service.rpc.core.Response;
import com.service.rpc.core.handler.ConsumerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.service.rpc.core.encoder.*;
import com.service.rpc.core.decoder.*;

/**
 * @author yule.zhang
 * @date 2019/3/16 23:15
 * @email zhangyule1993@sina.com
 * @description 利用RPC调用暴露的接口，消费调用远程服务
 */
public class Consumer {

    private Response response;

    private String address;
    private int port;

    public Consumer(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public Response send(Request request) {
        //服务类
        Bootstrap bootstrap = new Bootstrap();

        //worker
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            //设置线程池
            bootstrap.group(worker);
            //设置socket工厂、
            bootstrap.channel(NioSocketChannel.class);
            //设置管道
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast("encode",new RequestEncoder());
                    ch.pipeline().addLast("decode",new ResponseDecoder());
                    ch.pipeline().addLast(new ConsumerHandler(response));
                }
            });

            //连接到指定服务地址和端口号
            ChannelFuture connect = bootstrap.connect(address, port);

            //发送请求
            connect.channel().writeAndFlush(request);

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            //优雅的关闭
            worker.shutdownGracefully();
        }

        return this.response;
    }
}
