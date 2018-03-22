package com.xyw55.rpc.core.zookeeper;

import com.xyw55.rpc.core.model.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * 服务端注册中心接口
 *
 * @author xyw55
 * @date 2018/3/17
 */
public interface IRegisterCenter4Provider {
    /**
     * 服务端将服务提供者信息注册到zk对应的节点下
     *
     * @param serviceMetaData
     */
    void registerProvider(final List<ProviderService> serviceMetaData);

    /**
     * 服务端获取服务提供者的信息
     * 注：返回对象，Key为服务提供者接口，Value为服务提供者服务方法列表
     *
     * @return
     */
    Map<String, List<ProviderService>> getProviderServiceMap();
}
