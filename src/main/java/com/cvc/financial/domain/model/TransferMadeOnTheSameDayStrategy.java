package com.cvc.financial.domain.model;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class TransferMadeOnTheSameDayStrategy extends TransferStrategy {

    public TransferMadeOnTheSameDayStrategy() {
        this(null);
    }

    public TransferMadeOnTheSameDayStrategy(TransferStrategy transferStrategy) {
        super(TransferType.A, transferStrategy);
    }

    @Override
    protected boolean isCalculate(TransferValue transferValue) {
        long differenceInDays = getDifferenceDays(transferValue.getScheduling());

        return differenceInDays == 0;
    }

    @Override
    protected BigDecimal calculateRate(TransferValue transferValue) {
        log.info("Applied calculation is: same day.");

        BigDecimal rate = new BigDecimal("0.03")
                .multiply(transferValue.getTransferValue())
                .add(new BigDecimal("3"))
                .setScale(2, RoundingMode.HALF_EVEN);

        return rate;
    }
}
