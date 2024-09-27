package com.miapp.security.config;

import com.miapp.security.token.JwtTokenFilter;
import com.miapp.security.token.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public JwtConfigurer(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService; // Inicializar el UserDetailsService
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Pasar ambos argumentos al JwtTokenFilter
        JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider, userDetailsService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}