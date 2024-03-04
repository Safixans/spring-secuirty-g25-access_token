package uz.pdp.appspringsecurity.payload;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import uz.pdp.appspringsecurity.utils.AppConstants;


@Builder
@Getter
public class TokenDTO {

    private String accessToken;

    private String refreshToken;

    @Builder.Default
    private String tokenType = AppConstants.BEARER_AUTH;
}
