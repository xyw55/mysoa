package com.xyw55.rpc.common;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xyw55
 * @date 2018/2/25
 */
public class FDateJsonDeserializer extends JsonDeserializer<Date>{

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String date = jsonParser.getText();
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        if (StringUtils.isNumeric(date)) {
            return new Date(Long.valueOf(date));
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy=MM-dd HH:mm:ss");
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }
}
