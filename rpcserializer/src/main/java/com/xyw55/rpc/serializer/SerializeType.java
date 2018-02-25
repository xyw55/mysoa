package com.xyw55.rpc.serializer;

import org.apache.commons.lang3.StringUtils;

/**
 * @author xyw55
 * @date 2018/2/25
 */
public enum SerializeType {
    JSONSerializer("JSONSerializer"),
    XmlSerializer("XmlSerializer"),
    ProtoBufSerializer("ProtoBufSerializer"),
    ThriftSerializer("ThriftSerializer"),
    ;
    private String serializeType;

    SerializeType(String serializeType) {
        this.serializeType = serializeType;
    }

    public String getSerializeType() {
        return serializeType;
    }

    public static SerializeType queryByType(String serializeType) {
        if (StringUtils.isEmpty(serializeType)) {
            return null;
        }

        for (SerializeType serialize : SerializeType.values()) {
            if (StringUtils.equals(serializeType, serialize.getSerializeType())) {
                return serialize;
            }
        }
        return null;
    }

}
