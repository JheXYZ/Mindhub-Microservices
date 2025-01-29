package com.mindhub.order_service.utils;

public class EndPoints {

    public static final String USER_BASE_URL = "http://user-service/api/v1/users";
    public static final String PRODUCT_BASE_URL = "http://product-service/api/v1/products";
    public static final String PRODUCT_PATCH_STOCK = PRODUCT_BASE_URL + "/stock";

    public static String USER_GET_BY_EMAIL(String email) {
        return String.join("", USER_BASE_URL, "?email=", email);
    }

    public static String USER_GET_BY_ID(Long id) {
        return String.join("", USER_BASE_URL, "/", id.toString());
    }

    ;

    public static String PRODUCT_GET_ALL_BY_IDS(String ids) {
        return String.join("", PRODUCT_BASE_URL, "?ids=", ids);
    }

    ;

    public static String PRODUCT_GET_BY_ID(Long id) {
        return String.join("", PRODUCT_BASE_URL, "/", id.toString());
    }

}
