package com.saberpro.app.controllers;

import com.saberpro.app.models.Usuario;
import com.saberpro.app.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        System.out.println("🔍 Intentando login...");
        System.out.println("   Username recibido: [" + username + "]");

        Usuario usuario = usuarioRepository.findByCorreo(username);

        if (usuario == null) {
            System.out.println("❌ Usuario NO encontrado en BD");
            model.addAttribute("error", true);
            return "login";
        }

        System.out.println("✅ Usuario encontrado:");
        System.out.println("   Correo BD: [" + usuario.getCorreo() + "]");
        System.out.println("   Rol: [" + usuario.getRol() + "]");

        if (usuario.getPassword().equals(password)) {
            System.out.println("✅ Contraseña correcta - Login exitoso");
            session.setAttribute("usuario", usuario);
            
            // Redirigir según el rol
            if ("ADMIN".equals(usuario.getRol())) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/estudiante/dashboard";
            }
        } else {
            System.out.println("❌ Contraseña incorrecta");
        }

        model.addAttribute("error", true);
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}