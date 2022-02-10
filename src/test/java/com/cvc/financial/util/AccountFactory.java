package com.cvc.financial.util;

import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.model.User;

public class AccountFactory {

    private AccountFactory() {}

    public static Account createAccountToBeSaved(User user) {
        return Account.builder()
                .user(user)
                .agency("0001")
                .accountNumber("123456")
                .build();
    }
}
