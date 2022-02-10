package com.cvc.financial.api.v1.mapper;

import com.cvc.financial.api.v1.dto.UserInput;
import com.cvc.financial.api.v1.dto.UserOutput;
import com.cvc.financial.domain.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = AccountMapper.class)
public interface UserMapper {

    UserOutput toDto(User user);

    List<UserOutput> toDto(List<User> users);

    User toModel(UserInput userInput);
}
