package com.cvc.financial.util;

import com.cvc.financial.api.v1.dto.AccountInput;
import com.cvc.financial.api.v1.dto.TransferInput;
import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.model.Transfer;
import com.cvc.financial.domain.model.TransferType;
import com.cvc.financial.domain.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TransferFactory {
    private TransferFactory(){}

    public static Transfer createTransferToBeSaved(User user, Account originatingAccount,
                                                   Account destinationAccount, LocalDate scheduling) {
        return Transfer.builder()
                .user(user)
                .originatingAccount(originatingAccount)
                .destinationAccount(destinationAccount)
                .scheduling(scheduling)
                .transferValue(new BigDecimal("100.00"))
                .build();
    }

    public static Transfer createTransferToBeSaved(User user, Account originatingAccount,
                                                   Account destinationAccount, long days) {
        var scheduling = LocalDate.now().plus(days, ChronoUnit.DAYS);

        return Transfer.builder()
                .user(user)
                .originatingAccount(originatingAccount)
                .destinationAccount(destinationAccount)
                .scheduling(scheduling)
                .transferValue(new BigDecimal("100.00"))
                .build();
    }

    public static Transfer createTransferSaved(User user, Account originatingAccount,
                                               Account destinationAccount, LocalDate scheduling) {
        return Transfer.builder()
                .user(user)
                .originatingAccount(originatingAccount)
                .destinationAccount(destinationAccount)
                .scheduling(scheduling)
                .transferType(TransferType.A)
                .transferValue(new BigDecimal("100.00"))
                .rate(new BigDecimal("6.00"))
                .totalValue(new BigDecimal("106.00"))
                .build();
    }

    public static TransferInput createTransferInputToBeSaved(Account originatingAccount,
                                                             Account destinationAccount, long days) {
        var scheduling = LocalDate.now().plus(days, ChronoUnit.DAYS);

        AccountInput accountInput = new AccountInput();
        accountInput.setAgency(destinationAccount.getAgency());
        accountInput.setAccountNumber(destinationAccount.getAccountNumber());

        TransferInput transferInput = new TransferInput();
        transferInput.setTransferValue(new BigDecimal("100.00"));
        transferInput.setDestinationAccount(accountInput);
        transferInput.setScheduling(scheduling);

        return transferInput;
    }
}
