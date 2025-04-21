package com.fish.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

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
}
