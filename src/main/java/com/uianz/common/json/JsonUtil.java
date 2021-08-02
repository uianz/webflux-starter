package com.uianz.common.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author uianz
 * @date 2021/7/29
 */
@Configuration
public class JsonUtil {

    private static ObjectMapper mapper;

    @Autowired
    private ObjectMapper aMapper;

    @PostConstruct
    public void init() {
        mapper = aMapper;
    }

    /**
     * 将对象转换成json字符串。
     */
    public static String toJsonStr(Object data) {
        return Try.of(() -> mapper.writeValueAsString(data))
//                .onFailure(Throwable::printStackTrace)
                .get();
    }

    /**
     * 将json结果集转化为对象
     */
    public static <T> T toObj(String jsonData, Class<T> clazz) {
        return Try.of(() -> mapper.readValue(jsonData, clazz))
//                .onFailure(Throwable::printStackTrace)
                .get();
    }

    public static <T> T toObj(Object jsonData, Class<T> clazz) {
        return Try.of(() -> mapper.readValue(toJsonStr(jsonData), clazz))
//                .onFailure(Throwable::printStackTrace)
                .get();
    }

    public static <T> T toObj(String jsonData, TypeReference<T> valueTypeRef) {
        return Try.of(() -> mapper.readValue(jsonData, valueTypeRef))
//                .onFailure(Throwable::printStackTrace)
                .get();
    }

    public static <T> T toObj(byte[] jsonData, TypeReference<T> valueTypeRef) {
        return Try.of(() -> mapper.readValue(jsonData, valueTypeRef))
//                .onFailure(Throwable::printStackTrace)
                .get();
    }

    public static <T> T toObj(byte[] jsonData, Class<T> clazz) {
        return Try.of(() -> mapper.readValue(jsonData, clazz))
//                .onFailure(Throwable::printStackTrace)
                .get();
    }

    public static <T> T toObj(JsonNode jsonNode, Class<T> clazz) {
        return toObj(jsonNode.toString(), clazz);
    }

    public static JsonNode toJsonNode(String jsonData) {
        return toObj(jsonData, JsonNode.class);
    }

    public static JsonNode toJsonNode(Object obj) {
        return toObj(toJsonStr(obj), JsonNode.class);
    }

    public static <T> List<T> toList(String jsonData, Class<T> beanType) {
        var javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);
        return Try.of(() -> mapper.readValue(jsonData, javaType))
                .mapTry(obj -> (List<T>) obj)
//                .onFailure(Throwable::printStackTrace)
                .get();
    }

}
