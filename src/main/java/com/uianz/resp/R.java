package com.uianz.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

/**
 * @author uianz
 * @date 2021/5/14
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
        return of(code, message, Mono.never());
    }

    public static <T> Mono<R<T>> of(RCode respCode, Object data) {
        return of(respCode.getCode(), respCode.getMessage(), data);
    }

    @SuppressWarnings("unchecked")
    public static <T> Mono<R<T>> of(Integer code, String message, Object data) {
        var r = new R<T>().setCode(code).setMessage(message);
        return Match(data)
                .of(
                        Case($(instanceOf(Mono.class)), mono -> mono),
                        Case($(instanceOf(Flux.class)), Flux::collectSortedList),
//                        Case($(), Mono.error(() -> new RuntimeException("'data' is unsupported type")))
                        Case($(), Mono::just)
                )
                .map(r::setData)
                .or(Mono.just(r));
    }

    public static void main(String[] args) {
        var a = of(1, "error", Mono.never());
        a.doOnNext(System.out::println).subscribe();
    }

}
