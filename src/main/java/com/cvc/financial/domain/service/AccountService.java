package com.cvc.financial.domain.service;

import com.cvc.financial.domain.exception.AccountNotFoundException;
import com.cvc.financial.domain.exception.ExistingEntityException;
import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional
    public Account saveAccount(Account account) {
        try {
            return accountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new ExistingEntityException();
        }
    }

    public Account findByAgencyAndAccountNumber(String agency, String accountNumber) {
        return accountRepository.findByAgencyAndAccountNumber(agency, accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(agency, accountNumber));
    }
}
