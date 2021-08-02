package com.uianz.common.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.r2dbc.postgresql.codec.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * @author uianz
 * @date 2021/7/30
 */
@Slf4j
@JsonComponent
public class CustomJsonSerializer extends StdSerializer<Json> {

    protected CustomJsonSerializer() {
        this(null);
    }

    protected CustomJsonSerializer(final Class<?> vc) {
        super(vc, true);
    }

    @Override
    public void serialize(Json value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(value.asString());
        var node = gen.getCodec().readTree(parser);
        serializers.defaultSerializeValue(node, gen);
    }
}
