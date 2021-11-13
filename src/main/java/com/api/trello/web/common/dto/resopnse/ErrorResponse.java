package com.api.trello.web.common.dto.resopnse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse extends Header{

    @Builder
    public ErrorResponse(String message, int status, int code, List<CustomFieldError> customFieldErrors) {
        super(message, status, code);
        this.customFieldErrors = customFieldErrors;
    }

    @JsonProperty("errors")
    private List<CustomFieldError> customFieldErrors;

    public static ErrorResponse of(String message, int status, int code) {
        return ErrorResponse.builder()
                .message(message)
                .status(status)
                .code(code)
                .build();
    }

    public void setCustomFieldErrors(Errors errors) {
        this.customFieldErrors = getFieldErrors(errors.getFieldErrors());
    }

    private static List<CustomFieldError> getFieldErrors(List<FieldError> fieldErrors) {
        List<CustomFieldError> list = new ArrayList<>();

        fieldErrors.forEach(error -> list.add(CustomFieldError.builder()
                .field(error.getField())
                .value(error.getRejectedValue())
                .reason(error.getDefaultMessage())
                .build()));

        return list;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CustomFieldError {
        private String field;
        private Object value;
        private String reason;

        @Builder
        public CustomFieldError(String field, Object value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }

}
