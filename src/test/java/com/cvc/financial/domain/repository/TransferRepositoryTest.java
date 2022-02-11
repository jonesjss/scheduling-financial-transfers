package com.cvc.financial.domain.repository;

import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.model.Transfer;
import com.cvc.financial.domain.model.User;
import com.cvc.financial.util.AccountFactory;
import com.cvc.financial.util.TransferFactory;
import com.cvc.financial.util.UserFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

@DisplayName("Tests for TransferRepository")
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class TransferRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransferRepository transferRepository;

    private User originatingUser;
    private User destinationUser;
    private Account originatingAccount;
    private Account destinationAccount;

    @BeforeEach
    void setup() {
        var user = UserFactory.createUserOrigemToBeSaved();
        originatingAccount = AccountFactory.createAccountOriginatingToBeSaved(user);
        originatingUser = userRepository.save(user);

        user = UserFactory.createUserDestinationToBeSaved();
        destinationAccount = AccountFactory.createAccountDestinationToBeSaved(user);
        destinationUser = userRepository.save(user);
    }

    @Test
    @DisplayName("save return transfer when successful")
    void save_ReturnsTransfer_WhenSuccessful() {
        var transfer = TransferFactory.createTransferSaved(originatingUser, originatingAccount,
                destinationAccount, LocalDate.now());
        var transferSaved = transferRepository.save(transfer);

        Assertions.assertThat(transferSaved).isNotNull();
        Assertions.assertThat(transferSaved.getId()).isPositive();
        Assertions.assertThat(transferSaved.getUser()).isEqualTo(originatingUser);
        Assertions.assertThat(transferSaved.getOriginatingAccount()).isEqualTo(originatingAccount);
        Assertions.assertThat(transferSaved.getDestinationAccount()).isEqualTo(destinationAccount);
        Assertions.assertThat(transferSaved.getCreation()).isNotNull();
        Assertions.assertThat(transferSaved.getScheduling()).isNotNull();
    }

    @Test
    @DisplayName("findAllByUserIdAndAccountId return list transfer when successful")
    void findAllByUserIdAndAccountId_ReturnsListTransfer_WhenSuccessful() {
        var transfer = TransferFactory.createTransferSaved(originatingUser, originatingAccount,
                destinationAccount, LocalDate.now());

        var transferSaved = transferRepository.saveAndFlush(transfer);

        List<Transfer> transfers = transferRepository.findByUserIdAndAccountId(originatingUser.getId(), originatingAccount.getId());

        Assertions.assertThat(transfers).containsExactly(transferSaved);
    }
}