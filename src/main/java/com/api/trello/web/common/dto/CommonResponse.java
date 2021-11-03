package com.api.trello.web.common.dto;

import lombok.Getter;

@Getter
public enum CommonResponse {
    SUCCESS(0, "성공하였습니다."),
    FAIL(-1, "실패하였습니다.");

    private int code;
    private String message;

    CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
