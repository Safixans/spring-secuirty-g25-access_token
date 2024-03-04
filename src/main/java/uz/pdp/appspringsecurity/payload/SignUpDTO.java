package uz.pdp.appspringsecurity.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String prePassword;

    @NotBlank
    private String name;

}
