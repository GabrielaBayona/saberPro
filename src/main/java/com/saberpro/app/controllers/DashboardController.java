package com.saberpro.app.controllers;

import com.saberpro.app.repositories.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UsuarioRepository usuarioRepo;

    public DashboardController(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        long totalUsuarios = usuarioRepo.count();

        model.addAttribute("totalUsuarios", totalUsuarios);

        // Actividad reciente: últimos 5 usuarios creados
        model.addAttribute("actividad",
                usuarioRepo.findAll()
                        .stream()
                        .sorted((a,b) -> b.getId().compareTo(a.getId()))
                        .limit(5)
                        .toList()
        );

        return "dashboard_admin";
    }
}
