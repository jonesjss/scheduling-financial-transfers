package com.cvc.financial.api.v1.controller;

import com.cvc.financial.api.ResourceUriHelper;
import com.cvc.financial.api.openapi.controller.UserControllerOpenApi;
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
@RequestMapping(value = "v1/users")
public class UserController implements UserControllerOpenApi {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserOutput> findAll() {
        var users = userService.findAll();
        return userMapper.toDto(users);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserOutput findById(@PathVariable Long id) {
        User user = userService.findById(id);

        return userMapper.toDto(user);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestBody UserInput userInput) {
        User user = userMapper.toModel(userInput);
        user = userService.save(user);

        ResourceUriHelper.addLocationInUriResponseHeader(user.getId());
    }
}
