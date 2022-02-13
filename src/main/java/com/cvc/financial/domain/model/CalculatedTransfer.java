package com.cvc.financial.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Getter
public class CalculatedTransfer {
    private TransferType transferType;
    private BigDecimal calculatedRate;
    private BigDecimal calculatedValue;
}
