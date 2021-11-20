package com.api.trello.web.list.exception;

import com.api.trello.advice.exception.BusinessException;
import com.api.trello.advice.exception.ExceptionCode;

public class CListNotFoundException extends BusinessException {
    public CListNotFoundException() {
        super(ExceptionCode.LIST_NOT_FOUND);
    }
}
