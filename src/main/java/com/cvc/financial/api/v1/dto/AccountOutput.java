package com.cvc.financial.api.v1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountOutput {
    private Long id;
    private String agency;
    private String accountNumber;
    private Long userId;
}
