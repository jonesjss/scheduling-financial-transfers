package com.cvc.financial.domain.service;

import com.cvc.financial.domain.exception.UserAccountNotFoundException;
import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserAccountService {
    private final AccountRepository accountRepository;

    public Set<Account> findByUser(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account findByIdAndUserID(Long id, Long userId) {
        return accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new UserAccountNotFoundException(id, userId));
    }
}
