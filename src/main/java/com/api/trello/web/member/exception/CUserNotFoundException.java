package com.api.trello.web.member.exception;

import com.api.trello.advice.exception.BusinessException;
import com.api.trello.advice.exception.ExceptionCode;

public class CUserNotFoundException extends BusinessException {

    public CUserNotFoundException() {
        super(ExceptionCode.USER_NOT_FOUND);
    }
}
