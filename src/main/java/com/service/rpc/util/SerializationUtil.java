package com.service.rpc.util;


import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yule.zhang
 * @date 2019/3/16 19:47
 * @email zhangyule1993@sina.com
 * @description 序列化工具 基于 Protostuff 实现
 */
public class SerializationUtil {

    private static Map<Class<?>,Schema<?>> cacheSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    /**
     * 获取类的schema
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> Schema<T> getSchema(Class<T> clazz){

        //先尝试从缓存或者注册中心获取 schema
        Schema<T> schema = (Schema<T>) cacheSchema.get(clazz);

        //如果为空
        if(schema == null){

            //从字节码中获得 schema
            schema = RuntimeSchema.createFrom(clazz);

            if(schema != null){
                //缓存起来
                cacheSchema.put(clazz,schema);
            }
        }

        return schema;
    }

    /**
     * 对象序列化  ( object -- >  byte [] )
     * @param object
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T object){

        //获取字节码
        Class<T> clazz = (Class<T>) object.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try{
            //获取schema
            Schema<T> schema = getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(object, schema, buffer);
        }catch(Exception e){
            //抛出无效状态异常
            //在不合法或者不合理的时间内调用方法抛出异常
            throw new IllegalStateException(e.getMessage(), e);
        }finally{
            buffer.clear();
        }
    }


    /**
     * 反序列化  ( byte[] --> object)
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T desSerialize(byte [] data, Class<T> clazz){

        try{
            //如果一个类没有空构造方法的时候，进行实例化操作  clazz.newInstance(); 会抛出异常
            //通过ObjenesisStd 可以避免
            T message = objenesis.newInstance(clazz);

            Schema<T> schema = getSchema(clazz);

            ProtostuffIOUtil.mergeFrom(data, message, schema);

            return message;
        }catch(Exception e){
            throw new IllegalStateException(e.getMessage(),e);
        }
    }
}
