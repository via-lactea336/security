package com.miapp.security.services.user;

import com.miapp.security.dto.LoginRequest;
import com.miapp.security.token.JwtTokenProvider;
import com.miapp.sistemasdistribuidos.dto.UsuarioCreateDTO;
import com.miapp.sistemasdistribuidos.entity.Usuario;
import com.miapp.sistemasdistribuidos.entity.Rol;

import com.miapp.security.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(UsuarioCreateDTO userDto) {
        // Crear un nuevo usuario y guardarlo en la base de datos
        Usuario usuario = new Usuario();
        usuario.setNombre(userDto.getNombre());
        usuario.setEmail(userDto.getEmail());
        // Encriptar la contraseña
        usuario.setContrasena(passwordEncoder.encode(userDto.getContrasena()));
        usuario.setActivo(userDto.getActivo());
        usuario.setBio(userDto.getBio());
        usuario.setDireccion(userDto.getDireccion());
        usuario.setTelefono(userDto.getTelefono());
        Rol rol = new Rol();
        rol.setRolId(Integer.parseInt(userDto.getRolId()));
        usuario.setRolId(rol);
        usuario.setCreatedAt(LocalDateTime.now());
        userRepository.save(usuario);
    }

    public String login(LoginRequest loginRequest, JwtTokenProvider jwtTokenProvider) {
        // Lógica para validar el usuario y generar el token
        // Suponiendo que tienes un método para verificar el usuario:
        Usuario user = userRepository.findByEmail(loginRequest.getEmail());
        if (user != null && passwordEncoder.matches(loginRequest.getContrasena(), user.getContrasena())) {
            return jwtTokenProvider.generateToken(user.getNombre());
        }
        throw new RuntimeException("Invalid credentials");
    }

    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        Usuario user = userRepository.findByNombre(nombre);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getNombre(),
                user.getContrasena(),
                user.getActivo() !=null ? user.getActivo():false,
                true,
                true,
                true,
                mapRolesToAuthorities(user.getRolId()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Rol rol) {
        return Collections.singleton(new SimpleGrantedAuthority(rol.getNombreRol()));
    }

}
