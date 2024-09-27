package com.miapp.security.controllers;

import com.miapp.security.dto.JwtResponse;
import com.miapp.security.dto.LoginRequest;
import com.miapp.sistemasdistribuidos.dto.UsuarioCreateDTO;
import com.miapp.security.services.user.UserService;
import com.miapp.security.token.JwtTokenProvider;  // Asegúrate de importar JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;  // Inyectar JwtTokenProvider

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UsuarioCreateDTO userDto) {
        try {
            userService.register(userDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error registering user: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Pasar jwtTokenProvider como argumento
            String token = userService.login(loginRequest, jwtTokenProvider);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            // Crear un mapa para representar el error como un JSON
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Unauthorized");
            errorResponse.put("details", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

}
