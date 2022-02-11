package com.cvc.financial.domain.repository;

import com.cvc.financial.domain.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("select t from Transfer t join t.user u join t.originatingAccount oa where u.id = :userId and oa.id = :accountId")
    List<Transfer> findByUserIdAndAccountId(Long userId, Long accountId);
}