package com.fish.rpc.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Afish
 * @date 2025/4/28 16:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcConfig {
    private String serializer = "kyro";
}
