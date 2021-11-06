package com.api.trello.web.member.exception;

import com.api.trello.advice.exception.BusinessException;
import com.api.trello.advice.exception.ExceptionCode;

public class CPasswordNotMatchException extends BusinessException {

    public CPasswordNotMatchException() {
        super(ExceptionCode.PASSWORD_NOT_MATCH);
    }
}
