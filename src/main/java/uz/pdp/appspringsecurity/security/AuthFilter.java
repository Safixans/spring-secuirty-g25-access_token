package uz.pdp.appspringsecurity.security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.appspringsecurity.entity.User;
import uz.pdp.appspringsecurity.repository.UserRepository;
import uz.pdp.appspringsecurity.service.CurrentUserDetails;
import uz.pdp.appspringsecurity.utils.AppConstants;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value(value = "${app.jwt.access.token.key}")
    private String tokenKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(AppConstants.AUTH_HEADER);
        if (authorization != null) {
            UserDetails userDetails =
                    authorization.startsWith(AppConstants.BASIC_AUTH) ?
                            getUserByBasicAuth(authorization)
                            : getUserByBearerAuth(authorization);

            if (userDetails != null) {
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private UserDetails getUserByBearerAuth(String authorization) {

        try {
            authorization = authorization.substring(AppConstants.BEARER_AUTH.length());


            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(tokenKey)
                    .parseClaimsJws(authorization);

            String userId = claimsJws.getBody().getSubject();
            Optional<User> optionalUser = userRepository.findById(Integer.valueOf(userId));
            if (optionalUser.isEmpty())
                return null;
            User user = optionalUser.get();

            CurrentUserDetails principal = new CurrentUserDetails(user);
            if (!(principal.isAccountNonExpired() && principal.isAccountNonLocked()
                    && principal.isCredentialsNonExpired() && principal.isEnabled()))
                return null;
            return principal;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return null;
        } catch (Exception e) {
            System.out.println();
            return null;
        }


    }

    private UserDetails getUserByBasicAuth(String authorization) {
        try {
            authorization = authorization.substring(AppConstants.BASIC_AUTH.length());
            byte[] decode = Base64.getDecoder().decode(authorization);
            String[] split = new String(decode).split(":");

            String username = split[0];
            String password = split[1];

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username,
                    password
            ));
            return (CurrentUserDetails) authentication.getPrincipal();
        } catch (AuthenticationException e) {
            return null;
        }
    }


}
