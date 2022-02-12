package com.cvc.financial.domain.model;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class SameDayTransferStrategy extends TransferStrategy {

    public SameDayTransferStrategy() {
        this(null);
    }

    public SameDayTransferStrategy(TransferStrategy transferStrategy) {
        super(TransferType.A, transferStrategy);
    }

    @Override
    protected boolean isCalculate(TransferValue transferValue) {
        long differenceInDays = getDifferenceDays(transferValue.getScheduling());

        return differenceInDays == 0;
    }

    @Override
    protected BigDecimal calculateTotalValue(TransferValue transferValue) {
        log.info("Applied calculation is: same day.");

        BigDecimal rate = new BigDecimal("0.03")
                .multiply(transferValue.getTransferValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        return transferValue.getTransferValue()
                .add(new BigDecimal("3"))
                .add(rate)
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
