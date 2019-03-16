package com.service.rpc.core.handler;

import com.service.rpc.core.Request;
import com.service.rpc.core.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yule.zhang
 * @date 2019/3/16 22:46
 * @email zhangyule1993@sina.com
 * @description 注册服务处理
 */
public class ServerHandler extends SimpleChannelInboundHandler {

    private Map<String,Object> serviceMap = new ConcurrentHashMap<>();

    public ServerHandler(Map<String,Object> map){
        this.serviceMap = map;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {

        //获取请求消息
        Request request = (Request) msg;

        Response response = new Response();

        //调用请求类的请求方法执行并返回执行结果
        Object invoke = null;

        try{
            //根据要请求的类名寻找服务类
            Object bean = serviceMap.get(request.getClassName());

            //获取字节码
            Class<?> clazz = Class.forName(request.getClassName());

            //获取方法  方法名称和参数类型
            Method method = clazz.getMethod(request.getMethodName(),request.getParameterTypes());

            //反射执行方法
            invoke = method.invoke(bean,request.getParameters());

            //返回响应
            response.setRequestId(request.getRequestId());
            response.setResult(invoke);

        }catch(Exception e){
            response.setError(e);
            response.setRequestId(request.getRequestId());
        }

        //返回响应  写入通道
        //channelHandlerContext.channel().writeAndFlush(response);
        channelHandlerContext.writeAndFlush(response);
    }
}
