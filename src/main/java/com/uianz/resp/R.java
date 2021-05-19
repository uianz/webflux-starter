package com.uianz.resp;

import lombok.Data;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

/**
 * @author uianz
 * @date 2021/5/14
 */
@Data
@Accessors(chain = true)
public class R<T> implements Serializable {

    private Integer code;

    private String message;

    private Object data;

    public R() {
    }

    public static <T> Mono<R<T>> ok() {
        return of(RCode.OK);
    }

    public static <T> Mono<R<T>> ok(Mono<T> data) {
        return of(RCode.OK, data);
    }

    public static <T> Mono<R<T>> ok(Flux<T> data) {
        return of(RCode.OK, data);
    }

    public static <T> Mono<R<T>> fail() {
        return of(RCode.SERVER_ERROR);
    }

    public static <T> Mono<R<T>> fail(String message) {
        return of(RCode.SERVER_ERROR.getCode(), message);
    }

    public static <T> Mono<R<T>> fail(Integer code, String message) {
        return of(code, message);
    }

    public static <T> Mono<R<T>> of(RCode respCode) {
        return of(respCode.getCode(), respCode.getMessage());
    }

    public static <T> Mono<R<T>> of(Integer code, String message) {
        return of(code, message, Mono.empty());
    }

    public static <T> Mono<R<T>> of(RCode respCode, Object data) {
        return of(respCode.getCode(), respCode.getMessage(), data);
    }

    public static <T> Mono<R<T>> of(Integer code, String message, Object data) {
        Mono monoData;
        if (data instanceof Mono) {
            monoData = (Mono) data;
        } else if (data instanceof Flux) {
            monoData = ((Flux) data).collectSortedList();
        } else {
            throw new RuntimeException("parse response error");
        }
        return monoData.map(d -> new R<T>()
                .setCode(code)
                .setMessage(message)
                .setData(d));
    }

}
