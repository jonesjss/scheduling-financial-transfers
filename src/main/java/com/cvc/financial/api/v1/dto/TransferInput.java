package com.cvc.financial.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransferInput {
    @NotEmpty
    @Valid
    private AccountInput destinationAccount;
    @ApiModelProperty(example = "150000.00")
    @Positive
    private BigDecimal transferValue;
    @ApiModelProperty(example = "2022-03-31")
    @NotNull
    @FutureOrPresent
    private LocalDate scheduling;
}
