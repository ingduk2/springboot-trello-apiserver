package com.api.trello.web.test.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_findAll() {
        // given
        String email = "1@email.com";
        String name = "one";
        User one = User.builder()
                .email(email)
                .name(name)
                .build();

        // when
        User save = userRepository.save(one);
        List<User> all = userRepository.findAll();

        // then
        User user = all.get(0);
        assertEquals(email, user.getEmail());
    }

}