package com.cvc.financial.domain.model;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class TransferInUpTo10DaysStrategy extends TransferStrategy {

    public TransferInUpTo10DaysStrategy() {
        this(null);
    }

    public TransferInUpTo10DaysStrategy(TransferStrategy transferStrategy) {
        super(TransferType.B, transferStrategy);
    }

    @Override
    protected boolean isCalculate(TransferValue transferValue) {
        long differenceInDays = getDifferenceDays(transferValue.getScheduling());

        return (differenceInDays > 0 && differenceInDays <= 10);
    }

    @Override
    protected BigDecimal calculateRate(TransferValue transferValue) {
        log.info("Applied calculation is: 1 to 10 days.");

        long differenceInDays = getDifferenceDays(transferValue.getScheduling());

        BigDecimal rate =  new BigDecimal("12")
                            .multiply(new BigDecimal(differenceInDays))
                            .setScale(2, RoundingMode.HALF_EVEN);
        return rate;
    }
}
