package com.uianz.common.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * @author uianz
 * @date 2021/8/2
 */
@JsonComponent
public class CustomJsonDeserializer extends StdDeserializer<Json> {
    protected CustomJsonDeserializer() {
        super(Json.class);
    }

    @Override
    public Json deserialize(JsonParser jp, DeserializationContext ds) throws IOException {
        return Json.of(jp.getText());
    }
}
