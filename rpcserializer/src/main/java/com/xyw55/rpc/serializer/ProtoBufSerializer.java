package com.xyw55.rpc.serializer;

import com.google.protobuf.GeneratedMessageV3;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 使用protobuf序列化，利用反射处理
 * @author xyw55
 * @date 2018/2/25
 */
public class ProtoBufSerializer implements ISerializer{
    @Override
    public <T> byte[] serialize(T obj) {
        try {
            if (!(obj instanceof GeneratedMessageV3)) {
                throw new UnsupportedOperationException("not support obj type");
            }
            return (byte[]) MethodUtils.invokeExactMethod(obj, "toByteArray");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            if (!GeneratedMessageV3.class.isAssignableFrom(clazz)) {
                throw new UnsupportedOperationException("not support obj type");
            }
            Object o = MethodUtils.invokeExactMethod(clazz, "getDefaultInstance");
            return (T) MethodUtils.invokeExactMethod(o, "parseFrom", new Object[]{data});
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
