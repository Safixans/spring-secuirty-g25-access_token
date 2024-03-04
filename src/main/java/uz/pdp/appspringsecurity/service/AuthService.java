package uz.pdp.appspringsecurity.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.appspringsecurity.entity.User;
import uz.pdp.appspringsecurity.payload.ApiResult;
import uz.pdp.appspringsecurity.payload.SignInDTO;
import uz.pdp.appspringsecurity.payload.TokenDTO;
import uz.pdp.appspringsecurity.repository.UserRepository;

import java.util.Date;
import java.util.HashMap;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    @Value(value = "${app.jwt.access.token.key}")
    private String accessTokenKey;

    @Value(value = "${app.jwt.access.token.expiration.time}")
    private long accessTokenExpirationTime;

    @Value(value = "${app.jwt.refresh.token.key}")
    private String refreshTokenKey;

    @Value(value = "${app.jwt.refresh.token.expiration.time}")
    private long refreshTokenExpirationTime;

    public AuthService(UserRepository userRepository,
                       @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }


    public ApiResult<TokenDTO> login(SignInDTO signInDTO) {
//        User user = userRepository.findByEmail(signInDTO.getUsername()).orElseThrow(RuntimeException::new);
//
//        if (!passwordEncoder.matches(signInDTO.getPassword(), user.getPassword()))
//            throw new RuntimeException();
//
//        if (!user.isAccountNonExpired()
//                || !user.isAccountNonLocked()
//                || !user.isCredentialsNonExpired()
//                || !user.isEnabled())
//            throw new RuntimeException();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInDTO.getUsername(),
                        signInDTO.getPassword()));

        CurrentUserDetails user = (CurrentUserDetails) authentication.getPrincipal();


        String accessToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, accessTokenKey)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .addClaims(new HashMap<>() {{
                    put("ismi", user.getName());
                    put("test", "qalay");
                }})
                .compact();

        String refreshToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, refreshTokenKey)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .compact();

        return ApiResult.successResponse(TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " topilmadi"));
        return new CurrentUserDetails(user);
    }
}
