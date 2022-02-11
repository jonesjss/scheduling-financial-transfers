package com.cvc.financial.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransferOver30To40DaysStrategy extends TransferStrategy {

    public TransferOver30To40DaysStrategy() {
        this(null);
    }

    public TransferOver30To40DaysStrategy(TransferStrategy transferStrategy) {
        super(TransferType.C, transferStrategy);
    }

    @Override
    protected boolean isCalculate(TransferValue transferValue) {
        long differenceInDays = getDifferenceDays(transferValue.getScheduling());

        return (differenceInDays > 30 && differenceInDays <= 40);
    }

    @Override
    protected BigDecimal calculateTotalValue(TransferValue transferValue) {
        BigDecimal totalValue = new BigDecimal("0.04")
                .multiply(transferValue.getTransferValue())
                .add(transferValue.getTransferValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        return totalValue;
    }
}
