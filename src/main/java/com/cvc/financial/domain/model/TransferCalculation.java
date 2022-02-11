package com.cvc.financial.domain.model;

public class TransferCalculation {

    public static CalculatedTransfer calculateRate(TransferValue transferValue) {
        TransferStrategy transferOver40DaysAndAmountOver100ThousandStrategy = new TransferOver40DaysAndAmountOver100ThousandStrategy();
        TransferStrategy transferOver30To40DaysStrategy = new TransferOver30To40DaysStrategy(transferOver40DaysAndAmountOver100ThousandStrategy);
        TransferStrategy transferOver20To30DaysStrategy = new TransferOver20To30DaysStrategy(transferOver30To40DaysStrategy);
        TransferStrategy transferOver10To20DaysStrategy = new TransferOver10To20DaysStrategy(transferOver20To30DaysStrategy);
        TransferStrategy transferInUpTo10DaysStrategy = new TransferInUpTo10DaysStrategy(transferOver10To20DaysStrategy);
        TransferStrategy sameDayTransferStrategy = new SameDayTransferStrategy(transferInUpTo10DaysStrategy);

        return sameDayTransferStrategy.calculate(transferValue);
    }
}
