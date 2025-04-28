package com.fish.rpc.spi;

/**
 * @author Afish
 * @date 2025/4/28 11:07
 */
public class Holder<T> {
    private volatile T value;
    public T get(){
        return value;
    }

    public void set(T value){
        this.value = value;
    }
}
