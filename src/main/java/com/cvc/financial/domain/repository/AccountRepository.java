package com.cvc.financial.domain.repository;

import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Set<Account> findByUser(User user);
}
