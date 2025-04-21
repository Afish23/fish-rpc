package com.fish.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Afish
 * @date 2025/4/21 16:40
 */
@ToString
@Getter
@AllArgsConstructor
public enum VersionType {
    VERSION1((byte) 1, "版本1");

    private final byte code;
    private final String desc;
}
