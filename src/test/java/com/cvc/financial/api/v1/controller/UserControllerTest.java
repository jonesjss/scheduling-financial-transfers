package com.cvc.financial.api.v1.controller;

import com.cvc.financial.api.v1.dto.UserOutput;
import com.cvc.financial.api.v1.mapper.UserMapper;
import com.cvc.financial.domain.exception.BusinessException;
import com.cvc.financial.domain.exception.UserNotFoundException;
import com.cvc.financial.domain.service.UserService;
import com.cvc.financial.util.UserFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("findById return user when successful")
    void findById_ReturnUser_WhenSuccessful() {
        BDDMockito.when(userService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(UserFactory.createUserToBeSaved());

        UserOutput userOutput = userController.findById(1L);

        Assertions.assertThat(userOutput).isNotNull();
    }

    @Test
    @DisplayName("findById throw business exception when user not found")
    void findById_ThrowBusinessException_WhenUserNotFound() {
        BDDMockito.when(userService.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new UserNotFoundException(1L));

        Assertions.assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> userController.findById(1L))
                .withMessage("User not found with id 1");
    }
}