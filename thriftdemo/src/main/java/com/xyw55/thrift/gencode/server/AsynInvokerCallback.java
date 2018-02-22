package com.xyw55.thrift.gencode.server;

import org.apache.thrift.async.AsyncMethodCallback;

import java.util.concurrent.CountDownLatch;

public class AsynInvokerCallback implements AsyncMethodCallback<String> {

    private CountDownLatch latch;

    public AsynInvokerCallback(CountDownLatch latch) {
        this.latch = latch;
    }


    @Override
    public void onComplete(String resoponse) {
        try {
            System.out.println("response : " + resoponse);
        } finally {
            latch.countDown();
        }
    }

    @Override
    public void onError(Exception e) {
        latch.countDown();
    }
}
