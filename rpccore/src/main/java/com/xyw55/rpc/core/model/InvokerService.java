package com.xyw55.rpc.core.model;

import lombok.Data;

/**
 * @author xyw55
 * @date 2018/3/17
 */
@Data
public class InvokerService {
    /**
     * 服务接口
     */
    private Class<?> serviceItf;

    /**
     * 服务提供者唯一标识
     */
    private String remoteAppKey;

    /**
     * 服务分组组名
     */
    private String groupName;
}
