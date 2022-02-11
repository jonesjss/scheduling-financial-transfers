package com.cvc.financial.api.v1.dto;

import com.cvc.financial.domain.model.TransferType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
public class TransferOutput {
    private Long id;
    private Long userId;
    private Long accountId;
    private AccountOutput destinationAccount;
    private OffsetDateTime creation;
    private TransferType transferType;
    private LocalDate scheduling;
    private BigDecimal transferValue;
    private BigDecimal totalValue;
}
