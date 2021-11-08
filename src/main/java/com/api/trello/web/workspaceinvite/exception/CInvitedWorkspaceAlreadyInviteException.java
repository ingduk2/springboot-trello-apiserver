package com.api.trello.web.workspaceinvite.exception;

import com.api.trello.advice.exception.BusinessException;
import com.api.trello.advice.exception.ExceptionCode;

public class CInvitedWorkspaceAlreadyInviteException extends BusinessException {


    public CInvitedWorkspaceAlreadyInviteException() {
        super(ExceptionCode.INVITED_WORKSPACE_ALREADY);
    }
}
