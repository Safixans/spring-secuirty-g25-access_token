package uz.pdp.appspringsecurity.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import uz.pdp.appspringsecurity.payload.ApiResult;
import uz.pdp.appspringsecurity.payload.ErrorData;
import uz.pdp.appspringsecurity.utils.AppConstants;

import java.io.IOException;
import java.util.List;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ApiResult<List<ErrorData>> apiResult = ApiResult.errorResponse(accessDeniedException.getMessage(),
                HttpServletResponse.SC_FORBIDDEN);
        String string = AppConstants.objectMapper.writeValueAsString(apiResult);
        response.getWriter().write(string);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
