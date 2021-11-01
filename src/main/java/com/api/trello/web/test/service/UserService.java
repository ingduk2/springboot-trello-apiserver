package com.api.trello.web.test.service;

import com.api.trello.web.test.domain.User;
import com.api.trello.web.test.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Long save(User user) {
        return userRepository.save(user).getId();
    }

    public User findById(Long id) {
        return userRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
