package com.fish.rpc.serialize.impl;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.fish.rpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Afish
 * @date 2025/4/27 11:34
 */
@Slf4j
public class HessianSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()){
            HessianOutput ho = new HessianOutput(os);
            ho.writeObject(obj);
            log.info("========使用Hessian做序列化========");
            return os.toByteArray();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)){
            HessianInput hi = new HessianInput(is);
            Object o = hi.readObject();
            log.info("========使用Hessian做反序列化========");
            return clazz.cast(o);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
