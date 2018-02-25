package com.xyw55.rpc.invoke;

import com.xyw55.rpc.framework.ConsumerProxy;
import com.xyw55.rpc.service.HelloService;

public class RpcConsumerMain {
    public static void main(String[] args) {
        HelloService helloService = ConsumerProxy.consume(HelloService.class, "127.0.0.1", 8801);
        for (int i = 0; i < 10; i++) {
            String result = helloService.sayHello("xyw55-" + i);
            System.out.println(result);
        }
    }
}
