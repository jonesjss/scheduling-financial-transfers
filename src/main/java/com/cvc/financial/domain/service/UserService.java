package com.cvc.financial.domain.service;

import com.cvc.financial.domain.exception.UserNotFoundException;
import com.cvc.financial.domain.model.User;
import com.cvc.financial.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
}
