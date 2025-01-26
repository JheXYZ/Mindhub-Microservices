package com.mindhub.order_service.utils;

import java.util.List;

public class EndPoints {

    public static final String USER_BASE_URL = "http://localhost:8082/api/v1/users";
    public static final String USER_CREATE = USER_BASE_URL;
    public static final String USER_GET =  USER_BASE_URL;
    public static final String USER_GET_ID =  USER_BASE_URL + '/';
    public static final String USER_DELETE = USER_BASE_URL + '/';
    public static final String USER_PATCH = USER_BASE_URL + '/';
    public static final String USER_PUT = USER_BASE_URL + '/';

    public static final String PRODUCT_BASE_URL = "http://localhost:8083/api/v1/products";
    public static final String PRODUCT_CREATE = PRODUCT_BASE_URL;
    public static final String PRODUCT_GET = PRODUCT_BASE_URL;
    public static final String PRODUCT_GET_ID = PRODUCT_BASE_URL + '/';
    public static final String PRODUCT_DELETE = PRODUCT_BASE_URL + '/';
    public static final String PRODUCT_PUT = PRODUCT_BASE_URL + '/';
    public static final String PRODUCT_PATCH = PRODUCT_BASE_URL + '/';
    public static final String PRODUCT_PATCH_STOCK = PRODUCT_BASE_URL + "/stock";

}
