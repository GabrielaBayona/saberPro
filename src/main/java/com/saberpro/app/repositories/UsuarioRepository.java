package com.saberpro.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.saberpro.app.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByCorreo(String correo);
}
