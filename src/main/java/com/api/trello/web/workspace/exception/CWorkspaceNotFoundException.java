package com.api.trello.web.workspace.exception;

import com.api.trello.advice.exception.BusinessException;
import com.api.trello.advice.exception.ExceptionCode;

public class CWorkspaceNotFoundException extends BusinessException {

    public CWorkspaceNotFoundException() {
        super(ExceptionCode.WORKSPACE_NOT_FOUND);
    }
}
