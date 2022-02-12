package com.cvc.financial.domain.model;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class TransferOver10To20DaysStrategy extends TransferStrategy {

    public TransferOver10To20DaysStrategy() {
        this(null);
    }

    public TransferOver10To20DaysStrategy(TransferStrategy transferStrategy) {
        super(TransferType.C, transferStrategy);
    }

    @Override
    protected boolean isCalculate(TransferValue transferValue) {
        long differenceInDays = getDifferenceDays(transferValue.getScheduling());

        return (differenceInDays > 10 && differenceInDays <= 20);
    }

    @Override
    protected BigDecimal calculateTotalValue(TransferValue transferValue) {
        log.info("Applied calculation is: 11 to 20 days.");

        BigDecimal totalValue = new BigDecimal("0.08")
                .multiply(transferValue.getTransferValue())
                .add(transferValue.getTransferValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        return totalValue;
    }
}
