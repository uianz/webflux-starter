package com.uianz.common.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uianz.modules.person.bean.Person;
import com.uianz.modules.person.config.PersonDeserializer;
import io.r2dbc.postgresql.codec.Json;
import io.vavr.jackson.datatype.VavrModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

/**
 * @author uianz
 * @date 2021/7/29
 */
@Configuration
public class JacksonConfig {

    @Bean
    Jackson2JsonEncoder jackson2JsonEncoder(ObjectMapper mapper) {
        return new Jackson2JsonEncoder(mapper);
    }

    @Bean
    Jackson2JsonDecoder jackson2JsonDecoder(ObjectMapper mapper) {
        return new Jackson2JsonDecoder(mapper);
    }

    @Bean
    @Order(0)
    @Primary
    public ObjectMapper objectMapper() {
//        var pgJsonModule = new SimpleModule();
//        pgJsonModule.addSerializer(Json.class, new CustomJsonSerializer());
//        pgJsonModule.addDeserializer(Json.class, new CustomJsonDeserializer());

        return new ObjectMapper()
                //禁止序列化值为 null 的属性
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                //禁止序列化时间为时间戳
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModules(
                        //注册 Jsr310（Java8 的时间兼容模块）
                        new JavaTimeModule(),
                        new Jdk8Module(),
                        new VavrModule(),
                        new CustomJsonModule(),
                        new SimpleModule().addDeserializer(Person.class, new PersonDeserializer())
//                        pgJsonModule
                )
                .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
                ;
    }

    public static class CustomJsonModule extends SimpleModule {
        public CustomJsonModule() {
            this.addSerializer(Json.class, new CustomJsonSerializer());
            this.addDeserializer(Json.class, new CustomJsonDeserializer());
        }
    }

}
