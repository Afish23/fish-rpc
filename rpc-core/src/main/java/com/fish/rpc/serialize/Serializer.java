package com.fish.rpc.serialize;

/**
 * @author Afish
 * @date 2025/4/21 16:51
 */
public interface Serializer {
    byte[] serialize(Object obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
