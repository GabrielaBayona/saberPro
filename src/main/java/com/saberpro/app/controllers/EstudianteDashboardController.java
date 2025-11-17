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
@RequestMapping("/estudiante")
public class EstudianteDashboardController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    // Método auxiliar para obtener datos del estudiante
    //private Estudiante obtenerDatosEstudiante(Usuario usuario) {
      //  String correo = usuario.getCorreo();
      //  String apellido = correo.split("@")[0].toUpperCase();
      //  List<Estudiante> estudiantes = estudianteRepository.findByPrimerApellidoContainingIgnoreCaseOrNumeroRegistroContaining(apellido, "");
      //  return estudiantes.isEmpty() ? null : estudiantes.get(0);
    //}
    private Estudiante obtenerDatosEstudiante(Usuario usuario) {

        String correo = usuario.getCorreo();
        String apellido = correo.split("@")[0]; // ejemplo: "parra"

        // Buscar por apellido exacto ignorando mayúsculas
        Estudiante estudiante = estudianteRepository.findByPrimerApellidoIgnoreCase(apellido);

        return estudiante;
    }

    // Método auxiliar para calcular estadísticas
    private void calcularEstadisticas(Estudiante misDatos, Model model) {
        List<Estudiante> todosEstudiantes = estudianteRepository.findByEstado("ACTIVO");
        
        int miPuntaje = misDatos.getPuntajeSaberPro() != null ? misDatos.getPuntajeSaberPro() : 0;
        int mejoresPuntajes = 0;
        int menoresPuntajes = 0;
        
        double promedio = 0;
        int maximo = 0;
        int minimo = Integer.MAX_VALUE;

        for (Estudiante e : todosEstudiantes) {
            int puntaje = e.getPuntajeSaberPro() != null ? e.getPuntajeSaberPro() : 0;
            promedio += puntaje;
            
            if (puntaje > maximo) maximo = puntaje;
            if (puntaje < minimo) minimo = puntaje;
            
            if (puntaje > miPuntaje) mejoresPuntajes++;
            else if (puntaje < miPuntaje) menoresPuntajes++;
        }
        
        promedio = promedio / todosEstudiantes.size();
        int posicion = mejoresPuntajes + 1;
        double percentil = ((double) menoresPuntajes / todosEstudiantes.size()) * 100;

        // Determinar incentivo
        String incentivo = "Sin incentivo";
        String descripcionIncentivo = "No alcanzaste el puntaje mínimo para incentivos (120 puntos)";
        String colorIncentivo = "secondary";
        
        if (miPuntaje >= 171) {
            incentivo = "Excelente";
            descripcionIncentivo = "Exoneración Seminario II con nota 5.0 + 100% beca derechos de grado";
            colorIncentivo = "success";
        } else if (miPuntaje >= 151) {
            incentivo = "Muy Bueno";
            descripcionIncentivo = "Exoneración Seminario II con nota 4.7 + 50% beca derechos de grado";
            colorIncentivo = "warning";
        } else if (miPuntaje >= 120) {
            incentivo = "Bueno";
            descripcionIncentivo = "Exoneración Seminario II con nota 4.5";
            colorIncentivo = "primary";
        } else if (miPuntaje < 80) {
            incentivo = "No Apto";
            descripcionIncentivo = "⚠️ No puedes graduarte con este puntaje. Debes presentar nuevamente la prueba.";
            colorIncentivo = "danger";
        }

        model.addAttribute("posicion", posicion);
        model.addAttribute("totalEstudiantes", todosEstudiantes.size());
        model.addAttribute("percentil", percentil);                 // número real
        model.addAttribute("percentilTexto", String.format("%.1f", percentil));  // texto para mostrar

        model.addAttribute("promedio", promedio);                  // número real
        model.addAttribute("promedioTexto", String.format("%.1f", promedio));    // texto formateado

        model.addAttribute("promedioInt", (int) Math.round(promedio));

        model.addAttribute("maximo", maximo);
        model.addAttribute("minimo", minimo == Integer.MAX_VALUE ? 0 : minimo);
        model.addAttribute("incentivo", incentivo);
        model.addAttribute("descripcionIncentivo", descripcionIncentivo);
        model.addAttribute("colorIncentivo", colorIncentivo);
        model.addAttribute("mejoresPuntajes", mejoresPuntajes);
        model.addAttribute("menoresPuntajes", menoresPuntajes);
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        Estudiante misDatos = obtenerDatosEstudiante(usuario);
        if (misDatos == null) {
            model.addAttribute("error", "No se encontraron datos para tu usuario");
            model.addAttribute("usuario", usuario);
            return "estudiante/dashboard";
        }

        calcularEstadisticas(misDatos, model);
        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiante", misDatos);
        
        return "estudiante/dashboard";
    }

    @GetMapping("/identificacion")
    public String identificacion(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        Estudiante misDatos = obtenerDatosEstudiante(usuario);
        if (misDatos == null) {
            return "redirect:/estudiante/dashboard";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiante", misDatos);
        
        return "estudiante/identificacion";
    }

    @GetMapping("/resultado-global")
    public String resultadoGlobal(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        Estudiante misDatos = obtenerDatosEstudiante(usuario);
        if (misDatos == null) {
            return "redirect:/estudiante/dashboard";
        }

        calcularEstadisticas(misDatos, model);
        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiante", misDatos);
        
        return "estudiante/resultado-global";
    }

    @GetMapping("/resultados-asignaturas")
    public String resultadosAsignaturas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        Estudiante misDatos = obtenerDatosEstudiante(usuario);
        if (misDatos == null) {
            return "redirect:/estudiante/dashboard";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiante", misDatos);
        
        return "estudiante/resultados-asignaturas";
    }

    @GetMapping("/beneficio")
    public String beneficio(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        Estudiante misDatos = obtenerDatosEstudiante(usuario);
        if (misDatos == null) {
            return "redirect:/estudiante/dashboard";
        }

        calcularEstadisticas(misDatos, model);
        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiante", misDatos);
        
        return "estudiante/beneficio";
    }

    @GetMapping("/comparacion")
    public String comparacion(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        Estudiante misDatos = obtenerDatosEstudiante(usuario);
        if (misDatos == null) {
            return "redirect:/estudiante/dashboard";
        }

        calcularEstadisticas(misDatos, model);
        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiante", misDatos);
        
        return "estudiante/comparacion";
    }
}