package com.cvc.financial.api.v1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class UserOutput {
    private Long id;
    private String name;
    private OffsetDateTime creationDate;
}
