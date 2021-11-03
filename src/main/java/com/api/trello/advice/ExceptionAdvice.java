package com.api.trello.advice;

import com.api.trello.advice.exception.ExceptionCode;
import com.api.trello.web.common.dto.ResponseResult;
import com.api.trello.web.common.dto.resopnse.ErrorResopnse;
import com.api.trello.web.member.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseResult responseResult;

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResopnse> defaultException(HttpServletRequest request, Exception e) {
        ErrorResopnse response = ErrorResopnse.of(
                getStr(ExceptionCode.UNKNOWN.getMsg()),
                getInt(ExceptionCode.UNKNOWN.getStatus()),
                getInt(ExceptionCode.UNKNOWN.getCode())
        );

        return ResponseEntity.internalServerError().body(response);
    }

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResopnse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResopnse response = ErrorResopnse.of(
                getStr(ExceptionCode.INVALID_INPUT.getMsg()),
                getInt(ExceptionCode.INVALID_INPUT.getStatus()),
                getInt(ExceptionCode.INVALID_INPUT.getCode()),
                e.getBindingResult());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ErrorResopnse> userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        ErrorResopnse response = ErrorResopnse.of(
                getStr(ExceptionCode.USER_NOT_FOUND.getMsg()),
                getInt(ExceptionCode.USER_NOT_FOUND.getStatus()),
                getInt(ExceptionCode.USER_NOT_FOUND.getCode()));

        return ResponseEntity.badRequest().body(response);
    }

    private String getStr(String code) {
        return getMessage(code, null);
    }

    private int getInt(String code) {
        return Integer.parseInt(getMessage(code, null));
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
