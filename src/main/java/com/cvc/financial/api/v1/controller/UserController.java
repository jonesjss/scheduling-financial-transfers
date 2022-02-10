package com.cvc.financial.api.v1.controller;

import com.cvc.financial.api.ResourceUriHelper;
import com.cvc.financial.api.v1.dto.UserInput;
import com.cvc.financial.api.v1.dto.UserOutput;
import com.cvc.financial.api.v1.mapper.UserMapper;
import com.cvc.financial.domain.exception.BusinessException;
import com.cvc.financial.domain.exception.UserNotFoundException;
import com.cvc.financial.domain.model.User;
import com.cvc.financial.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "v1/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserOutput> findAll() {
        var users = userService.findAll();
        return userMapper.toDto(users);
    }

    @GetMapping("/{id}")
    public UserOutput findById(@PathVariable Long id) {
        try {
            User user = userService.findById(id);

            return userMapper.toDto(user);
        } catch(UserNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestBody UserInput userInput) {
        User user = userMapper.toModel(userInput);
        user = userService.save(user);

        ResourceUriHelper.addLocationInUriResponseHeader(user.getId());
    }
}
