package com.uianz.resp;

import lombok.Data;
import lombok.experimental.Accessors;
import reactor.core.publisher.Mono;

/**
 * @author uianz
 * @date 2021/5/14
 */
@Data
@Accessors(chain = true)
public class R<T> {

    private Integer code;

    private String message;

    private T data;

    public R() {
    }

    public static <T> Mono<R<T>> success() {
        return of(RespCode.SUCCEED);
    }

    public static <T> Mono<R<T>> success(T data) {
        return of(RespCode.SUCCEED, data);
    }

    public static <T> Mono<R<T>> fail() {
        return of(RespCode.SERVER_ERROR);
    }

    public static <T> Mono<R<T>> fail(String message) {
        return of(RespCode.SERVER_ERROR.getCode(), message);
    }

    public static <T> Mono<R<T>> fail(Integer code, String message) {
        return of(code, message);
    }

    public static <T> Mono<R<T>> of(RespCode respCode) {
        return of(respCode.getCode(), respCode.getMessage());
    }

    public static <T> Mono<R<T>> of(Integer code, String message) {
        return of(code, message, null);
    }

    public static <T> Mono<R<T>> of(RespCode respCode, T data) {
        return of(respCode.getCode(), respCode.getMessage(), data);
    }

    public static <T> Mono<R<T>> of(Integer code, String message, T data) {
        return Mono.just(new R<T>()
                .setCode(code)
                .setMessage(message)
                .setData(data)
        );
    }

}
