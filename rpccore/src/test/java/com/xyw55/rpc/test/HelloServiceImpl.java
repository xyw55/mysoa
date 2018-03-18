package com.xyw55.rpc.test;

/**
 * @author xyw55
 * @date 2018/3/18
 */
public class HelloServiceImpl implements HelloService{
    @Override
    public String sayHello(String name) {
        return "hello, " + name;
    }

}
