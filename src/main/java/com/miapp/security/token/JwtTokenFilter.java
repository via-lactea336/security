package com.miapp.security.token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, @Lazy UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsernameFromToken(token);
                String role = jwtTokenProvider.getRoleFromToken(token);
                logger.info("Token Username: {}", username);
                logger.info("Role from Token: {}", role);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    logger.info("Authorities from UserDetails: ");
                    for (GrantedAuthority authority : userDetails.getAuthorities()) {
                        logger.info("Authority: {}", authority.getAuthority());
                    }

                    if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role))) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.info("Authentication successful for user: {}", username);
                    } else {
                        logger.warn("Role mismatch. Token role: {}, UserDetails roles: {}", role, userDetails.getAuthorities());
                        throw new ServletException("No tiene los permisos necesarios para acceder a este recurso.");
                    }
                }
            }

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Manejar token expirado
            logger.error("Error de autorización: Token expirado", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"El token ha expirado.\"}");
            return;
        } catch (Exception e) {
            logger.error("Error de autorización: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"No tiene permisos para acceder a este recurso.\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}