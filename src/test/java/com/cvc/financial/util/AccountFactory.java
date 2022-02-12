package com.cvc.financial.util;

import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.model.User;

public class AccountFactory {

    private AccountFactory() {}

    public static Account createAccountOriginatingToBeSaved(User user) {
        var account =  Account.builder()
                                    .user(user)
                                    .agency("0001")
                                    .accountNumber("123456789")
                                    .build();
        user.addAccount(account);
        return account;
    }

    public static Account createAccountOriginatingSaved(User user) {
        var account =  Account.builder()
                                    .id(1L)
                                    .user(user)
                                    .agency("0001")
                                    .accountNumber("123456")
                                    .build();
        user.addAccount(account);
        return account;
    }

    public static Account createAccountDestinationToBeSaved(User user) {
        var account =  Account.builder()
                                    .user(user)
                                    .agency("0002")
                                    .accountNumber("456841")
                                    .build();
        user.addAccount(account);
        return account;
    }

    public static Account createAccountDestinationSaved(User user) {
        var account =  Account.builder()
                                    .id(2L)
                                    .user(user)
                                    .agency("0002")
                                    .accountNumber("987654")
                                    .build();
        user.addAccount(account);
        return account;
    }
}
