package com.cvc.financial.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class UserOutput {
    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "João da Silva")
    private String name;
    @ApiModelProperty(example = "2022-02-20T12:00:00.000Z")
    private OffsetDateTime creation;
}
