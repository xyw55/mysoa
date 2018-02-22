package com.xyw55.thrift.gencode.server;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class SimpleInvoker {
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
         * 2.创建transport， 阻塞通信
         */
        int port = 8801;
        TServerSocket serverSocket = new TServerSocket(port);

        /**
         * 3.创建protocol
         */
        TBinaryProtocol.Factory protocal = new TBinaryProtocol.Factory();

        /**
         * 4.将processor transport protocal 设置到服务器中
         */
        TServer.Args args = new TServer.Args(serverSocket);
        args.processor(tProcessor);
        args.protocolFactory(protocal);

        TServer server = new TSimpleServer(args);
        server.serve();

    }
    /**
     * 客户端调用服务端
     */

    public void startClient() throws TException {
        String ip = "127.0.0.1";
        int port = 8801;
        int timeOut = 1000;
        /**
         * 1.创建transport
         */
        TTransport transport = new TSocket(ip, port, timeOut);
        /**
         * 2.创建protocol
         */
        TProtocol protocol = new TBinaryProtocol(transport);
        /**
         * 3.基于transport和protocol创建client
         */
        HelloService.Client client = new HelloService.Client(protocol);
        transport.open();


        User user = new User("55", "xyw5.com");
        String response = client.sayHello(user);
        System.out.println("response: " + response);

    }

    public static void main(String[] args) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SimpleInvoker simpleInvoker = new SimpleInvoker();
                    try {
                        simpleInvoker.startServer();
                    } catch (TTransportException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Thread.sleep(5000);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SimpleInvoker simpleInvoker = new SimpleInvoker();
                    try {
                        simpleInvoker.startClient();
                    } catch (TException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
