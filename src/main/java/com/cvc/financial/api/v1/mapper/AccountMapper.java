package com.cvc.financial.api.v1.mapper;

import com.cvc.financial.api.v1.dto.AccountOutput;
import com.cvc.financial.domain.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountMapper {

    @Mapping(source = "user.id", target = "userId")
    AccountOutput toDTO(Account account);
}
