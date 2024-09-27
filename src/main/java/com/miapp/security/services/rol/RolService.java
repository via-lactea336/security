package com.miapp.security.services.rol;


import com.miapp.security.repository.rol.RolRepository;
import com.miapp.sistemasdistribuidos.dto.RolDTO;
import com.miapp.sistemasdistribuidos.entity.Rol;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;



@Service
@Transactional
public class RolService implements IRolService {
    private static final Logger logger = LoggerFactory.getLogger(RolService.class);
    @Autowired
    private RolRepository rolRepository;

    private RolDTO convertToDTO(Rol rol) {
        RolDTO rolDTO = new RolDTO();
        rolDTO.setRolId(String.valueOf(rol.getRolId()));
        rolDTO.setDescripcionRol(rol.getDescripcionRol());
        rolDTO.setNombreRol(rol.getNombreRol());
        return rolDTO;
    }
    private Rol convertToEntity(RolDTO rolDTO) {
        Rol rol = new Rol();
        rol.setNombreRol(rolDTO.getNombreRol());
        rol.setDescripcionRol(rolDTO.getDescripcionRol());
        rol.setRolId(Integer.valueOf(rolDTO.getRolId()));

        return rol;
    }

    @Override
    public Optional<RolDTO> getRolByID(Integer id) {
        try {
            Optional<Rol> rol = rolRepository.findById(Long.valueOf(id));
            if (rol.isPresent()) {
                Optional<RolDTO> rolDTO = rol.map(this::convertToDTO);
                logger.info("Retornando Rol con ID: {}", id);
                return rolDTO;
            } else {
                logger.error("No se encontró el rol con el ID: {}", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado");
            }
        } catch (Exception e) {
            logger.error("Error al obtener el rol: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public RolDTO createRol(RolDTO rol) {
        try {
            Rol newRol = convertToEntity(rol);
            Rol savedRol = rolRepository.save(newRol);
            logger.info("Rol creado con éxito: {}", savedRol);
            return convertToDTO(savedRol);
        } catch (Exception e) {
            logger.error("Error al crear el rol: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public RolDTO updateRol(RolDTO rol) {
        try {
            Optional<Rol> savedRol = rolRepository.findById(Long.valueOf(rol.getRolId()));
            if (savedRol.isPresent()) {
                Rol newRol = convertToEntity(rol);
                rolRepository.save(newRol);
                logger.info("Rol actualizado con éxito: {}", rol);
                return rol;
            } else {
                logger.error("No se encontró el rol con el ID: {}", rol.getRolId());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado");
            }
        } catch (Exception e) {
            logger.error("Error al actualizar el rol: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteRol(Integer id) {
        try{
            rolRepository.deleteById(Long.valueOf(id));
            logger.info("Rol eliminado con ID: {}" + id);
        }catch (Exception e) {
            logger.error("Error al eliminar el rol: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Page<RolDTO> getAllRols(Pageable pageable) {
        try{
            Page<RolDTO> roles = rolRepository.findAll(pageable).map(this::convertToDTO);
            logger.info("Retornando lista de roles paginada");
            return roles;
        }catch (Exception e) {
            logger.error("Error al obtener los roles: {}", e.getMessage());
            throw e;
        }
    }
}
