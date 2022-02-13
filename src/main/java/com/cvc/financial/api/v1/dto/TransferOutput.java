package com.cvc.financial.api.v1.dto;

import com.cvc.financial.domain.model.TransferType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
public class TransferOutput {
    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "1")
    private Long userId;
    @ApiModelProperty(example = "1")
    private Long accountId;
    private AccountOutput destinationAccount;
    @ApiModelProperty(example = "2022-02-11T12:00:00.000Z")
    private OffsetDateTime creation;
    @ApiModelProperty(example = "C")
    private TransferType transferType;
    @ApiModelProperty(example = "2022-03-31")
    private LocalDate scheduling;
    @ApiModelProperty(example = "150000")
    private BigDecimal transferValue;
    @ApiModelProperty(example = "3000")
    private BigDecimal rate;
    @ApiModelProperty(example = "153000")
    private BigDecimal totalValue;
}
