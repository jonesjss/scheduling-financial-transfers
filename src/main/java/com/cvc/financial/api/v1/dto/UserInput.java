package com.cvc.financial.api.v1.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserInput {
    @NotEmpty
    private String name;
}
