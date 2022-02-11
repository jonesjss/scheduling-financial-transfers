package com.cvc.financial.domain.repository;

import com.cvc.financial.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select a from Account a join a.user u where u.id = :userId")
    Set<Account> findByUserId(Long userId);

    @Query("select a from Account a join a.user u where a.id = :id and u.id = :userId")
    Optional<Account> findByIdAndUserId(Long id, Long userId);

    Optional<Account> findByAgencyAndAccountNumber(String agency, String accountNumber);
}
