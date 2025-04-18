package org.example.rpc.transmission.socket.client;

import lombok.extern.slf4j.Slf4j;
import org.example.rpc.dto.RpcRequest;
import org.example.rpc.dto.RpcResp;
import org.example.rpc.transmission.RpcClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
@Slf4j
public class SocketRpcClient implements RpcClient {
    private final String host;
    private final int port;

    public SocketRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public RpcResp<?> sendRequest(RpcRequest request) {
        try(Socket socket = new Socket(host, port)){
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(request);
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object o = inputStream.readObject();

            return (RpcResp<?>)o;

        }catch (Exception e){
            log.error("发送rpc请求失败", e);
            throw new RuntimeException(e);
        }
    }
}
