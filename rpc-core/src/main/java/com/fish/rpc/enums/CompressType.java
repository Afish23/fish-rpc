package com.fish.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

/**
 * @author Afish
 * @date 2025/4/21 16:46
 */
@ToString
@Getter
@AllArgsConstructor
public enum CompressType {
    GZIP((byte) 1, "gzip");

    private final byte code;
    private final String desc;

    public static CompressType from(byte code) {
        return Arrays.stream(values()).filter(o -> o.code == code).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("code异常" + code));
    }
}
