package com.cvc.financial.domain.service;

import com.cvc.financial.domain.exception.UserNotFoundException;
import com.cvc.financial.domain.repository.UserRepository;
import com.cvc.financial.util.UserFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for UserService")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("findById return user when successful")
    void findById_ReturnUser_WhenSuccessful() {
        BDDMockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(UserFactory.createUserOrigemToBeSaved()));

        var user = userService.findById(1L);

        Assertions.assertThat(user).isNotNull();
    }

    @Test
    @DisplayName("findById throw not found exception when user not found")
    void findById_ThrowUserNotFoundException_WhenUserNotFound() {
        BDDMockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.findById(1L))
                .withMessage("User not found with id 1");
    }


    @Test
    @DisplayName("findById throw not found exception when user not found")
    void save_ReturnUser_WhenUserIsSaved() {
        var user = UserFactory.createUserOrigemToBeSaved();
        BDDMockito.when(userRepository.save(user))
                .thenReturn(user);

        var userSaved = userService.save(user);

        Assertions.assertThat(userSaved).isNotNull();
    }
}