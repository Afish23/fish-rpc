package com.fish.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Afish
 * @date 2025/4/26 15:03
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {
    /**
     *每秒支持多少请求访问
     * @return
     */
    double permitsPerSecond();

    /**
     * 拿不到令牌的等待时间
     *
     * @return
     */
    long timeout();
}
