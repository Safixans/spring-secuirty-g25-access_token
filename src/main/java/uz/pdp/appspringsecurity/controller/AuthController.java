package uz.pdp.appspringsecurity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appspringsecurity.payload.ApiResult;
import uz.pdp.appspringsecurity.payload.SignInDTO;
import uz.pdp.appspringsecurity.payload.TokenDTO;
import uz.pdp.appspringsecurity.service.AuthService;

@RestController
@RequestMapping(AuthController.BASE_PATH)
@RequiredArgsConstructor
public class AuthController {
    public static final String BASE_PATH = "/auth";
    public static final String LOGIN_PATH = "/login";


    private final AuthService authService;

    @PostMapping(LOGIN_PATH)
    public ApiResult<TokenDTO> login(@Valid @RequestBody SignInDTO signInDTO) {
        return authService.login(signInDTO);
    }

    @GetMapping("/test")
    public String bla() {
        return "test auth";
    }


    @GetMapping("/test/bla")
    public String bla2() {
        return "Bla 2";
    }
}
