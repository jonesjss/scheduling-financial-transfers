package com.cvc.financial.api.v1.controller;

import com.cvc.financial.api.ResourceUriHelper;
import com.cvc.financial.api.openapi.controller.UserAccountTransferControllerOpenApi;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/users/{userId}/accounts/{accountId}/transfers")
public class UserAccountTransferController implements UserAccountTransferControllerOpenApi {
    private final UserAccountService userAccountService;
    private final AccountService accountService;
    private final TransferService transferService;
    private final TransferMapper transferMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransferOutput> findAllTransfersByUserIdAndAccountId(@PathVariable Long userId, @PathVariable Long accountId) {
        log.info("Request for findAllTransfersByUserIdAndAccountId with userId {} userId {} and accountId {}",  userId, accountId);

        List<Transfer> transferList = transferService.findAllByUserIdAndAccountId(userId, accountId);

        return transferMapper.toDto(transferList);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransferOutput findAllTransfersByUserIdAndAccountId(@PathVariable Long userId, @PathVariable Long accountId,
                                                                     @PathVariable(name = "id") Long transferId) {
        log.info("Request for findAllTransfersByUserIdAndAccountId with userId {} userId {}, accountId {} and transferId {}"
                    ,userId, accountId, transferId);

        Transfer transfer = transferService.findByIdAndUserIdAndAccountId(transferId, userId, accountId);

        return transferMapper.toDto(transfer);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveTransfer(@PathVariable Long userId, @PathVariable Long accountId,
                             @Valid @RequestBody TransferInput transferInput) {
        log.info("Request for saveTransfer with userId {} and accountId {}", userId, accountId);

        AccountInput accountInput = transferInput.getDestinationAccount();

        Account originatingAccount = userAccountService.findByIdAndUserID(accountId, userId);
        Account destinationAccount = accountService.findByAgencyAndAccountNumber(accountInput.getAgency(), accountInput.getAccountNumber());

        Transfer transfer = transferMapper.toModel(transferInput, originatingAccount, destinationAccount);

        transfer = transferService.save(transfer);

        log.info("Transfer performed successfully.", userId, accountId);

        ResourceUriHelper.addLocationInUriResponseHeader(transfer.getId());
    }
}
