package com.xyw55.rpc.engine;

import com.xyw55.rpc.common.SerializeType;
import com.xyw55.rpc.serializer.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xyw55
 * @date 2018/2/25
 */
public class SerializerEngine {
    public static final Map<SerializeType, ISerializer> serializerMap = new ConcurrentHashMap<>();

    //注册序列化工具类到serializerMap
    static {
        serializerMap.put(SerializeType.JSONSerializer, new JSONSerializer());
        serializerMap.put(SerializeType.XmlSerializer, new XmlSerializer());
        serializerMap.put(SerializeType.ThriftSerializer, new ThriftSerializer());
        serializerMap.put(SerializeType.ProtoBufSerializer, new ProtoBufSerializer());
    }

    /**
     * 序列化
     * @param obj
     * @param serializeType
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T obj, String serializeType) {
        SerializeType serialize = SerializeType.queryByType(serializeType);
        if (serialize == null) {
            throw new RuntimeException("serialize is null");
        }

        ISerializer serializer = serializerMap.get(serialize);
        if (serializer == null) {
            throw new RuntimeException("serializer error");
        }
        try {
            return serializer.serialize(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化
     * @param data
     * @param clazz
     * @param serializeType
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz, String serializeType) {
        SerializeType serialize = SerializeType.queryByType(serializeType);
        if (serialize == null) {
            throw new RuntimeException("serialize is null");
        }

        ISerializer serializer = serializerMap.get(serialize);

        if (serializer == null) {
            throw new RuntimeException("serializer error");
        }

        try {
            return serializer.deserialize(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
