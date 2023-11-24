package com.yoanpetrov.studentmanagementsystem.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

@Component
@RequiredArgsConstructor
@Slf4j
public class HttpEndpointChecker {

    private final DispatcherServlet dispatcherServlet;

    public boolean existsEndpoint(HttpServletRequest request) {
        log.debug("Checking if requested endpoint exists...");
        for (HandlerMapping mapping : dispatcherServlet.getHandlerMappings()) {
            try {
                HandlerExecutionChain handler = mapping.getHandler(request);
                if (handler != null) {
                    log.debug("Handler exists.");
                    return true;
                }
            } catch (Exception e) {
                log.error("Error while checking endpoint existence.", e);
                break;
            }
        }
        log.debug("Handler does not exist.");
        return false;
    }
}
