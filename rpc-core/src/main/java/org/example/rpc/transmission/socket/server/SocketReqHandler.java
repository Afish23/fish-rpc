package org.example.rpc.transmission.socket.server;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.dto.RpcRequest;
import org.example.rpc.dto.RpcResp;
import org.example.rpc.handler.RpcReqHandler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
@AllArgsConstructor
public class SocketReqHandler implements Runnable {
    private final Socket socket;
    private final RpcReqHandler rpcReqHandler;

    @SneakyThrows
    @Override
    public void run() {
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        RpcRequest rpcReq = (RpcRequest) inputStream.readObject();

        Object data = rpcReqHandler.invoke(rpcReq);
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        RpcResp<?> rpcResp = RpcResp.success(rpcReq.getRequestId(), data);
        outputStream.writeObject(rpcResp);
        outputStream.flush();
    }
}
