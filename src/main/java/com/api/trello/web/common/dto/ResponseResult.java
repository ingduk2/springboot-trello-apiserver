package com.api.trello.web.common.dto;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseResult<T> {

    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMessage(CommonResponse.SUCCESS.getMessage());
        return result;
    }

    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMessage(CommonResponse.SUCCESS.getMessage());
        return result;
    }

    public CommonResult getSuccessResult() {
        return CommonResult.builder()
                .success(true)
                .code(CommonResponse.SUCCESS.getCode())
                .message(CommonResponse.SUCCESS.getMessage())
                .build();
    }

    public CommonResult getFailResult(int code, String msg) {
        return CommonResult.builder()
                .success(false)
                .code(code)
                .message(msg)
                .build();
    }
}
