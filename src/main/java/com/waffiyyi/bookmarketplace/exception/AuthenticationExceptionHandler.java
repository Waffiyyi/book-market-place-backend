package com.waffiyyi.bookmarketplace.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@Slf4j
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("Authentication exception type: " + authException.getClass().getName());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        String message;
        if (authException instanceof CustomAuthenticationException) {
            message = authException.getMessage();
        } else {
            message = authException.getMessage();
        }

        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write("{ \"errorMessage\": \"" + message + "\" }");
        writer.flush();
    }


}
