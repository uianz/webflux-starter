package com.uianz.modules.person.bean;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.uianz.common.json.CustomJsonSerializer;
import com.uianz.modules.person.config.PersonDeserializer;
import io.r2dbc.postgresql.codec.Json;
import lombok.Value;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author uianz
 * @date 2021/5/14
 */
@Table
@Value
@Accessors(chain = true)
public class Person {
    //    @With
    @Id
    Integer id;
    String name;
    Integer age;
    @JsonDeserialize(using = PersonDeserializer.class)
    public Json data;
}
