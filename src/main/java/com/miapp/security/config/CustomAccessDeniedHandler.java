package com.miapp.security.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        // Establecer el código de error 403 (Forbidden)
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Establecer el tipo de contenido como JSON
        response.setContentType("application/json");

        // Escribir el mensaje de error
        response.getWriter().write("{\"error\": \"Acceso denegado. No tienes permiso para acceder a este recurso.\"}");
        response.getWriter().flush();
    }
}