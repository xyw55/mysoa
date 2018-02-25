package com.xyw55.rpc.serializer;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;

/**
 * 使用thrift进行序列化，这里采用TBinaryProtocol，还可以使用TCompactProtocol,TJSONProtocol
 * @author xyw55
 * @date 2018/2/25
 */
public class ThriftSerializer implements ISerializer{
    @Override
    public <T> byte[] serialize(T obj) {
        try {
            if (!(obj instanceof TBase)) {
                throw new UnsupportedOperationException("not support obj type");
            }

            TSerializer serializer = new TSerializer(new TBinaryProtocol.Factory());
            return serializer.serialize((TBase) obj);
        } catch (TException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            if (!TBase.class.isAssignableFrom(clazz)) {
                throw new UnsupportedOperationException("not support obj type");
            }
            TBase o = (TBase) clazz.newInstance();
            TDeserializer deserializer = new TDeserializer(new TBinaryProtocol.Factory());
            deserializer.deserialize(o, data);
            return (T) o;
        } catch (IllegalAccessException | InstantiationException | TException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
