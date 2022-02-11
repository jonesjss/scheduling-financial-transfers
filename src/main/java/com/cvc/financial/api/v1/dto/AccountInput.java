package com.cvc.financial.api.v1.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AccountInput {
    @NotEmpty
    @Pattern(regexp = "[0-9]{4,5}")
    private String agency;
    @NotEmpty
    @Pattern(regexp = "[0-9]{4,10}[0-9a-zA-Z]{1}")
    private String accountNumber;
}
