package com.xyw55.rpc.core.model;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author xyw55
 * @date 2018/3/17
 */
@Data
public class ProviderService {
    /**
     * 服务接口
     */
    private Class<?> serviceItf;

    /**
     * 服务实现
     */
    private Object serviceObject;

    /**
     * 服务IP
     */
    private String serverIp;

    /**
     * 服务端口
     */
    private int serverPort;

    /**
     * 服务超时时间
     */
    private long timeout;

    /**
     * 服务提供方法
     */
    private Method ServiceMethod;

    /**
     * 服务提供者唯一标识
     */
    private String appKey;

    /**
     * 服务分组组名
     */
    private String groupName;

    /**
     * 服务提供者权重，默认1，范围[1-100]
     */
    private int weight;

    /**
     * 服务端线程数，默认是个线程
     */
    private int workerThreads;
}
