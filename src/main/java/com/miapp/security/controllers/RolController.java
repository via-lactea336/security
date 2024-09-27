package com.miapp.security.controllers;

import com.miapp.security.services.rol.RolService;
import com.miapp.sistemasdistribuidos.dto.RolDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    int size = 10;

    @Autowired
    private RolService rolService;

    // Obtener Roles paginados
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<Page<RolDTO>> getAllRoles(
            @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RolDTO> result = rolService.getAllRols(pageable);
        return ResponseEntity.ok(result);
    }

    // Obtener un Rol por ID
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> getRolById(@PathVariable Integer id) {
        Optional<RolDTO> rol = rolService.getRolByID(id);
        return rol.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo rol
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<RolDTO> createRol(@RequestBody RolDTO rolDTO) {
        RolDTO newRol = rolService.createRol(rolDTO);
        return ResponseEntity.ok(newRol);
    }

    // Actualizar un rol existente
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RolDTO> updateRol(@PathVariable Integer id, @RequestBody RolDTO rolDTO) {
        rolDTO.setRolId(String.valueOf(id));
        RolDTO updatedRol = rolService.updateRol(rolDTO);
        return updatedRol != null ? ResponseEntity.ok(updatedRol) : ResponseEntity.notFound().build();
    }

    // Eliminar un rol
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable Integer id) {
        rolService.deleteRol(id);
        return ResponseEntity.noContent().build();
    }
}
