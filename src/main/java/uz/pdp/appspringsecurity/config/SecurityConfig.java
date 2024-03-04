package uz.pdp.appspringsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.pdp.appspringsecurity.security.AuthEntryPoint;
import uz.pdp.appspringsecurity.security.AuthFilter;
import uz.pdp.appspringsecurity.security.MyAccessDeniedHandler;
import uz.pdp.appspringsecurity.service.AuthService;
import uz.pdp.appspringsecurity.utils.AppConstants;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthFilter authFilter;
    private final AuthEntryPoint authEntryPoint;
    private final MyAccessDeniedHandler accessDeniedHandler;
    private final AuthService authService;

    public SecurityConfig(
            @Lazy AuthFilter authFilter,
            AuthEntryPoint authEntryPoint,
            MyAccessDeniedHandler accessDeniedHandler,
            AuthService authService) {
        this.authFilter = authFilter;
        this.authEntryPoint = authEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authService = authService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(matcherRegistry ->
                        matcherRegistry
                                .requestMatchers(AppConstants.OPEN_PAGES).permitAll()
                                .requestMatchers("/payment/**").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, AppConstants.CASHIER_GET_PAGES).hasAnyRole("CASHIER")
                                .requestMatchers(HttpMethod.POST, "/payment", "/ketmon", "/tesha").hasAnyRole("CASHIER")
                                .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(httpSession ->
                        httpSession.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authEntryPoint);
                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler);
                });
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(authService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
