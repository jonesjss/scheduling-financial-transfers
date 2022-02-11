package com.cvc.financial.domain.model;

import java.math.BigDecimal;

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
        BigDecimal totalValue = new BigDecimal("0.08")
                .multiply(transferValue.getTransferValue())
                .add(transferValue.getTransferValue())
                .setScale(2);

        return totalValue;
    }
}
