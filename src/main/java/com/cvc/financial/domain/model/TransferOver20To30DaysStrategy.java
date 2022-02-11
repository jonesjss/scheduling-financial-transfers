package com.cvc.financial.domain.model;

import java.math.BigDecimal;

public class TransferOver20To30DaysStrategy extends TransferStrategy {

    public TransferOver20To30DaysStrategy() {
        this(null);
    }

    public TransferOver20To30DaysStrategy(TransferStrategy transferStrategy) {
        super(TransferType.C, transferStrategy);
    }

    @Override
    protected boolean isCalculate(TransferValue transferValue) {
        long differenceInDays = getDifferenceDays(transferValue.getScheduling());

        return (differenceInDays > 20 && differenceInDays <= 30);
    }

    @Override
    protected BigDecimal calculateTotalValue(TransferValue transferValue) {
        BigDecimal totalValue = new BigDecimal("0.06")
                .multiply(transferValue.getTransferValue())
                .add(transferValue.getTransferValue())
                .setScale(2);

        return totalValue;
    }
}
