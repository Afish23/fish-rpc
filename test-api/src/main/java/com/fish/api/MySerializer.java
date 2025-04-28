package com.fish.api;

import com.fish.rpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Afish
 * @date 2025/4/28 10:56
 */
@Slf4j
public class MySerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            log.info("========使用MySerializer做序列化========");
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)){
            log.info("========使用MySerializer做反序列化========");
            return clazz.cast(ois.readObject());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
