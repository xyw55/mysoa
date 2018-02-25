package com.xyw55.rpc.serializer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 使用XStream
 * @author xyw55
 * @date 2018/2/25
 */
public class XmlSerializer implements ISerializer{
    /**
     * 初始化Xstream对象
     */
    private static final XStream xstream = new XStream(new DomDriver());


    @SuppressWarnings("unchecked")
    @Override
    public <T> byte[] serialize(T obj) {
        return xstream.toXML(obj).getBytes();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        String xml = new String(data);
        return (T) xstream.fromXML(xml);
    }
}
