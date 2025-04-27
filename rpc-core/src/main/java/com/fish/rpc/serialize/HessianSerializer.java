package com.fish.rpc.serialize;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Afish
 * @date 2025/4/27 11:34
 */
public class HessianSerializer implements Serializer{

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()){
            HessianOutput ho = new HessianOutput(os);
            ho.writeObject(obj);
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
            return clazz.cast(o);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
