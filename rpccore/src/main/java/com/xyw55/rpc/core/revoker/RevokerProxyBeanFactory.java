package com.xyw55.rpc.core.revoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 消费端bean代理工厂
 * @author xyw55
 * @date 2018/3/17
 */
public class RevokerProxyBeanFactory implements InvocationHandler{

    /**
     * 服务接口
     */
    private Class<?> targetInterface;

    /**
     * 客户端超时时间
     */
    private int consumeTimeout;

    /**
     * 客户端请求线程数
     */
    private int threadWorkerNumber = 10;

    /**
     * 负载均衡策略
     */
    private String clusterStrategy;

    public RevokerProxyBeanFactory(Class<?> targetInterface, int consumeTimeout, String clusterStrategy) {
        this.targetInterface = targetInterface;
        this.consumeTimeout = consumeTimeout;
        this.clusterStrategy = clusterStrategy;
    }

    private static volatile RevokerProxyBeanFactory singleton;

    public static RevokerProxyBeanFactory singleton(Class<?> targetInterface, int consumeTimeout, String clusterStrategy) {
        if (singleton == null) {
            synchronized (RevokerProxyBeanFactory.class) {
                if (singleton == null) {
                    singleton = new RevokerProxyBeanFactory(targetInterface, consumeTimeout, clusterStrategy);
                }
            }
        }
        return singleton;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return null;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getClass().getClassLoader(), new Class<?>[]{targetInterface}, this);
    }
}
