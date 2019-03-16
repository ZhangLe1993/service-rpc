package com.service.rpc.core.rpc;

import com.service.rpc.core.proxy.ProxyHandler;

import java.lang.reflect.Proxy;

/**
 * @author yule.zhang
 * @date 2019/3/17 0:23
 * @email zhangyule1993@sina.com
 * @description Rpc调用
 */
public class Rpc {

    public static <T>T execute(Class<?> clazz, String address, int port) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, new ProxyHandler(address, port));
    }
}
