package com.cvc.financial.api.v1.integration;

import static io.restassured.RestAssured.config;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.hamcrest.Matchers.equalTo;

import com.cvc.financial.api.v1.dto.TransferInput;
import com.cvc.financial.domain.model.Account;
import com.cvc.financial.domain.model.TransferType;
import com.cvc.financial.domain.repository.UserRepository;
import com.cvc.financial.util.AccountFactory;
import com.cvc.financial.util.CommonUtils;
import com.cvc.financial.util.TransferFactory;
import com.cvc.financial.util.UserFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@DisplayName("Integration tests for UserAccountTransferController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserAccountTransferControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private Account accountOriginating;
    private Account accountDestination;

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/v1/users";

        var userOriginating = UserFactory.createUserOrigemToBeSaved();
        accountOriginating = AccountFactory.createAccountOriginatingToBeSaved(userOriginating);
        userRepository.save(userOriginating);

        var userDestination = UserFactory.createUserDestinationToBeSaved();
        accountDestination = AccountFactory.createAccountDestinationToBeSaved(userDestination);
        userRepository.save(userDestination);
    }

    @DisplayName("save transfer returns with a three percent fee when scheduled for the same day")
    @Test
    void saveTransfer_TransferReturnsWithA3PercentFee_WhenScheduledForTheSameDay() {
        var params = getParams();

        TransferInput transfer = TransferFactory.createTransferInputToBeSaved(accountOriginating, accountDestination, 0);

        Response response =  given()
                .pathParams(params)
                .body(transfer)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{userId}/accounts/{accountId}/transfers");
        response.then().statusCode(HttpStatus.CREATED.value());

        // rate calculation formula
        BigDecimal rate = new BigDecimal("0.03")
                .multiply(transfer.getTransferValue())
                .add(new BigDecimal("3.00"))
                .setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal totalValue = rate.add(transfer.getTransferValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        given()
                .accept(ContentType.JSON)
                .config(config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
                .when()
                .get(response.header("Location"))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("rate", is(rate),
                        "totalValue", is(totalValue),
                        "transferType", equalTo(TransferType.A.toString())
                );
    }

    @DisplayName("save transfer returns transfer with a rate of 12 multiplied by the number of days when the transfer is within 10 days")
    @Test
    void saveTransfer_ReturnsTransferWithARateOf12MultipliedByTheNumberOfDays_WhenTheTransferIsWithin10Days() {
        var params = getParams();

        long differenceInDays = CommonUtils.getRandomValuesBetween(1, 10);

        TransferInput transfer = TransferFactory.createTransferInputToBeSaved(accountOriginating, accountDestination, differenceInDays);

        Response response =  given()
                .pathParams(params)
                .body(transfer)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{userId}/accounts/{accountId}/transfers");
        response.then().statusCode(HttpStatus.CREATED.value());

        // rate calculation formula
        BigDecimal rate = new BigDecimal("12")
                .multiply(new BigDecimal(differenceInDays))
                .setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal totalValue = rate.add(transfer.getTransferValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        given()
                .accept(ContentType.JSON)
                .config(config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
                .when()
                .get(response.header("Location"))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("rate", is(rate),
                        "totalValue", is(totalValue),
                        "transferType", equalTo(TransferType.B.toString())
                );
    }

    @DisplayName("save transfer returns transfer with an 8 percent fee when over 10 days and under 20 days")
    @Test
    void saveTransfer_ReturnsTransferWithAn8PercentFee_WhenOver10DaysAndUnder20Days() {
        var params = getParams();

        long differenceInDays = CommonUtils.getRandomValuesBetween(11, 20);

        TransferInput transfer = TransferFactory.createTransferInputToBeSaved(accountOriginating, accountDestination, differenceInDays);

        Response response =  given()
                .pathParams(params)
                .body(transfer)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{userId}/accounts/{accountId}/transfers");
        response.then().statusCode(HttpStatus.CREATED.value());

        // rate calculation formula
        BigDecimal rate = new BigDecimal("0.08")
                .multiply(transfer.getTransferValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal totalValue = rate.add(transfer.getTransferValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        given()
                .accept(ContentType.JSON)
                .config(config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
                .when()
                .get(response.header("Location"))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("rate", is(rate),
                        "totalValue", is(totalValue),
                        "transferType", equalTo(TransferType.C.toString())
                );
    }

    @DisplayName("save transfer returns transfer with an 6 percent fee when over 20 days and under 30 days")
    @Test
    void saveTransfer_ReturnsTransferWithAn6PercentFee_WhenOver20DaysAndUnder30Days() {
        var params = getParams();

        long differenceInDays = CommonUtils.getRandomValuesBetween(21, 30);

        TransferInput transfer = TransferFactory.createTransferInputToBeSaved(accountOriginating, accountDestination, differenceInDays);

        Response response =  given()
                .pathParams(params)
                .body(transfer)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{userId}/accounts/{accountId}/transfers");
        response.then().statusCode(HttpStatus.CREATED.value());

        // rate calculation formula
        BigDecimal rate = new BigDecimal("0.06")
                .multiply(transfer.getTransferValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal totalValue = rate.add(transfer.getTransferValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        given()
                .accept(ContentType.JSON)
                .config(config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
                .when()
                .get(response.header("Location"))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("rate", is(rate),
                        "totalValue", is(totalValue),
                        "transferType", equalTo(TransferType.C.toString())
                );
    }

    @DisplayName("save transfer returns transfer with an 4 percent fee when over 30 days and under 40 days")
    @Test
    void saveTransfer_ReturnsTransferWithAn4PercentFee_WhenOver30DaysAndUnder40Days() {
        var params = getParams();

        long differenceInDays = CommonUtils.getRandomValuesBetween(31, 40);

        TransferInput transfer = TransferFactory.createTransferInputToBeSaved(accountOriginating, accountDestination, differenceInDays);

        Response response =  given()
                .pathParams(params)
                .body(transfer)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{userId}/accounts/{accountId}/transfers");
        response.then().statusCode(HttpStatus.CREATED.value());

        // rate calculation formula
        BigDecimal rate = new BigDecimal("0.04")
                .multiply(transfer.getTransferValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal totalValue = rate.add(transfer.getTransferValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        given()
                .accept(ContentType.JSON)
                .config(config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
                .when()
                .get(response.header("Location"))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("rate", is(rate),
                        "totalValue", is(totalValue),
                        "transferType", equalTo(TransferType.C.toString())
                );
    }

    @DisplayName("save transfer returns transfer with a fee of 2 percent when over 40 days and value over 100 thousand")
    @Test
    void saveTransfer_ReturnsTransferWithAn2PercentFee_WhenOver40DaysAndValueOver100Thousand() {
        var params = getParams();

        long differenceInDays = CommonUtils.getRandomValuesBetween(41, 1000);

        TransferInput transfer = TransferFactory.createTransferInputToBeSaved(accountOriginating, accountDestination, differenceInDays);
        transfer.setTransferValue(new BigDecimal("150000.00"));

        Response response =  given()
                .pathParams(params)
                .body(transfer)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{userId}/accounts/{accountId}/transfers");
        response.then().statusCode(HttpStatus.CREATED.value());

        // rate calculation formula
        BigDecimal rate = new BigDecimal("0.02")
                .multiply(transfer.getTransferValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal totalValue = rate.add(transfer.getTransferValue())
                .setScale(2, RoundingMode.HALF_EVEN);

        given()
                .accept(ContentType.JSON)
                .config(config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
                .when()
                .get(response.header("Location"))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("rate", is(rate),
                        "totalValue", is(totalValue),
                        "transferType", equalTo(TransferType.C.toString())
                );
    }

    @DisplayName("save transfer return status bad request when there is no applicable fee")
    @Test
    void saveTransfer_ReturnStatusBadRquest_WhenThereIsNoApplicableFee() {
        var params = getParams();

        long differenceInDays = CommonUtils.getRandomValuesBetween(41, 1000);

        TransferInput transfer = TransferFactory.createTransferInputToBeSaved(accountOriginating, accountDestination, differenceInDays);

        Response post = given()
                .pathParams(params)
                .body(transfer)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{userId}/accounts/{accountId}/transfers");

        Assertions.assertThat(post.getBody().print()).contains("Could not find a calculation for this transfer");
    }

    private Map<String, Object> getParams() {
        var params = new HashMap<String, Object>();
        params.put("userId", 1);
        params.put("accountId", 1);
        return params;
    }
}