package com.saberpro.app.controllers;

import com.saberpro.app.models.Estudiante;
import com.saberpro.app.models.Usuario;
import com.saberpro.app.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin/estudiantes")
public class AdminEstudianteController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @GetMapping
    public String listar(HttpSession session, Model model,
                        @RequestParam(required = false) String busqueda) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        List<Estudiante> estudiantes;
        List<Estudiante> anulados = new ArrayList<>();
        
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            // Buscar por apellido o número de registro (incluye TODOS los estados)
            List<Estudiante> todosResultados = estudianteRepository.findByPrimerApellidoContainingIgnoreCaseOrNumeroRegistroContaining(
                busqueda, busqueda);
            
            // Separar activos de anulados
            estudiantes = new ArrayList<>();
            for (Estudiante e : todosResultados) {
                if ("ANULADO".equals(e.getEstado())) {
                    anulados.add(e);
                } else {
                    estudiantes.add(e);
                }
            }
            
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("mostrandoResultados", true);
        } else {
            // Listar estudiantes activos
            estudiantes = estudianteRepository.findByEstadoOrderByPuntajeSaberProDesc("ACTIVO");
            // Listar estudiantes anulados
            anulados = estudianteRepository.findByEstado("ANULADO");
            model.addAttribute("mostrandoResultados", false);
        }

        // Calcular estadísticas solo de activos
        double promedio = 0;
        int maximo = 0;
        int minimo = 0;
        long nivel4 = 0;
        long nivel3 = 0;
        long nivel2 = 0;
        long nivel1 = 0;

        if (!estudiantes.isEmpty()) {
            int suma = 0;
            minimo = Integer.MAX_VALUE;
            
            for (Estudiante e : estudiantes) {
                int puntaje = e.getPuntajeSaberPro() != null ? e.getPuntajeSaberPro() : 0;
                suma += puntaje;
                if (puntaje > maximo) maximo = puntaje;
                if (puntaje < minimo) minimo = puntaje;
                
                // Contar niveles
                if ("Nivel 4".equals(e.getNivelSaberPro())) nivel4++;
                else if ("Nivel 3".equals(e.getNivelSaberPro())) nivel3++;
                else if ("Nivel 2".equals(e.getNivelSaberPro())) nivel2++;
                else if ("Nivel 1".equals(e.getNivelSaberPro())) nivel1++;
            }
            
            promedio = (double) suma / estudiantes.size();
        }

        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("anulados", anulados);
        model.addAttribute("totalEstudiantes", estudiantes.size());
        model.addAttribute("totalAnulados", anulados.size());
        model.addAttribute("promedio", String.format("%.1f", promedio));
        model.addAttribute("maximo", maximo);
        model.addAttribute("minimo", minimo);
        model.addAttribute("nivel4", nivel4);
        model.addAttribute("nivel3", nivel3);
        model.addAttribute("nivel2", nivel2);
        model.addAttribute("nivel1", nivel1);
        model.addAttribute("usuario", usuario);
        
        return "admin/estudiantes";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, HttpSession session, Model model) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);
        
        if (estudiante == null) {
            return "redirect:/admin/estudiantes";
        }

        model.addAttribute("estudiante", estudiante);
        model.addAttribute("usuario", usuario);
        
        return "admin/estudiante-detalle";
    }
}