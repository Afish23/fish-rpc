package com.fish.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

/**
 * @author Afish
 * @date 2025/4/21 16:45
 */
@ToString
@Getter
@AllArgsConstructor
public enum SerializeType {
    KRYO((byte) 1, "Kryo");

    private final byte code;
    private final String desc;

    public static SerializeType from(byte code) {
        return Arrays.stream(values()).filter(s -> s.code == code).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("code异常" + code));
    }
}
