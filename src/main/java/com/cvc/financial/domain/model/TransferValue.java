package com.cvc.financial.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransferValue {
    BigDecimal getTransferValue();
    BigDecimal getRate();
    LocalDate getScheduling();
}
