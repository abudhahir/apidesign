package com.cleveloper.apidesign.constants;

public enum PaymentMethod {
    P01_CASH("P01", "CASH"),
    P02_CARD("P02", "CARD");

    private final String code;
    private final String type;

    PaymentMethod(String code, String type) {
        this.code = code;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static String getTypeByCode(String code) {
        for (PaymentMethod method : values()) {
            if (method.getCode().equals(code)) {
                return method.getType();
            }
        }
        throw new IllegalArgumentException("Invalid payment method code: " + code);
    }
} 