package com.saberpro.app.controllers;

import com.saberpro.app.models.Usuario;
import com.saberpro.app.repositories.EstudianteRepository;
import com.saberpro.app.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        
        // Estadísticas reales
        Long totalEstudiantes = estudianteRepository.countEstudiantesActivos();
        Long totalUsuarios = usuarioRepository.count();
        Double promedioSaberPro = estudianteRepository.promedioSaberPro();
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("totalEstudiantes", totalEstudiantes);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("promedioSaberPro", promedioSaberPro != null ? Math.round(promedioSaberPro) : 0);
        
        return "admin/dashboard";
    }
}