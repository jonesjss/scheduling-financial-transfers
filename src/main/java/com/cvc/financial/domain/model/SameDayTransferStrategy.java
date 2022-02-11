package com.cvc.financial.domain.model;

import java.math.BigDecimal;

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
        BigDecimal rate = new BigDecimal("0.03")
                .multiply(transferValue.getTransferValue())
                .setScale(2);

        return transferValue.getTransferValue()
                .add(new BigDecimal("3"))
                .add(rate)
                .setScale(2);
    }
}
