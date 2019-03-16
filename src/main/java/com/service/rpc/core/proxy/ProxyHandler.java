package com.service.rpc.core.proxy;

import com.service.rpc.consumer.Consumer;
import com.service.rpc.core.Request;
import com.service.rpc.core.Response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author yule.zhang
 * @date 2019/3/17 0:09
 * @email zhangyule1993@sina.com
 * @description 代理对象处理类
 */
public class ProxyHandler implements InvocationHandler {

    private String address;
    private int port;

    public ProxyHandler(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setRequestId(UUID.randomUUID().toString());
        request.setParameterTypes(method.getParameterTypes());
        Consumer client = new Consumer(address, port);
        Response response = client.send(request);
        if (response.getError() != null){
            throw response.getError();
        } else {
            return response;
        }
    }
}
