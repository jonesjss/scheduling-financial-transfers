package com.cvc.financial.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserInput {
    @ApiModelProperty(example = "João da Silva")
    @NotEmpty
    private String name;
}
