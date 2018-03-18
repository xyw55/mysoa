package com.xyw55.rpc.core.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author xyw55
 * @date 2018/3/18
 */
public class RpcReferenceNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("reference", new RevokerFactoryBeanDefinitionParser());
    }
}
