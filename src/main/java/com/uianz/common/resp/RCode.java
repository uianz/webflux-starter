package com.uianz.common.resp;
import lombok.AllArgsConstructor;

import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RCode {

    /**
     * 成功
     */
    OK(200, "ok!"),

    /**
     * 服务器错误
     */
    SERVER_ERROR(500, "server error!"),

    ;
    private final Integer code;

    private final String message;

}
