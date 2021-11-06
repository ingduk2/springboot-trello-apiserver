package com.api.trello.web.member.exception;

import com.api.trello.advice.exception.BusinessException;
import com.api.trello.advice.exception.ExceptionCode;

public class CEmailExistException extends BusinessException {

    public CEmailExistException() {
        super(ExceptionCode.EMAIL_EXIST);
    }
}
