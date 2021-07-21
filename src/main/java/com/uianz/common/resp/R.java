package com.uianz.common.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;

/**
 * @author uianz
 * @date 2021/5/14
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("R")
public class R<T> implements Serializable {

    private Integer code;

    private String message;

    private T data;

    public R() {
    }

    public static <T> Mono<R<T>> ok() {
        return of(RCode.OK);
    }

    public static <T> Mono<R<T>> ok(Mono<T> data) {
        return of(RCode.OK, data);
    }

    public static <T> Mono<R<List<T>>> ok(Flux<T> data) {
        return of(RCode.OK, data.collectList());
    }

    public static <T> Mono<R<T>> fail() {
        return of(RCode.SERVER_ERROR);
    }

    public static <T> R<T> failEntity(){
        return new R<T>().setCode(RCode.SERVER_ERROR.getCode()).setMessage(RCode.SERVER_ERROR.getMessage());
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

    public static <T> Mono<R<T>> of(RCode respCode, Mono<T> data) {
        return of(respCode.getCode(), respCode.getMessage(), data);
    }

    public static <T> Mono<R<T>> of(Integer code, String message, Mono<T> data) {
        var r = new R<T>().setCode(code).setMessage(message);
        return data.map(r::setData)
                .switchIfEmpty(Mono.just(r));
    }

    public static void main(String[] args) {
        var a = of(1, "error", Mono.empty());
        a.doOnNext(System.out::println).subscribe();
    }

}
