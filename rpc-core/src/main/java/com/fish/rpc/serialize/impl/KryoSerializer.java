package com.fish.rpc.serialize.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.dto.RpcResp;
import com.fish.rpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

/**
 * @author Afish
 * @date 2025/4/21 16:52
 */
@Slf4j
public class KryoSerializer implements Serializer {
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(()->{
        Kryo kryo = new Kryo();
        kryo.register(RpcReq.class);
        kryo.register(RpcResp.class);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream oos = new ByteArrayOutputStream(); Output output = new Output(oos)){
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            kryo.writeObject(output, obj);
            output.flush();
            return oos.toByteArray();
        }catch (Exception e){
            log.error("kryo序列化失败", e);
            throw new RuntimeException(e);
        }finally {
            KRYO_THREAD_LOCAL.remove();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return null;
    }
}
