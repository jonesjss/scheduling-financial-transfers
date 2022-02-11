package com.cvc.financial.domain.model;

import java.math.BigDecimal;

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
    protected BigDecimal calculateTotalValue(TransferValue transferValue) {
        long differenceInDays = getDifferenceDays(transferValue.getScheduling());

        BigDecimal totalValue = new BigDecimal("12")
                .multiply(new BigDecimal(differenceInDays))
                .add(transferValue.getTransferValue())
                .setScale(2);

        return totalValue;
    }
}
