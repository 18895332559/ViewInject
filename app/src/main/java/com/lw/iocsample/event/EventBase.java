package com.lw.iocsample.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lw on 2016/11/11.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {
    // 事件类型
    Class<?> listenerType();

    //事件set方法
    String listenerSetter();

    //方法名
    String methodName();
}
