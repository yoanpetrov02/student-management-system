package com.yoanpetrov.studentmanagementsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoanpetrov.studentmanagementsystem.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException {
        log.debug("403 entry point overriding class called. Returning 401 with a message to the client.");
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        String json = new ObjectMapper().writeValueAsString(
            new ErrorResponse("401", "You are not authorized to access this resource."));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(json);
        response.flushBuffer();
    }
}
