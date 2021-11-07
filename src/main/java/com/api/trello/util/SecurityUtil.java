package com.api.trello.util;

import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class SecurityUtil {

    /**
     * jwt 인증 구현 전까지 임시로 사용
     */
    public static Optional<String> getCurrentUsername() {
        return Optional.of("ingduk2@gmail.com");
    }

}
