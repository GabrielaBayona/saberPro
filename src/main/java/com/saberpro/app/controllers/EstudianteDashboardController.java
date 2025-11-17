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

    private Estudiante obtenerDatosEstudiante(Usuario usuario) {
        String correo = usuario.getCorreo();
        String apellidoCorreo = correo.split("@")[0];
        
        List<Estudiante> todosEstudiantes = estudianteRepository.findAll();
        
        for (Estudiante e : todosEstudiantes) {
            if (e.getPrimerApellido() != null) {
                if (e.getPrimerApellido().trim().equalsIgnoreCase(apellidoCorreo.trim())) {
                    return e;
                }
            }
        }
        
        return null;
    }
    
    // Método auxiliar para inicializar valores en 0 cuando son null
    private void inicializarValoresNull(Estudiante estudiante) {
        if (estudiante.getPuntajeSaberPro() == null) estudiante.setPuntajeSaberPro(0);
        if (estudiante.getComunicacionEscrita() == null) estudiante.setComunicacionEscrita(0);
        if (estudiante.getRazonamientoCuantitativo() == null) estudiante.setRazonamientoCuantitativo(0);
        if (estudiante.getLecturaCritica() == null) estudiante.setLecturaCritica(0);
        if (estudiante.getCompetenciasCiudadanas() == null) estudiante.setCompetenciasCiudadanas(0);
        if (estudiante.getIngles() == null) estudiante.setIngles(0);
        if (estudiante.getFormulacionProyectos() == null) estudiante.setFormulacionProyectos(0);
        if (estudiante.getPensamientoCientifico() == null) estudiante.setPensamientoCientifico(0);
        if (estudiante.getDisenoSoftware() == null) estudiante.setDisenoSoftware(0);
        
        // Inicializar niveles
        if (estudiante.getComunicacionEscritaNivel() == null) estudiante.setComunicacionEscritaNivel("N/A");
        if (estudiante.getRazonamientoCuantitativoNivel() == null) estudiante.setRazonamientoCuantitativoNivel("N/A");
        if (estudiante.getLecturaCriticaNivel() == null) estudiante.setLecturaCriticaNivel("N/A");
        if (estudiante.getCompetenciasCiudadanasNivel() == null) estudiante.setCompetenciasCiudadanasNivel("N/A");
        if (estudiante.getInglesNivel() == null) estudiante.setInglesNivel("N/A");
        if (estudiante.getNivelIngles() == null) estudiante.setNivelIngles("A0");
        if (estudiante.getFormulacionProyectosNivel() == null) estudiante.setFormulacionProyectosNivel("N/A");
        if (estudiante.getPensamientoCientificoNivel() == null) estudiante.setPensamientoCientificoNivel("N/A");
        if (estudiante.getDisenoSoftwareNivel() == null) estudiante.setDisenoSoftwareNivel("N/A");
        if (estudiante.getNivelSaberPro() == null) estudiante.setNivelSaberPro("N/A");
    }

    private void calcularEstadisticas(Estudiante misDatos, Model model) {
        if (!"ACTIVO".equals(misDatos.getEstado())) {
            model.addAttribute("posicion", 0);
            model.addAttribute("totalEstudiantes", 0);
            model.addAttribute("percentil", 0.0);
            model.addAttribute("percentilTexto", "0.0");
            model.addAttribute("promedio", 0.0);
            model.addAttribute("promedioTexto", "0.0");
            model.addAttribute("promedioInt", 0);
            model.addAttribute("maximo", 0);
            model.addAttribute("minimo", 0);
            model.addAttribute("incentivo", "No Aplicable");
            model.addAttribute("descripcionIncentivo", "⚠️ Tu prueba fue ANULADA por el ICFES. No tienes acceso a incentivos.");
            model.addAttribute("colorIncentivo", "dark");
            model.addAttribute("mejoresPuntajes", 0);
            model.addAttribute("menoresPuntajes", 0);
            return;
        }
        
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
        model.addAttribute("percentil", percentil);
        model.addAttribute("percentilTexto", String.format("%.1f", percentil));
        model.addAttribute("promedio", promedio);
        model.addAttribute("promedioTexto", String.format("%.1f", promedio));
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
        if (usuario == null) return "redirect:/login";

        Estudiante misDatos = obtenerDatosEstudiante(usuario);
        if (misDatos == null) {
            misDatos = new Estudiante();
            misDatos.setPrimerApellido(usuario.getCorreo().split("@")[0].toUpperCase());
            misDatos.setEstado("ANULADO");
            model.addAttribute("error", "⚠️ No se encontraron tus datos en el sistema");
        }
        
        inicializarValoresNull(misDatos);
        calcularEstadisticas(misDatos, model);
        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiante", misDatos);
        return "estudiante/dashboard";
    }

    @GetMapping("/identificacion")
    public String identificacion(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        
        Estudiante misDatos = obtenerDatosEstudiante(usuario);
        if (misDatos == null) {
            misDatos = new Estudiante();
            misDatos.setPrimerApellido(usuario.getCorreo().split("@")[0].toUpperCase());
            misDatos.setEstado("ANULADO");
        }
        
        inicializarValoresNull(misDatos);
        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiante", misDatos);
        return "estudiante/identificacion";
    }

    @GetMapping("/resultado-global")
    public String resultadoGlobal(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        
        Estudiante misDatos = obtenerDatosEstudiante(usuario);
        if (misDatos == null) {
            misDatos = new Estudiante();
            misDatos.setPrimerApellido(usuario.getCorreo().split("@")[0].toUpperCase());
            misDatos.setEstado("ANULADO");
        }
        
        inicializarValoresNull(misDatos);
        calcularEstadisticas(misDatos, model);
        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiante", misDatos);
        return "estudiante/resultado-global";
    }

    @GetMapping("/resultados-asignaturas")
    public String resultadosAsignaturas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        
        Estudiante misDatos = obtenerDatosEstudiante(usuario);
        if (misDatos == null) {
            misDatos = new Estudiante();
            misDatos.setPrimerApellido(usuario.getCorreo().split("@")[0].toUpperCase());
            misDatos.setEstado("ANULADO");
        }
        
        inicializarValoresNull(misDatos);
        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiante", misDatos);
        return "estudiante/resultados-asignaturas";
    }

    @GetMapping("/beneficio")
    public String beneficio(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        
        Estudiante misDatos = obtenerDatosEstudiante(usuario);
        if (misDatos == null) {
            misDatos = new Estudiante();
            misDatos.setPrimerApellido(usuario.getCorreo().split("@")[0].toUpperCase());
            misDatos.setEstado("ANULADO");
        }
        
        inicializarValoresNull(misDatos);
        calcularEstadisticas(misDatos, model);
        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiante", misDatos);
        return "estudiante/beneficio";
    }

    @GetMapping("/comparacion")
    public String comparacion(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        
        Estudiante misDatos = obtenerDatosEstudiante(usuario);
        if (misDatos == null) {
            misDatos = new Estudiante();
            misDatos.setPrimerApellido(usuario.getCorreo().split("@")[0].toUpperCase());
            misDatos.setEstado("ANULADO");
        }
        
        inicializarValoresNull(misDatos);
        calcularEstadisticas(misDatos, model);
        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiante", misDatos);
        return "estudiante/comparacion";
    }
}