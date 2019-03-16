package com.service.rpc.provider;

import com.service.rpc.core.annotation.Expose;
import com.service.rpc.core.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.service.rpc.core.encoder.*;
import com.service.rpc.core.decoder.*;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yule.zhang
 * @date 2019/3/16 21:01
 * @email zhangyule1993@sina.com
 * @description 服务暴露，监听端口
 */
public class ProviderServer implements ApplicationContextAware {

    private int port;

    public ProviderServer(int port) {
        this.port = port;
    }

    private Map<String,Object> serviceMap = new ConcurrentHashMap<>();

    public void start() {

        ServerBootstrap bootstrap = new ServerBootstrap();

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            //设置线程池
            bootstrap.group(boss, worker);

            //设置socket工厂
            bootstrap.channel(NioServerSocketChannel.class);

            //设置管道工厂
            bootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    //对接受的请求解码
                    channel.pipeline().addLast("decoder",new RequestDecoder());
                    //返回编码后的响应
                    channel.pipeline().addLast("encoder",new ResponseEncoder());
                    //处理请求
                    channel.pipeline().addLast("handler",new ServerHandler(serviceMap));
                }
            });

            //netty3中对应设置如下
            //bootstrap.setOption("backlog", 1024);
            //bootstrap.setOption("tcpNoDelay", true);
            //bootstrap.setOption("keepAlive", true);
            //设置参数，TCP参数
            bootstrap.option(ChannelOption.SO_BACKLOG, 2048);           //serverSocketChannel的设置，链接缓冲池的大小
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);    //socketChannel的设置,维持链接的活跃，清除死链接
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);     //socketChannel的设置,关闭延迟发送

            //绑定端口
            //bootstrap.bind(new InetSocketAddress(port));
            ChannelFuture future = bootstrap.bind(port);

            System.out.println("start");

            //等待服务端关闭
            future.channel().closeFuture().sync();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //释放资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        //获取加了暴露注解的接口
        Map<String,Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Expose.class);

        //遍历
        for(Map.Entry<String,Object> entry : beansWithAnnotation.entrySet()) {

            //获取接口名称
            String interfaceName = entry.getValue().getClass().getAnnotation(Expose.class).value().getName();

            //获取全路径
            serviceMap.put(interfaceName,entry.getValue());
        }

        //启动RPC服务
        start();
    }
}
