package com.cvc.financial.api.v1.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransferInput {
    @NotNull
    @Valid
    private AccountInput destinationAccount;
    @Positive
    private BigDecimal transferValue;
    @NotNull
    @FutureOrPresent
    private LocalDate scheduling;
}
