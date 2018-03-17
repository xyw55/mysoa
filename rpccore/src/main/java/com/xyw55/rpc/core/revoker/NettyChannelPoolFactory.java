package com.xyw55.rpc.core.revoker;

import com.xyw55.rpc.core.model.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * @author xyw55
 * @date 2018/3/17
 */
public class NettyChannelPoolFactory {

    private static NettyChannelPoolFactory nettyChannelPoolFactory = new NettyChannelPoolFactory();

    private NettyChannelPoolFactory(){}


    public static NettyChannelPoolFactory channelPoolFactoryInstance(){
        return nettyChannelPoolFactory;
    }

    public void initChannelPollFactory(Map<String, List<ProviderService>> providerMap) {

    }

}
