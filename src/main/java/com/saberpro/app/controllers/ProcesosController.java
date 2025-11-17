package com.saberpro.app.controllers;

import com.saberpro.app.models.Estudiante;
import com.saberpro.app.models.Incentivo;
import com.saberpro.app.models.Usuario;
import com.saberpro.app.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/procesos")
public class ProcesosController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @GetMapping
    public String procesos(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        // Obtener todos los estudiantes activos
        List<Estudiante> todosEstudiantes = estudianteRepository.findByEstado("ACTIVO");
        
        // Obtener estudiantes anulados
        List<Estudiante> anulados = estudianteRepository.findByEstado("ANULADO");

        // Clasificar estudiantes según incentivos SABER TyT (0-200 puntos)
        List<Estudiante> excelentes = new ArrayList<>();      // > 171 puntos
        List<Estudiante> muyBuenos = new ArrayList<>();       // 151-170 puntos
        List<Estudiante> buenos = new ArrayList<>();          // 120-150 puntos
        List<Estudiante> sinIncentivo = new ArrayList<>();    // 80-119 puntos
        List<Estudiante> noAptos = new ArrayList<>();         // < 80 puntos

        for (Estudiante e : todosEstudiantes) {
            int puntaje = e.getPuntajeSaberPro() != null ? e.getPuntajeSaberPro() : 0;
            
            if (puntaje < 80) {
                noAptos.add(e);
            } else if (puntaje < 120) {
                sinIncentivo.add(e);
            } else if (puntaje <= 150) {
                buenos.add(e);
            } else if (puntaje <= 170) {
                muyBuenos.add(e);
            } else {
                excelentes.add(e);
            }
        }

        // Definir incentivos según el acuerdo - SABER TyT
        List<Incentivo> incentivos = new ArrayList<>();
        
        incentivos.add(new Incentivo(
            "Exoneración de Seminario de grado II con nota 4.5",
            "Bueno",
            "primary",
            120,
            150
        ));
        
        incentivos.add(new Incentivo(
            "Exoneración de Seminario de grado II con nota 4.7 + 50% beca derechos de grado",
            "Muy Bueno",
            "warning",
            151,
            170
        ));
        
        incentivos.add(new Incentivo(
            "Exoneración de Seminario de grado II con nota 5.0 + 100% beca derechos de grado",
            "Excelente",
            "success",
            171,
            200
        ));

        // Estadísticas generales
        int totalEstudiantes = todosEstudiantes.size();
        int conIncentivo = excelentes.size() + muyBuenos.size() + buenos.size();
        double porcentajeIncentivo = totalEstudiantes > 0 ? (conIncentivo * 100.0 / totalEstudiantes) : 0;

        model.addAttribute("usuario", usuario);
        model.addAttribute("incentivos", incentivos);
        model.addAttribute("excelentes", excelentes);
        model.addAttribute("muyBuenos", muyBuenos);
        model.addAttribute("buenos", buenos);
        model.addAttribute("sinIncentivo", sinIncentivo);
        model.addAttribute("noAptos", noAptos);
        model.addAttribute("anulados", anulados);
        model.addAttribute("totalEstudiantes", totalEstudiantes);
        model.addAttribute("conIncentivo", conIncentivo);
        model.addAttribute("porcentajeIncentivo", String.format("%.1f", porcentajeIncentivo));

        return "admin/procesos";
    }
}