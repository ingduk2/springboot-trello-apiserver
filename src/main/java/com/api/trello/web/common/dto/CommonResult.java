package com.api.trello.web.common.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult {

    private boolean success;

    private int code;

    private String message;
}
