package com.cvc.financial.domain.service;

import com.cvc.financial.domain.exception.TransferException;
import com.cvc.financial.domain.exception.TransferNotFoundException;
import com.cvc.financial.domain.model.Transfer;
import com.cvc.financial.domain.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransferService {
    private final TransferRepository transferRepository;

    public List<Transfer> findAllByUserIdAndAccountId(Long userId, Long accountId) {
        return transferRepository.findByUserIdAndAccountId(userId, accountId);
    }

    public Transfer findByIdAndUserIdAndAccountId(Long transferId, Long userId, Long accountId) {
        return transferRepository.findByIdAndUserIdAndAccountId(transferId, userId, accountId)
                .orElseThrow(() -> new TransferNotFoundException(
                        String.format("Transfer not found with id %d, userId %d and accountId %d", transferId, userId, accountId)));
    }

    @Transactional
    public Transfer save(Transfer transfer) {
        if(transfer.getOriginatingAccount().equals(transfer.getDestinationAccount())) {
            throw new TransferException("Cannot transfer to the same account");
        }

        transfer.calculateRate();

        return transferRepository.save(transfer);
    }
}
