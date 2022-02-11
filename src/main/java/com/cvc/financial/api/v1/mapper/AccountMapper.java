package com.cvc.financial.api.v1.mapper;

import com.cvc.financial.api.v1.dto.AccountInput;
import com.cvc.financial.api.v1.dto.AccountOutput;
import com.cvc.financial.domain.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(source = "user.id", target = "userId")
    AccountOutput toDTO(Account account);

    List<AccountOutput> toDTO(Collection<Account> account);

    Account toModel(AccountInput accountInput);
}
