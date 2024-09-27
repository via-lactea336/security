package com.miapp.security.services.rol;

import com.miapp.sistemasdistribuidos.dto.RolDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IRolService {
    Optional<RolDTO> getRolByID(Integer id);

    RolDTO createRol(RolDTO rol);
    RolDTO updateRol(RolDTO rol);
    void deleteRol(Integer id);

    Page<RolDTO> getAllRols(Pageable pageable);
}
