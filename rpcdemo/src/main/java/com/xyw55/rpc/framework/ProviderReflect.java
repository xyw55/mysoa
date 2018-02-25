package com.xyw55.rpc.framework;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 服务发布类
 */
public class ProviderReflect {

    /**
     * 处理请求线程池
     */
    private static final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(20,
            new BasicThreadFactory.Builder().namingPattern("provider-pool-%d").daemon(true).build());

    public static void provider(final Object service, int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            final Socket socket = serverSocket.accept();
            scheduledExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {

                        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                        try {
                            try {
                                // 获取方法名称
                                String method = inputStream.readUTF();
                                // 获取请求参数
                                Object[] args = (Object[]) inputStream.readObject();
                                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                                try {
                                    // 反射调用方法
                                    Object result = MethodUtils.invokeExactMethod(service, method, args);
                                    // 输出结果
                                    outputStream.writeObject(result);
                                } catch (Throwable t) {
                                    outputStream.writeObject(t);
                                } finally {
                                    outputStream.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                inputStream.close();
                            }
                        } finally {
                            socket.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }
}
