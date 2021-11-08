package com.api.trello.web.common.dto.resopnse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse<T> {

    private enum StatusCode {
        OK("success", 200, 200), //GET, PUT/PATCH
        CREATED("success created", 201, 201), //POST
        DELETE("success delete", 204, 204); //DELETE

        private String message;
        private int status;
        private int code;

        StatusCode(String message, int status, int code) {
            this.message = message;
            this.status = status;
            this.code = code;
        }
    }

    private String message;
    private int status;
    private int code;

    private T data;

    public static SuccessResponse success() {
        return success(null);
    }

    public static SuccessResponse success(Object data) {
        return of(StatusCode.OK, data);
    }

    public static SuccessResponse created(Object data) {
        return of(StatusCode.CREATED, data);
    }

    public static SuccessResponse delete(Object data) {
        return of(StatusCode.DELETE, data);
    }

    private static SuccessResponse of(StatusCode statusCode, Object data) {
        return SuccessResponse.builder()
                .message(statusCode.message)
                .status(statusCode.status)
                .code(statusCode.code)
                .data(data)
                .build();
    }
}
