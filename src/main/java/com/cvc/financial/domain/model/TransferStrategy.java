package com.cvc.financial.domain.model;

import com.cvc.financial.domain.exception.TransferException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

import static com.cvc.financial.util.DateUtils.differenceInDays;

@Getter
public abstract class TransferStrategy {
    protected TransferStrategy next;
    protected TransferType transferType;

    public TransferStrategy(TransferType transferType){
        this(transferType, null);
    }

    public TransferStrategy(TransferType transferType, TransferStrategy transferStrategy){
        this.transferType = transferType;
        this.next = transferStrategy;
    }

    public CalculatedTransfer calculate(TransferValue transferValue) {
        if(isCalculate(transferValue)) {
            BigDecimal totalValue = calculateTotalValue(transferValue);

            CalculatedTransfer calculatedTransfer = CalculatedTransfer.builder()
                    .calculatedValue(totalValue)
                    .transferType(getTransferType())
                    .build();

            return calculatedTransfer;
        } else if(!Objects.isNull(getNext())) {
            return this.getNext().calculate(transferValue);
        }
        throw new TransferException("Could not find a calculation for this transfer");
    }

    protected long getDifferenceDays(LocalDate scheduling) {
        return differenceInDays(LocalDate.now(), scheduling);
    }

    protected abstract boolean isCalculate(TransferValue transferValue);

    protected abstract BigDecimal calculateTotalValue(TransferValue transferValue);
}
