package com.xyw55.rpc.framework;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * 服务消费代理类
 */
public class ConsumerProxy {

    public static <T> T consume(final Class<T> interfaceClass, final String host, final int port) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = new Socket(host, port);
                        try {

                            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                            try {
                                // 请求方法
                                outputStream.writeUTF(method.getName());
                                // 请求参数
                                outputStream.writeObject(args);
                                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                                try {
                                    // 获取结果
                                    Object result = inputStream.readObject();
                                    if (result instanceof Throwable) {
                                        throw (Throwable) result;
                                    }
                                    return result;
                                } finally {
                                    inputStream.close();
                                }
                            } finally {
                                outputStream.close();
                            }
                        } finally {
                            socket.close();
                        }
                    }
                });
    }

}
