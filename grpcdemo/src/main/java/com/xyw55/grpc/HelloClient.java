package com.xyw55.grpc;

import grpc.example.HelloRequest;
import grpc.example.HelloResponse;
import grpc.example.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class HelloClient {
    private final ManagedChannel channel;

    private final HelloServiceGrpc.HelloServiceBlockingStub blockingStub;

    public HelloClient(String host, int port) {
        //初始化连接
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();
        //初始化stub

        blockingStub = HelloServiceGrpc.newBlockingStub(channel);
    }

    /**
     * 关闭连接
     * @throws InterruptedException
     */
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public String sayHello(String name) {
        //构造调用参数对象
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        //调用远程服务方法
        HelloResponse response = blockingStub.sayHello(request);
        //返回值
        return response.getMessage();
    }

    public static void main(String[] args) throws InterruptedException {
        String host = "127.0.0.1";
        int port = 8081;
        HelloClient client = new HelloClient(host, port);
        String content = client.sayHello("55");
        System.out.println(content);
        client.shutdown();
    }


}
