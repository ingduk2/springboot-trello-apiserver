package com.api.trello.web.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> extends CommonResult{
    private T data;
}
