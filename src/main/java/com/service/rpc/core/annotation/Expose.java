package com.service.rpc.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * ElementType.METHOD  表示用在方法上
 * ElementType.TYPE    表示用在 类、接口（包括注解类型）或enum声明
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//使用spring实例化
@Component
public @interface Expose {

    Class<?> value();
}
