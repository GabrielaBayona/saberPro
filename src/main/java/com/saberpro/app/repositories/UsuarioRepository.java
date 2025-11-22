package com.saberpro.app.repositories;

import com.saberpro.app.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByCorreo(String correo);
    List<Usuario> findByCorreoContainingIgnoreCase(String correo);
}