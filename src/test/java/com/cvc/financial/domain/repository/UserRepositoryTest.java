package com.cvc.financial.domain.repository;

import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.model.User;
import com.cvc.financial.util.AccountFactory;
import com.cvc.financial.util.UserFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.Set;

@DisplayName("Tests for UserRepository")
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("save return user when successful")
    void save_ReturnsUser_WhenSuccessful() {
        var user = UserFactory.createUserOrigemToBeSaved();

        var userSaved = userRepository.save(user);

        Assertions.assertThat(userSaved).isNotNull();
        Assertions.assertThat(userSaved.getId()).isPositive();
        Assertions.assertThat(userSaved.getName()).isEqualTo(user.getName());
        Assertions.assertThat(userSaved.getCreation()).isNotNull();
    }

    @Test
    @DisplayName("save return user with account when successful")
    void save_ReturnsUserWithAccount_WhenSuccessful() {
        var user = UserFactory.createUserOrigemToBeSaved();
        var account = AccountFactory.createAccountOriginatingToBeSaved(user);
        user.addAccount(account);

        var userSaved = userRepository.save(user);
        System.out.println(user.getId());

        Assertions.assertThat(userSaved.getAccounts()).containsExactly(account);
    }

    @Test
    @DisplayName("delete user with account when successful")
    void delete_UserWithAccount_WhenSuccessful() {
        var user = UserFactory.createUserOrigemToBeSaved();
        var account = AccountFactory.createAccountOriginatingToBeSaved(user);
        user.addAccount(account);

        var userSaved = userRepository.save(user);

        userRepository.delete(userSaved);

        userRepository.flush();

        Optional<User> userNull = userRepository.findById(userSaved.getId());
        Set<Account> accounts = accountRepository.findByUserId(user.getId());

        Assertions.assertThat(userNull).isEmpty();
        Assertions.assertThat(accounts).isEmpty();
    }

    @Test
    @DisplayName("save throw exception when user is empty")
    void save_ThrowException_WhenUserIsEmpty() {
        Assertions.assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.save(new User()));

    }
}