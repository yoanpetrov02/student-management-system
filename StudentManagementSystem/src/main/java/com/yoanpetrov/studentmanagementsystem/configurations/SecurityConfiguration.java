package com.yoanpetrov.studentmanagementsystem.configurations;

import com.yoanpetrov.studentmanagementsystem.security.JwtRequestFilter;
import com.yoanpetrov.studentmanagementsystem.services.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures the beans that are needed for authentication/authorization.
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITELISTED_ENDPOINTS = {
        "/api/v1/register/**",
        "/api/v1/login/**"
    };

    private final JwtRequestFilter jwtRequestFilter;
    private final UserAccountService userAccountService;
    private final PasswordEncoder passwordEncoder;
   /* private final AccessDeniedHandler accessDeniedHandler;
    private final HttpEntryPoint httpEntryPoint;*/

    /**
     * Builds the {@code SecurityFilterChain} bean.
     * The filter authorizes all requests to the register and login endpoints.
     * All other endpoints have to be authenticated.
     * All created sessions are stateless, as the app uses JWT.
     *
     * @param http the http security builder.
     * @return the {@code SecurityFilterChain} bean.
     * @throws Exception if an error occurs.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(c ->
                c
                    .requestMatchers(WHITELISTED_ENDPOINTS)
                    .permitAll()
                    .anyRequest().authenticated())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            /*.exceptionHandling(
                c -> c
                    .authenticationEntryPoint(httpEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))*/;
        return http.build();
    }

    /**
     * Builds the {@code AuthenticationProvider} bean.
     * The provider is a {@code DaoAuthenticationProvider} and uses a {@code UserAccountDetailsService}
     *
     * @return the {@code AuthenticationProvider} bean.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder);
        authProvider.setUserDetailsService(userAccountService);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}
