package com.api.trello.web.workspaceinvite.exception;

import com.api.trello.advice.exception.BusinessException;
import com.api.trello.advice.exception.ExceptionCode;

public class CInvitedWorkspaceMemberIsWorkspaceOwnerException extends BusinessException {

    public CInvitedWorkspaceMemberIsWorkspaceOwnerException() {
        super(ExceptionCode.INVITED_WORKSPACE_MEMBER_IS_WORKSPACE_OWNER);
    }
}
