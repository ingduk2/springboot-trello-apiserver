package com.api.trello.advice;

import com.api.trello.advice.exception.BusinessException;
import com.api.trello.advice.exception.ExceptionCode;
import com.api.trello.web.common.dto.resopnse.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    protected ResponseEntity<ErrorResponse> defaultException(HttpServletRequest request, Exception e) {
        log.error("defaultException", e);
        ErrorResponse response = getErrorResponse(ExceptionCode.UNKNOWN);

        return ResponseEntity.internalServerError().body(response);
    }

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        ErrorResponse response = getErrorResponse(ExceptionCode.INVALID_INPUT);
        response.setCustomFieldErrors(e.getBindingResult());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * BusinessException Handler
     * userNotFoundException, BoardNotFoundException등
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("handleCustomException", e);
        ErrorResponse response = getErrorResponse(e.getExceptionCode());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 405 Method Not Allowed Handler
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        ExceptionCode code = ExceptionCode.METHOD_NOT_ALLOWED;
        ErrorResponse response = getErrorResponse(
                getStr(code.getMsg()) + request.getRequestURI(),
                getInt(code.getStatus()),
                getInt(code.getCode()));

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 404 Not Found Handler
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.error("handleNoHandlerFoundException", e);
        ExceptionCode code = ExceptionCode.NOT_FOUND;
        ErrorResponse response = getErrorResponse(
                getStr(code.getMsg()) + request.getRequestURI(),
                getInt(code.getStatus()),
                getInt(code.getCode()));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * JsonParseException 안잡혀서 HttpMessageNotReadableException 로 잡으니잡히네..
     * 처음것만 잡히는건가..?
     * 수정 필요
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleJsonParseException(HttpMessageNotReadableException e) {
        log.error("JsonParseException", e);
        ExceptionCode code = ExceptionCode.JSON_PARSER;
        ErrorResponse response = getErrorResponse(
                getStr(code.getMsg()),
                getInt(code.getStatus()),
                getInt(code.getCode()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * msg, status, code 는 공통으로 자주써서 method로 작성.
     * errorList 는 valid쪽에서만 사용해서 valid는 set으로 변경.
     * - 여기에 BindingList 받아서 if null 이면 구분 처리가 더 나은지 모르겠음..
     */
    private ErrorResponse getErrorResponse(ExceptionCode exceptionCode) {
        return getErrorResponse(getStr(exceptionCode.getMsg()),
                getInt(exceptionCode.getStatus()),
                getInt(exceptionCode.getCode()));
    }

    private ErrorResponse getErrorResponse(String msg, int status, int code) {
        return ErrorResponse.of(msg, status, code);
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
