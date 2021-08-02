package com.uianz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uianz.common.json.JsonUtil;
import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author uianz
 * @date 2021/7/29
 */
public class RedisTest extends TestBase {

    @Autowired
    private ReactiveRedisOperations<String, Object> redisOperations;

    @Autowired
    private ReactiveValueOperations<String, Object> redisValueOperations;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test() {
        var key = "test_key1";
        var obj = new TestObj()
                .setAge(18)
                .setName("uianz")
                .setList(List.of("a", "b", "c"))
                .setMap(Map.of(1, "a", 2, "b"))
                .setVavrList(io.vavr.collection.List.of("a", "b", "c"))
                .setVavrMap(HashMap.of(1, "a", 2, "b"))
                .setTuple2(Tuple.of("hi", false));

        var redisMono = redisValueOperations.set(key, obj)
                .doOnNext(next -> System.out.println("set value result = " + next))
                .then(redisValueOperations.get(key))
                .map(json -> JsonUtil.toObj(json, TestObj.class))
                .doOnNext(next -> System.out.println("get value result = " + next));
        block(redisMono);

    }


    @Data
    @Accessors(chain = true)
    public static class TestObj {
        private Integer age;
        private String name;
        private List<String> list;
        private Map<Integer, String> map;
        private io.vavr.collection.List<String> vavrList;
        private io.vavr.collection.Map<Integer, String> vavrMap;
        private Tuple2<String, Boolean> tuple2;
    }
}
