package com.xyw55.rpc.core.provider;

/**
 * @author xyw55
 * @date 2018/3/17
 */
public class NettyServer {

    private static NettyServer nettyServer = new NettyServer();


    private NettyServer() {

    }

    public static NettyServer singleton() {
        return nettyServer;
    }

    public void start(final int port) {

    }
}
