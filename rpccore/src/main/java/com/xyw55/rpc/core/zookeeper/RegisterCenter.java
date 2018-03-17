package com.xyw55.rpc.core.zookeeper;

import com.xyw55.rpc.core.model.InvokerService;
import com.xyw55.rpc.core.model.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * @author xyw55
 * @date 2018/3/17
 */
public class RegisterCenter implements IRegisterCenter4Provider, IRegisterCenter4Invoker{

    private static RegisterCenter registerCenter = new RegisterCenter();

    private RegisterCenter() {

    }

    public static RegisterCenter singleton() {
        return registerCenter;
    }

    @Override
    public void registerProvider(List<ProviderService> serviceMetaData) {

    }

    @Override
    public Map<String, List<ProviderService>> getProviderServiceMap() {
        return null;
    }


    @Override
    public void initProviderMap(String remoteAppKey, String groupName) {

    }

    @Override
    public Map<String, List<ProviderService>> getServiceMetaMapData4Consume() {
        return null;
    }

    @Override
    public void registerInvoker(InvokerService invokerService) {

    }
}
