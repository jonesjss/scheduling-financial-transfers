package com.cvc.financial.api.v1.controller;

import com.cvc.financial.api.ResourceUriHelper;
import com.cvc.financial.api.v1.dto.AccountInput;
import com.cvc.financial.api.v1.dto.TransferInput;
import com.cvc.financial.api.v1.dto.TransferOutput;
import com.cvc.financial.api.v1.mapper.TransferMapper;
import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.model.Transfer;
import com.cvc.financial.domain.service.AccountService;
import com.cvc.financial.domain.service.TransferService;
import com.cvc.financial.domain.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/users/{userId}/accounts/{accountId}/transfers")
public class UserAccountTransferController {
    private final UserAccountService userAccountService;
    private final AccountService accountService;
    private final TransferService transferService;
    private final TransferMapper transferMapper;

    @GetMapping
    public List<TransferOutput> findAllTransfersByUserIdAndAccountId(@PathVariable Long userId, @PathVariable Long accountId) {
        List<Transfer> transferList = transferService.findAllByUserIdAndAccountId(userId, accountId);

        return transferMapper.toDto(transferList);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveTransfer(@PathVariable Long userId, @PathVariable Long accountId,
                             @Valid @RequestBody TransferInput transferInput) {
        AccountInput accountInput = transferInput.getDestinationAccount();

        Account originatingAccount = userAccountService.findByIdAndUserID(accountId, userId);
        Account destinationAccount = accountService.findByAgencyAndAccountNumber(accountInput.getAgency(), accountInput.getAccountNumber());

        Transfer transfer = transferMapper.toModel(transferInput, originatingAccount, destinationAccount);

        transfer = transferService.save(transfer);

        ResourceUriHelper.addLocationInUriResponseHeader(transfer.getId());
    }
}
