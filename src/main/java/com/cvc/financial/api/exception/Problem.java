package com.cvc.financial.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class Problem {
    @ApiModelProperty(example = "400", position = 1)
    private Integer status;
    @ApiModelProperty(example = "2019-10-01T12:00:00.00000Z", position = 5)
    private OffsetDateTime timestamp;
    @ApiModelProperty(example = "https://www.cvc.com/api/documentation/resource-not-found", position = 10)
    private String type;
    @ApiModelProperty(example = "Resource not found", position = 15)
    private String title;
    @ApiModelProperty(example = "Invalid data", position = 20)
    private String detail;
    @ApiModelProperty(example = "User not found with id 1", position = 25)
    private String userMessage;
    @ApiModelProperty(value = "List of objects or fields that generated the error", position = 30)
    private List<Error> errors;

    @Getter
    @Builder
    public static class Error {
        @ApiModelProperty(example = "userId")
        private String name;
        @ApiModelProperty(example = "O userId is required")
        private String userMessage;
    }
}