package com.saberpro.app.controllers;

import com.saberpro.app.models.Estudiante;
import com.saberpro.app.models.Usuario;
import com.saberpro.app.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/estadisticas")
public class EstadisticasController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @GetMapping
    public String estadisticas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        List<Estudiante> estudiantes = estudianteRepository.findByEstado("ACTIVO");

        // Estadísticas generales
        int total = estudiantes.size();
        double promedio = 0;
        int maximo = 0;
        int minimo = Integer.MAX_VALUE;

        // Contadores por nivel
        int nivel4 = 0, nivel3 = 0, nivel2 = 0, nivel1 = 0;

        // Contadores por rango de puntaje
        int rango0_80 = 0, rango80_120 = 0, rango120_150 = 0, rango150_170 = 0, rango170_200 = 0;

        // Promedios por competencia
        double promComEscrita = 0, promRazCuant = 0, promLecCrit = 0, promCompCiu = 0;
        double promIngles = 0, promFormProy = 0, promPensCien = 0, promDisSoft = 0;

        // Distribución nivel inglés
        int inglesA0 = 0, inglesA1 = 0, inglesA2 = 0, inglesB1 = 0, inglesB2 = 0;

        if (!estudiantes.isEmpty()) {
            int suma = 0;

            for (Estudiante e : estudiantes) {
                int puntaje = e.getPuntajeSaberPro() != null ? e.getPuntajeSaberPro() : 0;
                suma += puntaje;

                if (puntaje > maximo) maximo = puntaje;
                if (puntaje < minimo) minimo = puntaje;

                // Contar por nivel
                if ("Nivel 4".equals(e.getNivelSaberPro())) nivel4++;
                else if ("Nivel 3".equals(e.getNivelSaberPro())) nivel3++;
                else if ("Nivel 2".equals(e.getNivelSaberPro())) nivel2++;
                else if ("Nivel 1".equals(e.getNivelSaberPro())) nivel1++;

                // Contar por rango
                if (puntaje < 80) rango0_80++;
                else if (puntaje < 120) rango80_120++;
                else if (puntaje < 150) rango120_150++;
                else if (puntaje < 170) rango150_170++;
                else rango170_200++;

                // Sumar competencias
                promComEscrita += e.getComunicacionEscrita() != null ? e.getComunicacionEscrita() : 0;
                promRazCuant += e.getRazonamientoCuantitativo() != null ? e.getRazonamientoCuantitativo() : 0;
                promLecCrit += e.getLecturaCritica() != null ? e.getLecturaCritica() : 0;
                promCompCiu += e.getCompetenciasCiudadanas() != null ? e.getCompetenciasCiudadanas() : 0;
                promIngles += e.getIngles() != null ? e.getIngles() : 0;
                promFormProy += e.getFormulacionProyectos() != null ? e.getFormulacionProyectos() : 0;
                promPensCien += e.getPensamientoCientifico() != null ? e.getPensamientoCientifico() : 0;
                promDisSoft += e.getDisenoSoftware() != null ? e.getDisenoSoftware() : 0;

                // Contar nivel inglés
                String nivelIngles = e.getNivelIngles();
                if ("A0".equals(nivelIngles)) inglesA0++;
                else if ("A1".equals(nivelIngles)) inglesA1++;
                else if ("A2".equals(nivelIngles)) inglesA2++;
                else if ("B1".equals(nivelIngles)) inglesB1++;
                else if ("B2".equals(nivelIngles)) inglesB2++;
            }

            promedio = (double) suma / total;
            promComEscrita /= total;
            promRazCuant /= total;
            promLecCrit /= total;
            promCompCiu /= total;
            promIngles /= total;
            promFormProy /= total;
            promPensCien /= total;
            promDisSoft /= total;
        }

        // Pasar datos al modelo
        model.addAttribute("usuario", usuario);
        model.addAttribute("total", total);
        model.addAttribute("promedio", String.format("%.1f", promedio));
        model.addAttribute("maximo", maximo);
        model.addAttribute("minimo", minimo == Integer.MAX_VALUE ? 0 : minimo);

        model.addAttribute("nivel4", nivel4);
        model.addAttribute("nivel3", nivel3);
        model.addAttribute("nivel2", nivel2);
        model.addAttribute("nivel1", nivel1);

        model.addAttribute("rango0_80", rango0_80);
        model.addAttribute("rango80_120", rango80_120);
        model.addAttribute("rango120_150", rango120_150);
        model.addAttribute("rango150_170", rango150_170);
        model.addAttribute("rango170_200", rango170_200);

        model.addAttribute("promComEscrita", String.format("%.1f", promComEscrita));
        model.addAttribute("promRazCuant", String.format("%.1f", promRazCuant));
        model.addAttribute("promLecCrit", String.format("%.1f", promLecCrit));
        model.addAttribute("promCompCiu", String.format("%.1f", promCompCiu));
        model.addAttribute("promIngles", String.format("%.1f", promIngles));
        model.addAttribute("promFormProy", String.format("%.1f", promFormProy));
        model.addAttribute("promPensCien", String.format("%.1f", promPensCien));
        model.addAttribute("promDisSoft", String.format("%.1f", promDisSoft));

        model.addAttribute("inglesA0", inglesA0);
        model.addAttribute("inglesA1", inglesA1);
        model.addAttribute("inglesA2", inglesA2);
        model.addAttribute("inglesB1", inglesB1);
        model.addAttribute("inglesB2", inglesB2);

        return "admin/estadisticas";
    }
}