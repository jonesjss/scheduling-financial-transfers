package com.cvc.financial.domain.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "transfers")
public class Transfer implements TransferValue {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "originating_account_id", updatable = false)
    private Account originatingAccount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destination_account_id", updatable = false)
    private Account destinationAccount;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime creation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TransferType transferType;

    @Column(nullable = false)
    private LocalDate scheduling;

    @Column(nullable = false, updatable = false)
    private BigDecimal transferValue;

    @Column(nullable = false, updatable = false)
    private BigDecimal totalValue;

    public void setUser(User user) {
        this.user = user;
    }

    public void setOriginatingAccount(Account originatingAccount) {
        this.originatingAccount = originatingAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }

    public void setScheduling(LocalDate scheduling) {
        this.scheduling = scheduling;
    }

    public void setTransferValue(BigDecimal transferValue) {
        this.transferValue = transferValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public void calculateRate() {
        CalculatedTransfer calculatedTransfer = TransferCalculation.calculateRate(this);

        setTotalValue(calculatedTransfer.getCalculatedValue());
        setTransferType(calculatedTransfer.getTransferType());
    }
}
