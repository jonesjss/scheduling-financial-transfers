package com.cvc.financial.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountOutput {
    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "0001")
    private String agency;
    @ApiModelProperty(example = "012345")
    private String accountNumber;
    @ApiModelProperty(example = "1")
    private Long userId;
}
