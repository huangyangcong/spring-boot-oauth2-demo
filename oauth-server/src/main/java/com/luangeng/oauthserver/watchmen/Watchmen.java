package com.luangeng.oauthserver.watchmen;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 指定时间内最大允许访问的次数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface Watchmen {

    /**
     * 允许访问的次数，默认值1
     */
    int count() default 1;

    /**
     * 时间段，单位为ms，默认值1s
     */
    long time() default 1000;

    WatchStrategy[] strage() default {WatchStrategy.SESSION};

//    Class strage() default SessionStrage.class;


}
