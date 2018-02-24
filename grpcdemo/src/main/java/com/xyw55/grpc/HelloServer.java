package com.xyw55.grpc;

import grpc.example.HelloRequest;
import grpc.example.HelloResponse;
import grpc.example.HelloServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class HelloServer {
    private int port = 8081;
    private Server server;

    /**
     *
     * @throws IOException
     */
    private void start() throws IOException {
        //初始化并启动服务
        server = ServerBuilder.forPort(port)
                .addService(new HelloServiceImpl())
                .build()
                .start();

        // 注册钩子，jvm退出的时候停止服务
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                HelloServer.this.stop();
            }
        });
    }

    /**
     * 停止服务
     */
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * 阻塞一直到退出程序
     */
    private void blockUnitlShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase{

        @Override
        public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
            // 构造返回对象
            HelloResponse reply = HelloResponse.newBuilder().setMessage("hello," + request.getName()).build();
            //将返回结果传入stream，返回给调用方
            responseObserver.onNext(reply);
            //通知stream结束
            responseObserver.onCompleted();
        }

    }


    public static void main(String[] args) throws InterruptedException, IOException {
        final HelloServer server = new HelloServer();
        server.start();
        server.blockUnitlShutdown();
    }
}
