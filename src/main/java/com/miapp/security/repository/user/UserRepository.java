package com.miapp.security.repository.user;

import com.miapp.sistemasdistribuidos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

    // Método para encontrar un usuario por su nombre
    Usuario findByNombre(String nombre);
    Usuario findByEmail(String email);
}