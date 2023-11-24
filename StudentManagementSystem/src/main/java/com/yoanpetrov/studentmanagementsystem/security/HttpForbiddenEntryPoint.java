package com.yoanpetrov.studentmanagementsystem.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpForbiddenEntryPoint extends Http403ForbiddenEntryPoint {

    private final HttpEndpointChecker httpEndpointChecker;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        if (!httpEndpointChecker.existsEndpoint(request)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested endpoint does not exist.");
            return;
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You do not have access to this resource.");
    }
}
