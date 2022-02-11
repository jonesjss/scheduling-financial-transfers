package com.cvc.financial.api.v1.controller;

import com.cvc.financial.api.ResourceUriHelper;
import com.cvc.financial.api.v1.dto.TransferInput;
import com.cvc.financial.api.v1.MapperInstancesFactory;
import com.cvc.financial.api.v1.mapper.TransferMapper;
import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.model.Transfer;
import com.cvc.financial.domain.model.User;
import com.cvc.financial.domain.service.AccountService;
import com.cvc.financial.domain.service.TransferService;
import com.cvc.financial.domain.service.UserAccountService;
import com.cvc.financial.util.AccountFactory;
import com.cvc.financial.util.CommonUtils;
import com.cvc.financial.util.TransferFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
class UserAccountTransferControllerTest {

    @InjectMocks
    private UserAccountTransferController userAccountTransferController;
    @Mock
    private UserAccountService userAccountService;
    @Mock
    private AccountService accountService;
    @Mock
    private TransferService transferService;
    @Spy
    private TransferMapper transferMapper = MapperInstancesFactory.transferMapper();


    private ArgumentCaptor<Transfer> transferCaptor;

    private Account originatingAccount;
    private Account destinationAccount;

    @BeforeEach
    void setup() {
        originatingAccount = AccountFactory.createAccountOriginatingSaved(new User());
        destinationAccount  = AccountFactory.createAccountDestinationSaved(new User());

        BDDMockito.when(userAccountService.findByIdAndUserID(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(originatingAccount);

        BDDMockito.when(
                        accountService.findByAgencyAndAccountNumber(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(destinationAccount);
        transferCaptor = null;
    }

    @BeforeAll
    void beforeAll() {
        BDDMockito.mockStatic(ResourceUriHelper.class)
                .when(() -> ResourceUriHelper.addLocationInUriResponseHeader(ArgumentMatchers.anyLong()))
                .thenAnswer((Answer<Void>) invocation -> null);
    }

    @DisplayName("save transfer returns with a three percent fee when scheduled for the same day")
    @Test
    void saveTransfer_TransferReturnsWithA3PercentFee_WhenScheduledForTheSameDay() {
        TransferInput transferInput = TransferFactory.createTransferInputToBeSaved(originatingAccount, destinationAccount, 0);
        Transfer transferSaved = TransferFactory.createTransferSaved(new User(), originatingAccount, destinationAccount, LocalDate.now());

        Mockito.when(transferService.save(ArgumentMatchers.any()))
                .thenReturn(transferSaved);

        transferCaptor = ArgumentCaptor.forClass(Transfer.class);

        userAccountTransferController
                .saveTransfer(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), transferInput);

        Mockito.verify(transferService, BDDMockito.atLeast(1)).save(transferCaptor.capture());

        Transfer transfer = transferCaptor.getValue();

        Assertions.assertThat(transfer).isNotNull();
        Assertions.assertThat(transfer.getOriginatingAccount()).isEqualTo(originatingAccount);
        Assertions.assertThat(transfer.getDestinationAccount()).isEqualTo(destinationAccount);
        Assertions.assertThat(transfer.getScheduling()).isEqualTo(LocalDate.now());
        Assertions.assertThat(transfer.getTransferValue()).isPositive();
    }

    @DisplayName("save returns transfer with an 8 percent fee when over 10 days and under 20 days")
    @Test
    void save_ReturnsTransferWithAn8PercentFee_WhenOver10DaysAndUnder20Days() {
        long differenceInDays = CommonUtils.getRandomValuesBetween(11, 20);

        TransferInput transferInput = TransferFactory.createTransferInputToBeSaved(originatingAccount, destinationAccount, differenceInDays);
        Transfer transferSaved = TransferFactory.createTransferSaved(new User(), originatingAccount, destinationAccount, transferInput.getScheduling());
        BDDMockito.when(transferService.save(ArgumentMatchers.any()))
                .thenReturn(transferSaved);

        transferCaptor = ArgumentCaptor.forClass(Transfer.class);

        userAccountTransferController
                .saveTransfer(1L, 1L, transferInput);

        BDDMockito.verify(transferService, BDDMockito.atLeast(1)).save(transferCaptor.capture());

        Transfer transfer = transferCaptor.getValue();

        var scheduling = LocalDate.now().plus(10, ChronoUnit.DAYS);

        Assertions.assertThat(transfer).isNotNull();
        Assertions.assertThat(transfer.getOriginatingAccount()).isEqualTo(originatingAccount);
        Assertions.assertThat(transfer.getDestinationAccount()).isEqualTo(destinationAccount);
        Assertions.assertThat(transfer.getScheduling()).isAfter(scheduling);
        Assertions.assertThat(transfer.getTransferValue()).isPositive();
    }
}