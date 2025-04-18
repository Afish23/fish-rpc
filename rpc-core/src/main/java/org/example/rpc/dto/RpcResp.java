package org.example.rpc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rpc.enums.RpcRespStatus;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResp<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestId;
    private Integer code;
    private String message;
    private T data;

    public static <T> RpcResp<T> success(String reqId, T data) {
        RpcResp<T> resp = new RpcResp<T>();
        resp.setRequestId(reqId);
        resp.setCode(0);
        resp.setData(data);

        return resp;
    }

    public static <T> RpcResp<T> fail(String reqId, String message) {
        RpcResp<T> resp = new RpcResp<T>();
        resp.setRequestId(reqId);
        resp.setCode(RpcRespStatus.FAIL.getCode());
        resp.setMessage(message);

        return resp;
    }
}
