package com.xyw55.thrift.gencode.server;

import org.apache.thrift.TException;

public class HelloServiceImpl implements HelloService.Iface {

    @Override
    public String sayHello(User user) throws TException {
        return "Hello, " + user.getName() + ", your email is " + user.getEmail();
    }

}
