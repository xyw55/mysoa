package com.xyw55.rpc.serializer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 使用java自带的XMLEncoder, XMLDecoder
 * @author xyw55
 * @date 2018/2/25
 */
public class Xml2Serializer implements ISerializer{

    @SuppressWarnings("unchecked")
    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLEncoder xmlEncoder = new XMLEncoder(out, "UTF-8", true, 0);
        xmlEncoder.writeObject(obj);
        xmlEncoder.close();
        return out.toByteArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        XMLDecoder xmlDecoder = new XMLDecoder(new ByteArrayInputStream(data));
        Object obj = xmlDecoder.readObject();
        xmlDecoder.close();
        return (T) obj;
    }
}
