package uz.pdp.appspringsecurity.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {


    @ExceptionHandler
    public ResponseEntity<?> handle(RuntimeException e) {

        return ResponseEntity.ok(e.getMessage());
    }

}
