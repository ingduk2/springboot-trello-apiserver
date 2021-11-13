package com.api.trello.web.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

public class ControllerTestUtil {

    public static Class<? extends Exception> getApiResultExceptionClass(MvcResult result) {
        return Objects.requireNonNull(result.getResolvedException()).getClass();
    }

    public static String getJsonStringByDto(Object dto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(dto);
    }
}
