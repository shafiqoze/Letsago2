package com.example.lab_rest.remote;

public class ApiUtils {
    // REST API server URL
    public static final String BASE_URL = "http://178.128.220.20/2022770505/api/";

    // return UserService instance
    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
    // return RentalService instance
    public static RentalService getRentalService() {
        return RetrofitClient.getClient(BASE_URL).create(RentalService.class);
    }
}
