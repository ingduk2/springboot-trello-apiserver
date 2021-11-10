package com.api.trello.web.util;

import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

public class ControllerTestUtil {

    public static Class<? extends Exception> getApiResultExceptionClass(MvcResult result) {
        return Objects.requireNonNull(result.getResolvedException()).getClass();
    }
}
