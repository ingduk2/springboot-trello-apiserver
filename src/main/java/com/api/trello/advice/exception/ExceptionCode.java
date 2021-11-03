package com.api.trello.advice.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    UNKNOWN("unKnown.msg", "unKnown.status", "unKnown.code"),
    INVALID_INPUT("inValidInput.msg", "inValidInput.status", "inValidInput.code"),
    USER_NOT_FOUND("userNotFound.msg", "userNotFound.status", "userNotFound.code");

    private String msg;
    private String status;
    private String code;

    ExceptionCode(String msg, String status, String code) {
        this.msg = msg;
        this.status = status;
        this.code = code;
    }
}
