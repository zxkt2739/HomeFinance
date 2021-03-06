package com.example.core.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class LongDateSerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(value));
        jsonGenerator.writeString(format);
    }
}
