package com.xyw55.thrift.gencode.server;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NonblockingInvoker {
    /**
     * 启动服务
     * @throws TTransportException
     */
    public void startServer() throws TTransportException {
        /**
         * 1. 创建processor
         */
        TProcessor tProcessor = new HelloService.Processor<HelloService.Iface>(new HelloServiceImpl());
        /**
         * 2.创建transport， 非阻塞 nonblocking, 非阻塞需要用TFramedTransport这种方式
         */
        int port = 8801;
        TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(port);
        TFramedTransport.Factory transport = new TFramedTransport.Factory();
        /**
         * 3.创建protocol
         */
        TCompactProtocol.Factory protocal = new TCompactProtocol.Factory();

        /**
         * 4.将processor transport protocal 设置到服务器中
         */
        TNonblockingServer.Args args = new TNonblockingServer.Args(serverTransport);
        args.processor(tProcessor);
        args.transportFactory(transport);
        args.protocolFactory(protocal);

        TServer server = new TNonblockingServer(args);
        server.serve();

    }
    /**
     * 客户端调用服务端
     */

    public void startClient() throws TException, IOException, InterruptedException {
        String ip = "127.0.0.1";
        int port = 8801;
        int timeOut = 1000;


        /**
         * 1.创建transport
         */
        TNonblockingTransport transport = new TNonblockingSocket(ip, port, timeOut);

        /**
         * 2.创建protocol
         */
        TProtocolFactory tProtocolFactory = new TCompactProtocol.Factory();

        /**
         * 3.基于transport和protocol clientManager 创建client
         */
        TAsyncClientManager clientManager = new TAsyncClientManager();
        HelloService.AsyncClient asyncClient = new HelloService.AsyncClient(tProtocolFactory, clientManager, transport);

        User user = new User("55", "xyw55.com");

        CountDownLatch latch = new CountDownLatch(1);
        AsynInvokerCallback callback = new AsynInvokerCallback(latch);
        asyncClient.sayHello(user, callback);

        latch.await(5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NonblockingInvoker nonblockingInvoker = new NonblockingInvoker();
                    try {
                        nonblockingInvoker.startServer();
                    } catch (TTransportException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Thread.sleep(5000);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    NonblockingInvoker nonblockingInvoker = new NonblockingInvoker();
                    try {
                        nonblockingInvoker.startClient();
                    } catch (TException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
