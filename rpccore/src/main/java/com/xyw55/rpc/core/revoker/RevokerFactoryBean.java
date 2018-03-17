package com.xyw55.rpc.core.revoker;

import com.xyw55.rpc.core.model.InvokerService;
import com.xyw55.rpc.core.model.ProviderService;
import com.xyw55.rpc.core.zookeeper.IRegisterCenter4Invoker;
import com.xyw55.rpc.core.zookeeper.RegisterCenter;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;

/**
 * @author xyw55
 * @date 2018/3/17
 */
public class RevokerFactoryBean implements FactoryBean, InitializingBean{

    /**
     * 服务接口
     */
    private Class<?> targetInterface;
    /**
     * 超时时间
     */
    private int timeout;
    /**
     * 服务bean
     */
    private Object serviceObject;
    /**
     * 负载均衡策略
     */
    private String clusterStrategy;
    /**
     * 服务提供者唯一标识
     */
    private String remoteAppKey;
    /**
     * 服务分组组名
     */
    private String groupName = "default";


    @Override
    public Object getObject() throws Exception {
        return serviceObject;
    }

    @Override
    public Class<?> getObjectType() {
        return targetInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //获取服务注册中心
        IRegisterCenter4Invoker registerCenter4Consume = RegisterCenter.singleton();
        //初始化服务提供者列表到本地缓存
        registerCenter4Consume.initProviderMap(remoteAppKey, groupName);
        //初始化Netty Channel
        Map<String, List<ProviderService>> providerMap = registerCenter4Consume.getServiceMetaMapData4Consume();

        if (MapUtils.isEmpty(providerMap)) {
            throw new RuntimeException("service provider list is empty");
        }

        NettyChannelPoolFactory.channelPoolFactoryInstance().initChannelPollFactory(providerMap);

        //获取服务端提供者代理对象
        RevokerProxyBeanFactory proxyBeanFactory = RevokerProxyBeanFactory.singleton(targetInterface, timeout, clusterStrategy);
        this.serviceObject = proxyBeanFactory.getProxy();

        //将消费者信心注册到注册中心
        InvokerService invoker = new InvokerService();
        invoker.setServiceItf(targetInterface);
        invoker.setRemoteAppKey(remoteAppKey);
        invoker.setGroupName(groupName);
        registerCenter4Consume.registerInvoker(invoker);

    }


    public Class<?> getTargetInterface() {
        return targetInterface;
    }

    public void setTargetInterface(Class<?> targetInterface) {
        this.targetInterface = targetInterface;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public void setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
    }

    public String getClusterStrategy() {
        return clusterStrategy;
    }

    public void setClusterStrategy(String clusterStrategy) {
        this.clusterStrategy = clusterStrategy;
    }

    public String getRemoteAppKey() {
        return remoteAppKey;
    }

    public void setRemoteAppKey(String remoteAppKey) {
        this.remoteAppKey = remoteAppKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
