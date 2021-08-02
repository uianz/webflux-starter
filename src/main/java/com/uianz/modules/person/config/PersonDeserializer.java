package com.uianz.modules.person.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.uianz.common.json.JsonUtil;
import com.uianz.modules.person.bean.Person;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class PersonDeserializer extends StdDeserializer<Person> {
    public PersonDeserializer() {
        super(Json.class);
    }

    @Override
    public Person deserialize(JsonParser jp, DeserializationContext ds) throws IOException {
        return JsonUtil.toObj(jp.getText(), Person.class);
    }
}