package com.api.trello.advice;

import com.api.trello.advice.exception.BusinessException;
import com.api.trello.advice.exception.ExceptionCode;
import com.api.trello.web.common.dto.resopnse.ErrorResopnse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final MessageSource messageSource;

    /**
     * Null Point Exception 등 그 밖의 예외처리.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResopnse> defaultException(HttpServletRequest request, Exception e) {
        log.error("defaultException", e);
        ErrorResopnse response = getErrorResponse(ExceptionCode.UNKNOWN);

        return ResponseEntity.internalServerError().body(response);
    }

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResopnse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        ErrorResopnse response = getErrorResponse(ExceptionCode.INVALID_INPUT);
        response.setCustomFieldErrors(e.getBindingResult());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * BusinessException Handler
     * userNotFoundException, BoardNotFoundException등
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResopnse> handleBusinessException(BusinessException e) {
        log.error("handleCustomException", e);
        ErrorResopnse response = getErrorResponse(e.getExceptionCode());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 405 Method Not Allowed Handler
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResopnse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        ExceptionCode code = ExceptionCode.METHOD_NOT_ALLOWED;
        ErrorResopnse response = getErrorResponse(
                getStr(code.getMsg()) + request.getRequestURI(),
                getInt(code.getStatus()),
                getInt(code.getCode()));

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 404 Not Found Handler
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResopnse> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.error("handleNoHandlerFoundException", e);
        ExceptionCode code = ExceptionCode.NOT_FOUND;
        ErrorResopnse response = getErrorResponse(
                getStr(code.getMsg()) + request.getRequestURI(),
                getInt(code.getStatus()),
                getInt(code.getCode()));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    /**
     * msg, status, code 는 공통으로 자주써서 method로 작성.
     * errorList 는 valid쪽에서만 사용해서 valid는 set으로 변경.
     * - 여기에 BindingList 받아서 if null 이면 구분 처리가 더 나은지 모르겠음..
     */
    private ErrorResopnse getErrorResponse(ExceptionCode exceptionCode) {
        return getErrorResponse(getStr(exceptionCode.getMsg()),
                getInt(exceptionCode.getStatus()),
                getInt(exceptionCode.getCode()));
    }

    private ErrorResopnse getErrorResponse(String msg, int status, int code) {
        return ErrorResopnse.of(msg, status, code);
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
