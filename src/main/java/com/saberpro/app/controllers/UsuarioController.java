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
@RequestMapping("/admin/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        // Obtener TODOS los usuarios
        List<Usuario> usuarios = usuarioRepository.findAll();
        
        System.out.println("📊 Cargando lista de usuarios...");
        System.out.println("   Total en BD: " + usuarios.size());
        for (Usuario u : usuarios) {
            System.out.println("   - " + u.getCorreo() + " (" + u.getRol() + ")");
        }
        
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuario", usuario);
        
        return "admin/usuarios";
    }

    @GetMapping("/nuevo")
    public String nuevo(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        model.addAttribute("usuarioForm", new Usuario());
        model.addAttribute("usuario", usuario);
        model.addAttribute("accion", "Crear");
        
        return "admin/usuario-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuarioForm, 
                         RedirectAttributes redirectAttributes,
                         HttpSession session) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        try {
            // Si es un nuevo usuario (id == null), validar que no exista el correo
            if (usuarioForm.getId() == null) {
                Usuario existente = usuarioRepository.findByCorreo(usuarioForm.getCorreo());
                if (existente != null) {
                    redirectAttributes.addFlashAttribute("error", "El correo ya está registrado");
                    return "redirect:/admin/usuarios/nuevo";
                }
            } else {
                // Si es edición y no cambió la contraseña, mantener la anterior
                Usuario usuarioExistente = usuarioRepository.findById(usuarioForm.getId()).orElse(null);
                if (usuarioExistente != null && (usuarioForm.getPassword() == null || usuarioForm.getPassword().trim().isEmpty())) {
                    usuarioForm.setPassword(usuarioExistente.getPassword());
                }
            }

            // Guardar el usuario
            usuarioRepository.save(usuarioForm);
            
            System.out.println("✅ Usuario guardado: " + usuarioForm.getCorreo());
            System.out.println("   Total usuarios en BD: " + usuarioRepository.count());
            
            redirectAttributes.addFlashAttribute("success", "Usuario guardado correctamente");
            
        } catch (Exception e) {
            System.err.println("❌ Error al guardar usuario: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario: " + e.getMessage());
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        Usuario usuarioEditar = usuarioRepository.findById(id).orElse(null);
        
        if (usuarioEditar == null) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/admin/usuarios";
        }

        model.addAttribute("usuarioForm", usuarioEditar);
        model.addAttribute("usuario", usuario);
        model.addAttribute("accion", "Editar");
        
        return "admin/usuario-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        try {
            Usuario usuarioEliminar = usuarioRepository.findById(id).orElse(null);
            
            if (usuarioEliminar == null) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/admin/usuarios";
            }

            // No permitir eliminar el usuario actual
            if (usuarioEliminar.getId().equals(usuario.getId())) {
                redirectAttributes.addFlashAttribute("error", "No puedes eliminar tu propio usuario");
                return "redirect:/admin/usuarios";
            }

            usuarioRepository.deleteById(id);
            
            System.out.println("✅ Usuario eliminado: " + usuarioEliminar.getCorreo());
            System.out.println("   Total usuarios en BD: " + usuarioRepository.count());
            
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente");
            
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
        }

        return "redirect:/admin/usuarios";
    }
}