package com.fish.rpc.dto;

import com.fish.rpc.enums.CompressType;
import com.fish.rpc.enums.MsgType;
import com.fish.rpc.enums.SerializeType;
import com.fish.rpc.enums.VersionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Afish
 * @date 2025/4/21 16:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer reqId;
    private VersionType version;
    private MsgType msgType;
    private SerializeType serializeType;
    private CompressType compressType;
    private Object data;
}
