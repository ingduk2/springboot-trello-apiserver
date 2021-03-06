package com.api.trello.advice.exception;

public enum ExceptionCode {

    /**
     * resources/i18n yml파일의 name.
     * 처음에는 3개를 넣었다가 UNKNOWN("unKnown.msg", "unKnown.status", "unKnown.code"),
     * name 이 겹치기 때문에 get할때 msg, status, code 같이 반환하는것으로 변경.
     */
    UNKNOWN("unKnown"),
    INVALID_INPUT("inValidInput"),
    MEMBER_NOT_FOUND("memberNotFound"),
    BOARD_NOT_FOUND("boardNotFound"),
    METHOD_NOT_ALLOWED("methodNotAllowed"),
    NOT_FOUND("notFound"),
    EMAIL_EXIST("emailExist"),
    EMAIL_NOT_FOUND("emailNotFound"),
    PASSWORD_NOT_MATCH("passwordNotMatch"),
    JSON_PARSER("jsonParser"),
    WORKSPACE_NOT_FOUND("workspaceNotFound"), 
    INVITED_WORKSPACE_MEMBER_IS_WORKSPACE_OWNER("invitedWorkspaceMemberIsWorkspaceOwner"),
    INVITED_WORKSPACE_ALREADY("invitedWorkspaceAlready"),
    LIST_NOT_FOUND("listNotFound");

    private String name;

    ExceptionCode(String name) {
        this.name = name;
    }

    public String getMsg() {
        return this.name + ".msg";
    }

    public String getStatus() {
        return this.name + ".status";
    }

    public String getCode() {
        return this.name + ".code";
    }
}
