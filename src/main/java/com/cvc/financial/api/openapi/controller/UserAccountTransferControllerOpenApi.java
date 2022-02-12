package com.cvc.financial.api.openapi.controller;

import com.cvc.financial.api.exception.Problem;
import com.cvc.financial.api.v1.dto.TransferInput;
import com.cvc.financial.api.v1.dto.TransferOutput;
import io.swagger.annotations.*;

import java.util.List;

@Api(tags = "Transfers")
public interface UserAccountTransferControllerOpenApi {

    @ApiOperation("Search a list of transfers")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid userId or accountId", response = Problem.class),
            @ApiResponse(code = 404, message = "Transfer not found", response = Problem.class)
    })
    List<TransferOutput> findAllTransfersByUserIdAndAccountId(
            @ApiParam(value = "User Id", example = "1", required = true) Long userId,
            @ApiParam(value = "Account Id", example = "1", required = true) Long accountId);

    @ApiOperation("Search a transfer")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid userId or accountId or transferId", response = Problem.class),
            @ApiResponse(code = 404, message = "Transfer not found", response = Problem.class)
    })
    TransferOutput findAllTransfersByUserIdAndAccountId(
            @ApiParam(value = "User Id", example = "1", required = true) Long userId,
            @ApiParam(value = "Account Id", example = "1", required = true) Long accountId,
            @ApiParam(value = "Transfer Id", example = "1", required = true) Long transferId);

    @ApiOperation("Save a transfer")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid userId or accountId", response = Problem.class),
            @ApiResponse(code = 404, message = "Transfer not found", response = Problem.class)
    })
    void saveTransfer(
            @ApiParam(value = "User Id", example = "1", required = true) Long userId,
            @ApiParam(value = "Account Id", example = "1", required = true) Long accountId,
            @ApiParam(name = "body", value = "Representation of a new transfer", required = true) TransferInput transferInput);
}
