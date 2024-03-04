package uz.pdp.appspringsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping("open")
    public String helloPage() {
        return "Hello from home page";
    }

    @GetMapping("secure")
    public String getSecurePage() {
        return "Secure page";
    }

}
