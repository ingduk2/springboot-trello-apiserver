package com.api.trello.web.common.dto.resopnse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Header {
    private String message;
    private int status;
    private int code;
}
