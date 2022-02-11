package com.cvc.financial.api.v1.controller;

import com.cvc.financial.api.ResourceUriHelper;
import com.cvc.financial.api.v1.dto.AccountInput;
import com.cvc.financial.api.v1.dto.AccountOutput;
import com.cvc.financial.api.v1.mapper.AccountMapper;
import com.cvc.financial.api.v1.mapper.UserMapper;
import com.cvc.financial.domain.exception.BusinessException;
import com.cvc.financial.domain.exception.EntityNotFoundException;
import com.cvc.financial.domain.exception.UserAccountNotFoundException;
import com.cvc.financial.domain.exception.UserNotFoundException;
import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.service.AccountService;
import com.cvc.financial.domain.service.UserAccountService;
import com.cvc.financial.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "v1/users/{userId}/accounts")
public class UserAccountController {
    private final UserService userService;
    private final AccountService accountService;
    private final UserAccountService userAccountService;
    private final AccountMapper accountMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AccountOutput> findAll(@PathVariable Long userId) {
        Set<Account> users = userAccountService.findByUser(userId);
        return accountMapper.toDTO(users);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountOutput findByIdAndUserId(@PathVariable Long userId, @PathVariable Long id) {
        Account user = userAccountService.findByIdAndUserID(id, userId);

        return accountMapper.toDTO(user);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAccount(@PathVariable Long userId, @Valid @RequestBody AccountInput accountInput) {
        try {
            var user = userService.findById(userId);
            Account account = accountMapper.toModel(accountInput);
            account.setUser(user);

            account = accountService.saveAccount(account);

            ResourceUriHelper.addLocationInUriResponseHeader(account.getId());
        } catch (UserNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
