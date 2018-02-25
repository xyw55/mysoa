package com.xyw55.rpc.invoke;

import com.xyw55.rpc.framework.ProviderReflect;
import com.xyw55.rpc.service.HelloService;
import com.xyw55.rpc.service.HelloServiceImpl;

import java.io.IOException;

public class RpcProviderMain {
    public static void main(String[] args) throws IOException {
        HelloService service = new HelloServiceImpl();
        ProviderReflect.provider(service, 8801);
    }
}
