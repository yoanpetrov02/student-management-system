/*
package com.yoanpetrov.studentmanagementsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoanpetrov.studentmanagementsystem.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class AccessDeniedHandler extends AccessDeniedHandlerImpl {

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException exception
    ) throws IOException {
        log.debug("AccessDeniedException handled.");
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            log.warn("User [{}] attempted to access the protected resource: {}",
                authentication.getName(),
                request.getRequestURI());
        }
        String json = new ObjectMapper().writeValueAsString(
            new ErrorResponse("401", "You are not authorized to access this resource."));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(json);
        response.flushBuffer();
    }
}
*/
