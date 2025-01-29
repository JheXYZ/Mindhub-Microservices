package com.mindhub.email_service.utils;

public class EndPoints {

    public static final String USER_BASE_URL = "http://user-service/api/v1/users";
    public static final String PRODUCT_BASE_URL = "http://product-service/api/v1/products";

    public static String USER_GET_BY_EMAIL(String email) {
        return String.join("", USER_BASE_URL, "?email=", email);
    }

    public static String PRODUCT_GET_ALL_BY_IDS(String ids) {
        return String.join("", PRODUCT_BASE_URL, "?ids=", ids);
    }

    ;

}
