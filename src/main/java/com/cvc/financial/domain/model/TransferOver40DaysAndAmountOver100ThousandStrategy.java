package com.cvc.financial.domain.model;

import java.math.BigDecimal;

public class TransferOver40DaysAndAmountOver100ThousandStrategy extends TransferStrategy {

    public TransferOver40DaysAndAmountOver100ThousandStrategy() {
        this(null);
    }

    public TransferOver40DaysAndAmountOver100ThousandStrategy(TransferStrategy transferStrategy) {
        super(TransferType.C, transferStrategy);
    }

    @Override
    protected boolean isCalculate(TransferValue transferValue) {
        long differenceInDays = getDifferenceDays(transferValue.getScheduling());
        BigDecimal hundredThousand = new BigDecimal("100000.00");

        return (differenceInDays > 40 && transferValue.getTransferValue().compareTo(hundredThousand) > 0);
    }

    @Override
    protected BigDecimal calculateTotalValue(TransferValue transferValue) {
        BigDecimal totalValue = new BigDecimal("0.02")
                .multiply(transferValue.getTransferValue())
                .add(transferValue.getTransferValue())
                .setScale(2);

        return totalValue;
    }
}
