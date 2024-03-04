package uz.pdp.appspringsecurity.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import uz.pdp.appspringsecurity.controller.AuthController;
import uz.pdp.appspringsecurity.controller.PaymentController;

public interface AppConstants {
    String AUTH_HEADER = "Authorization";
    String BASIC_AUTH = "Basic ";
    String BEARER_AUTH = "Bearer ";
    ObjectMapper objectMapper = new ObjectMapper();

    String[] OPEN_PAGES = {
            "/home/open",
            AuthController.BASE_PATH + "/**",
    };

    String[] CASHIER_GET_PAGES = {
            PaymentController.BASE_PATH + "/*"
    };
}
