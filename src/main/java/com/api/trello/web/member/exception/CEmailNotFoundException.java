package com.api.trello.web.member.exception;

import com.api.trello.advice.exception.BusinessException;
import com.api.trello.advice.exception.ExceptionCode;

public class CEmailNotFoundException extends BusinessException {

    public CEmailNotFoundException() {
        super(ExceptionCode.EMAIL_NOT_FOUND);
    }
}
