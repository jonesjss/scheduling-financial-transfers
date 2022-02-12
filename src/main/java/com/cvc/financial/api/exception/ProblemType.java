package com.cvc.financial.api.exception;

import lombok.Getter;

@Getter
public enum ProblemType {
    RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
    ENTITY_IN_USE("/entity-in-use", "Entity in use"),
    BUSINESS_ERROR("/business-error", "Business rule violation"),
    INCOMPREHESIBLE_MESSAGE("/incomprehensible-message", "Incomprehensible message"),
    INVALID_PARAMETER("/invalid-parameter", "Invalid parameter"),
    SYSTEM_ERROR("/system-error", "system-error"),
    INVALID_DATA("/invalid-data", "Invalid data"),
    TRANSFER_RULE("/transfer-rule", "Transfer rule"),
    EXISTING_ENTITY("/existing-entity", "Existing entity");

    private String title;
    private String uri;

    ProblemType(String path, String title) {
        this.uri = "https://www.cvc.com/api/documentation" + path;
        this.title = title;
    }
}