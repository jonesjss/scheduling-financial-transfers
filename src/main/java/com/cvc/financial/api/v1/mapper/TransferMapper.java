package com.cvc.financial.api.v1.mapper;

import com.cvc.financial.api.v1.dto.TransferInput;
import com.cvc.financial.api.v1.dto.TransferOutput;
import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.model.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper(uses = { AccountMapper.class })
public interface TransferMapper {
    TransferMapper INSTANCE = Mappers.getMapper(TransferMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "originatingAccount.id", target = "accountId")
    TransferOutput toDto(Transfer transfer);

    List<TransferOutput> toDto(Collection<Transfer> transfer);

    @Mapping(target = "transferValue", numberFormat = "#.##E0")
    Transfer toModel(TransferInput transferInput);

    @Mapping(target = "transferValue", numberFormat = "#.##E0")
    default Transfer toModel(TransferInput transferInput, Account originatingAccount, Account destinationAccount) {
        var transfer = toModel(transferInput);
        transfer.setUser(originatingAccount.getUser());
        transfer.setOriginatingAccount(originatingAccount);
        transfer.setDestinationAccount(destinationAccount);

        return transfer;
    }

}
