package com.api.trello.web.member.exception;

import com.api.trello.advice.exception.BusinessException;
import com.api.trello.advice.exception.ExceptionCode;

public class CMemberNotFoundException extends BusinessException {

    public CMemberNotFoundException() {
        super(ExceptionCode.MEMBER_NOT_FOUND);
    }
}
