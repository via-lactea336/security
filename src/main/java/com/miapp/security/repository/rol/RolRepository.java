package com.miapp.security.repository.rol;
import com.miapp.sistemasdistribuidos.entity.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Page<Rol> findAll(Pageable pageable);
}
