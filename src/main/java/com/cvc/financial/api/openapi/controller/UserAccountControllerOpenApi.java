package com.cvc.financial.api.openapi.controller;

import com.cvc.financial.api.exception.Problem;
import com.cvc.financial.api.v1.dto.AccountInput;
import com.cvc.financial.api.v1.dto.AccountOutput;
import io.swagger.annotations.*;

import java.util.List;

@Api(tags = "Accounts")
public interface UserAccountControllerOpenApi {

    @ApiOperation("Search a list of accounts")
    List<AccountOutput> findAll(
            @ApiParam(value = "User Id", example = "1", required = true) Long userId);

    @ApiOperation("Search a account")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid user id or accountId", response = Problem.class),
            @ApiResponse(code = 404, message = "User and Account not found", response = Problem.class)
    })
    AccountOutput findByIdAndUserId(
            @ApiParam(value = "User Id", example = "1", required = true) Long userId,
            @ApiParam(value = "Account Id", example = "1", required = true) Long id);

    @ApiOperation("Save on account")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Account created"),
    })
    void saveAccount(
            @ApiParam(value = "User Id", example = "1", required = true) Long userId,
            @ApiParam(name = "body", value = "Representation of a new account", required = true) AccountInput accountInput);
}
