package com.xyw55.rpc.core.zookeeper;

import com.xyw55.rpc.core.model.InvokerService;
import com.xyw55.rpc.core.model.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * @author xyw55
 * @date 2018/3/17
 */
public interface IRegisterCenter4Invoker {
    void initProviderMap(String remoteAppKey, String groupName);

    Map<String, List<ProviderService>> getServiceMetaMapData4Consume();


    void registerInvoker(InvokerService invokerService);
}
