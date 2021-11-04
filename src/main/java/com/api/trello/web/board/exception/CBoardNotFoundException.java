package com.api.trello.web.board.exception;

import com.api.trello.advice.exception.BusinessException;
import com.api.trello.advice.exception.ExceptionCode;

public class CBoardNotFoundException extends BusinessException {

    public CBoardNotFoundException() {
        super(ExceptionCode.BOARD_NOT_FOUND);
    }
}
