package com.fish.rpc.factory;

import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Afish
 * @date 2025/4/19 17:24
 */
public class SingletonFactory {
    private static final Map<Class<?>, Object> INSTANCES_CACHE = new ConcurrentHashMap<>();

    private SingletonFactory() {}

    @SneakyThrows
    public static <T> T getInstance(Class<T> clazz) {
        if (Objects.isNull(clazz)) {
            throw new IllegalArgumentException("clazz不能为空");
        }

        if (INSTANCES_CACHE.containsKey(clazz)) {
            return clazz.cast(INSTANCES_CACHE.get(clazz));
        }

        synchronized (SingletonFactory.class) {
            if (INSTANCES_CACHE.containsKey(clazz)) {
                return clazz.cast(INSTANCES_CACHE.get(clazz));
            }

            T t = clazz.getConstructor().newInstance();
            INSTANCES_CACHE.put(clazz, t);
            return t;
        }
    }
}
