package uz.pdp.appspringsecurity.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PaymentController.BASE_PATH)
public class PaymentController {
    public static final String BASE_PATH = "/payment";

    //CASHIER, ADMIN
    @GetMapping
    public List<String> getPayments() {
        return List.of("Samandar", "Yashnar");
    }

    //CASHIER, ADMIN
    @GetMapping("/{id}")
    public String getPayment(@PathVariable String id) {
        return id;
    }

    //CASHIER, ADMIN
    @PostMapping
    public String addPayments() {
        return "Added";
    }

    //ADMIN
    @PutMapping
    public String editPayments() {
        return "Edited";
    }

    //ADMIN
    @DeleteMapping
    public String deletePayments() {
        return "Deleted";
    }
}
