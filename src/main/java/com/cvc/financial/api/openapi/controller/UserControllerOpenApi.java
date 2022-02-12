package com.cvc.financial.api.openapi.controller;

import com.cvc.financial.api.exception.Problem;
import com.cvc.financial.api.v1.dto.UserInput;
import com.cvc.financial.api.v1.dto.UserOutput;
import io.swagger.annotations.*;

import java.util.List;

@Api(tags = "Users")
public interface UserControllerOpenApi {

    @ApiOperation("Search a list of users")
    List<UserOutput> findAll();

    @ApiOperation("Search a user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid user id", response = Problem.class),
            @ApiResponse(code = 404, message = "User not found", response = Problem.class)
    })
    UserOutput findById(
            @ApiParam(value = "User Id", example = "1", required = true) Long  id);

    @ApiResponses({
            @ApiResponse(code = 201, message = "User created")
    })
    @ApiOperation("Save a user")
    void save(@ApiParam(name = "body", value = "Representation of a new transfer", required = true) UserInput userInput);
}
