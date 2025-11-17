package com.saberpro.app.controllers;

import com.saberpro.app.models.Usuario;
import com.saberpro.app.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/configuracion")
public class ConfiguracionController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String configuracion(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        // Recargar usuario desde BD para tener datos actualizados
        Usuario usuarioActualizado = usuarioRepository.findById(usuario.getId()).orElse(usuario);
        
        List<Usuario> todosUsuarios = usuarioRepository.findAll();

        model.addAttribute("usuario", usuarioActualizado);
        model.addAttribute("todosUsuarios", todosUsuarios);

        return "admin/configuracion";
    }

    @PostMapping("/cambiar-password")
    public String cambiarPassword(@RequestParam String passwordActual,
                                  @RequestParam String passwordNueva,
                                  @RequestParam String passwordConfirmar,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        try {
            // Obtener usuario actual de BD
            Usuario usuarioBD = usuarioRepository.findById(usuario.getId()).orElse(null);
            
            if (usuarioBD == null) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/admin/configuracion";
            }

            // Verificar contraseña actual
            if (!usuarioBD.getPassword().equals(passwordActual)) {
                redirectAttributes.addFlashAttribute("error", "La contraseña actual es incorrecta");
                return "redirect:/admin/configuracion";
            }

            // Verificar que las nuevas contraseñas coincidan
            if (!passwordNueva.equals(passwordConfirmar)) {
                redirectAttributes.addFlashAttribute("error", "Las contraseñas nuevas no coinciden");
                return "redirect:/admin/configuracion";
            }

            // Validar longitud
            if (passwordNueva.length() < 6) {
                redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres");
                return "redirect:/admin/configuracion";
            }

            // Actualizar contraseña
            usuarioBD.setPassword(passwordNueva);
            usuarioRepository.save(usuarioBD);

            // Actualizar sesión
            session.setAttribute("usuario", usuarioBD);

            redirectAttributes.addFlashAttribute("success", "Contraseña cambiada exitosamente");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar la contraseña: " + e.getMessage());
        }

        return "redirect:/admin/configuracion";
    }

    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(@RequestParam String correo,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        try {
            Usuario usuarioBD = usuarioRepository.findById(usuario.getId()).orElse(null);
            
            if (usuarioBD == null) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/admin/configuracion";
            }

            // Verificar que el nuevo correo no esté en uso por otro usuario
            Usuario existente = usuarioRepository.findByCorreo(correo);
            if (existente != null && !existente.getId().equals(usuarioBD.getId())) {
                redirectAttributes.addFlashAttribute("error", "El correo ya está en uso por otro usuario");
                return "redirect:/admin/configuracion";
            }

            usuarioBD.setCorreo(correo);
            usuarioRepository.save(usuarioBD);

            // Actualizar sesión
            session.setAttribute("usuario", usuarioBD);

            redirectAttributes.addFlashAttribute("success", "Perfil actualizado correctamente");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el perfil: " + e.getMessage());
        }

        return "redirect:/admin/configuracion";
    }

    @PostMapping("/restablecer-password-usuario")
    public String restablecerPasswordUsuario(@RequestParam Long usuarioId,
                                            @RequestParam String nuevaPassword,
                                            HttpSession session,
                                            RedirectAttributes redirectAttributes) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        try {
            Usuario usuarioModificar = usuarioRepository.findById(usuarioId).orElse(null);
            
            if (usuarioModificar == null) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/admin/configuracion";
            }

            // Validar longitud
            if (nuevaPassword.length() < 6) {
                redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres");
                return "redirect:/admin/configuracion";
            }

            usuarioModificar.setPassword(nuevaPassword);
            usuarioRepository.save(usuarioModificar);

            redirectAttributes.addFlashAttribute("success", 
                "Contraseña restablecida para " + usuarioModificar.getCorreo());
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al restablecer contraseña: " + e.getMessage());
        }

        return "redirect:/admin/configuracion";
    }
}