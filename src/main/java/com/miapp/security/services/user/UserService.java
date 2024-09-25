package com.miapp.security.services.user;

import com.miapp.sistemasdistribuidos.entity.Usuario;
import com.miapp.sistemasdistribuidos.entity.Rol;

import com.miapp.security.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = userRepository.findByUsername(username);
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
