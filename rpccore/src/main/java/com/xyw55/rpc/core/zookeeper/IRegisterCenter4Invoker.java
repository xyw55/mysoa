package com.xyw55.rpc.core.zookeeper;

import com.xyw55.rpc.core.model.InvokerService;
import com.xyw55.rpc.core.model.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * 消费端注册中心接口
 * @author xyw55
 * @date 2018/3/17
 */
public interface IRegisterCenter4Invoker {
    /**
     * 消费端初始化服务提供者信息，本地缓存
     * @param remoteAppKey
     * @param groupName
     */
    void initProviderMap(String remoteAppKey, String groupName);

    /**
     * 消费端获取服务提供者信息
     * @return
     */
    Map<String, List<ProviderService>> getServiceMetaMapData4Consume();


    /**
     * 消费端将消费者信息注册到ZK对应的节点下
     * @param invokerService
     */
    void registerInvoker(final InvokerService invokerService);
}
