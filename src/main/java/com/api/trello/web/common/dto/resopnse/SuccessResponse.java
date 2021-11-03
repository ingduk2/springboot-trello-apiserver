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

    private String message;
    private int status;
    private int code;

    private T data;

    public static SuccessResponse success() {
        return of(null);
    }

    public static SuccessResponse of(Object data) {
        return SuccessResponse.builder()
                .message("success")
                .status(200)
                .code(200)
                .data(data)
                .build();
    }
}
