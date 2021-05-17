package com.uianz.resp;
import lombok.AllArgsConstructor;

import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RespCode {

    /**
     * 成功
     */
    SUCCEED(200, "success!"),

    /**
     * 服务器错误
     */
    SERVER_ERROR(500, "server error!"),

    ;
    private final Integer code;

    private final String message;

}
