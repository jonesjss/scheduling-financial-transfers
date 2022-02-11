package com.cvc.financial.domain.service;

import com.cvc.financial.domain.exception.TransferException;
import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.model.Transfer;
import com.cvc.financial.domain.model.TransferType;
import com.cvc.financial.domain.model.User;
import com.cvc.financial.domain.repository.TransferRepository;
import com.cvc.financial.util.AccountFactory;
import com.cvc.financial.util.CommonUtils;
import com.cvc.financial.util.TransferFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

@DisplayName("Tests for TransferService")
@ExtendWith(SpringExtension.class)
class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;
    @Mock
    private TransferRepository transferRepository;
    private Account accountOriginating;
    private Account accountDestination;

    @BeforeEach
    public void setup() {
        accountOriginating = AccountFactory.createAccountOriginatingSaved(new User());
        accountDestination = AccountFactory.createAccountDestinationSaved(new User());
    }

    @DisplayName("save transfer returns with a three percent fee when scheduled for the same day")
    @Test
    void save_TransferReturnsWithA3PercentFee_WhenScheduledForTheSameDay() {

        Transfer transfer = TransferFactory
                .createTransferSaved(new User(), accountOriginating, accountDestination, LocalDate.now());

        BDDMockito.when(transferRepository.save(transfer))
                .thenReturn(transfer);

        // rate calculation formula
        BigDecimal totalValue = new BigDecimal("0.03")
                .multiply(transfer.getTransferValue())
                .add(transfer.getTransferValue())
                .add(new BigDecimal("3.00"))
                .setScale(2);

        Transfer transferSaved = transferService.save(transfer);

        Assertions.assertThat(transferSaved).isNotNull();
        Assertions.assertThat(transferSaved.getTransferType()).isEqualTo(TransferType.A);
        Assertions.assertThat(transferSaved.getTotalValue()).isEqualTo(totalValue);
    }

    @DisplayName("save returns transfer with a rate of 12 multiplied by the number of days when the transfer is within 10 days")
    @Test
    void save_ReturnsTransferWithARateOf12MultipliedByTheNumberOfDays_WhenTheTransferIsWithin10Days() {
        long differenceInDays = CommonUtils.getRandomValuesBetween(1, 10);

        Transfer transfer = TransferFactory
                .createTransferToBeSaved(new User(), accountOriginating, accountDestination, differenceInDays);

        BDDMockito.when(transferRepository.save(transfer))
                .thenReturn(transfer);

        // rate calculation formula
        BigDecimal totalValue = new BigDecimal("12")
                .multiply(new BigDecimal(differenceInDays))
                .add(transfer.getTransferValue())
                .setScale(2);

        Transfer transferSaved = transferService.save(transfer);

        Assertions.assertThat(transferSaved).isNotNull();
        Assertions.assertThat(transferSaved.getTransferType()).isEqualTo(TransferType.B);
        Assertions.assertThat(transferSaved.getTotalValue()).isEqualTo(totalValue);
    }

    @DisplayName("save returns transfer with an 8 percent fee when over 10 days and under 20 days")
    @Test
    void save_ReturnsTransferWithAn8PercentFee_WhenOver10DaysAndUnder20Days() {
        long differenceInDays = CommonUtils.getRandomValuesBetween(11, 20);

        Transfer transfer = TransferFactory
                .createTransferToBeSaved(new User(), accountOriginating, accountDestination, differenceInDays);

        BDDMockito.when(transferRepository.save(transfer))
                .thenReturn(transfer);

        // rate calculation formula
        BigDecimal totalValue = new BigDecimal("0.08")
                .multiply(transfer.getTransferValue())
                .add(transfer.getTransferValue())
                .setScale(2);

        Transfer transferSaved = transferService.save(transfer);

        Assertions.assertThat(transferSaved).isNotNull();
        Assertions.assertThat(transferSaved.getTransferType()).isEqualTo(TransferType.C);
        Assertions.assertThat(transferSaved.getTotalValue()).isEqualTo(totalValue);
    }

    @DisplayName("save returns transfer with an 6 percent fee when over 20 days and under 30 days")
    @Test
    void save_ReturnsTransferWithAn6PercentFee_WhenOver20DaysAndUnder30Days() {
        long differenceInDays = CommonUtils.getRandomValuesBetween(21, 30);

        Transfer transfer = TransferFactory
                .createTransferToBeSaved(new User(), accountOriginating, accountDestination, differenceInDays);

        BDDMockito.when(transferRepository.save(transfer))
                .thenReturn(transfer);

        // rate calculation formula
        BigDecimal totalValue = new BigDecimal("0.06")
                .multiply(transfer.getTransferValue())
                .add(transfer.getTransferValue())
                .setScale(2);

        Transfer transferSaved = transferService.save(transfer);

        Assertions.assertThat(transferSaved).isNotNull();
        Assertions.assertThat(transferSaved.getTransferType()).isEqualTo(TransferType.C);
        Assertions.assertThat(transferSaved.getTotalValue()).isEqualTo(totalValue);
    }

    @DisplayName("save returns transfer with an 4 percent fee when over 30 days and under 40 days")
    @Test
    void save_ReturnsTransferWithAn4PercentFee_WhenOver30DaysAndUnder40Days() {
        long differenceInDays = CommonUtils.getRandomValuesBetween(31, 40);

        Transfer transfer = TransferFactory
                .createTransferToBeSaved(new User(), accountOriginating, accountDestination, differenceInDays);

        BDDMockito.when(transferRepository.save(transfer))
                .thenReturn(transfer);

        // rate calculation formula
        BigDecimal totalValue = new BigDecimal("0.04")
                .multiply(transfer.getTransferValue())
                .add(transfer.getTransferValue())
                .setScale(2);

        Transfer transferSaved = transferService.save(transfer);

        Assertions.assertThat(transferSaved).isNotNull();
        Assertions.assertThat(transferSaved.getTransferType()).isEqualTo(TransferType.C);
        Assertions.assertThat(transferSaved.getTotalValue()).isEqualTo(totalValue);
    }

    @DisplayName("save returns transfer with a fee of 2 percent when over 40 days and value over 100 thousand")
    @Test
    void save_ReturnsTransferWithAn2PercentFee_WhenOver40DaysAndValueOver100Thousand() {
        long differenceInDays = CommonUtils.getRandomValuesBetween(41, 1000);

        Transfer transfer = TransferFactory
                .createTransferToBeSaved(new User(), accountOriginating, accountDestination, differenceInDays);

        transfer.setTransferValue(new BigDecimal("150000.00"));

        BDDMockito.when(transferRepository.save(transfer))
                .thenReturn(transfer);

        // rate calculation formula
        BigDecimal totalValue = new BigDecimal("0.02")
                .multiply(transfer.getTransferValue())
                .add(transfer.getTransferValue())
                .setScale(2);

        Transfer transferSaved = transferService.save(transfer);

        Assertions.assertThat(transferSaved).isNotNull();
        Assertions.assertThat(transferSaved.getTransferType()).isEqualTo(TransferType.C);
        Assertions.assertThat(transferSaved.getTotalValue()).isEqualTo(totalValue);
    }

    @DisplayName("save should throw an transaction exception when there is no applicable fee")
    @Test
    void save_ShouldThrowAnTransactionException_WhenThereIsNoApplicableFee() {
        long differenceInDays = CommonUtils.getRandomValuesBetween(41, 1000);
        Account accountOriginating = AccountFactory.createAccountOriginatingSaved(new User());
        Account accountDestination = AccountFactory.createAccountDestinationSaved(new User());

        Transfer transfer = TransferFactory
                .createTransferToBeSaved(new User(), accountOriginating, accountDestination, differenceInDays);

        Assertions.assertThatExceptionOfType(TransferException.class)
                .isThrownBy(() -> transferService.save(transfer))
                .withMessage("Could not find a calculation for this transfer");
    }

    @DisplayName("save should throw an transaction exception when to transfer to the same account")
    @Test
    void save_ShouldThrowAnTransactionException_WhenToTransferToTheSameAccount() {
        Account accountOriginating = AccountFactory.createAccountOriginatingSaved(new User());

        Transfer transfer = TransferFactory
                .createTransferToBeSaved(new User(), accountOriginating, accountOriginating, 10);

        Assertions.assertThatExceptionOfType(TransferException.class)
                .isThrownBy(() -> transferService.save(transfer))
                .withMessage("Cannot transfer to the same account");
    }
}