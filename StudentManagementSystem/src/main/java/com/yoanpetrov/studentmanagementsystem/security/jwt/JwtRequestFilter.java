package com.yoanpetrov.studentmanagementsystem.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// TODO: 24-Oct-23 Add a case where the user is already validated.
// TODO: 24-Oct-23 Fix any faulty logic.

/**
 * A JWT authentication filter, executed on every incoming request.
 */
@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final int BEARER_TOKEN_START = 7;

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    /**
     * Validates the JWT token and sets the authentication on the {@code SecurityContext} on successful validation.
     *
     * @param request     the http request.
     * @param response    the http response.
     * @param filterChain the filter chain.
     * @throws ServletException if an error occurs in the servlet.
     * @throws IOException      if an i/o error occurs.
     */
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (!validateAuthHeader(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwt = authHeader.substring(BEARER_TOKEN_START);
        final String username = jwtService.extractUsername(jwt);

        if (username != null && noExistingAuthentication()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                var authToken = createAuthToken(userDetails);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Validates the Authorization header.
     *
     * @param authHeader the header.
     * @return true if the header contains a Bearer token, false if it's null or contains something else.
     */
    private boolean validateAuthHeader(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    /**
     * Checks whether there is no existing authentication in the {@code SecurityContext}.
     *
     * @return true if there is no authentication, false if there is.
     */
    private boolean noExistingAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    /**
     * Creates a {@code UsernamePasswordAuthenticationToken} for the given {@code UserDetails}.
     *
     * @param userDetails the user details to create the auth token for.
     * @return the auth token.
     */
    private UsernamePasswordAuthenticationToken createAuthToken(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());
    }

    /**
     * Sets the current {@code Authentication} in the {@code SecurityContext}.
     *
     * @param authentication the auth token.
     */
    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
